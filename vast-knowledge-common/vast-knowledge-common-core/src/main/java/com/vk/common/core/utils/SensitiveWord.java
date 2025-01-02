package com.vk.common.core.utils;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 敏感词过滤
 */
@Component
public class SensitiveWord {
    private static final Logger log = LoggerFactory.getLogger(SensitiveWord.class);

    private final Set<String> sensitiveWordSet = new CopyOnWriteArraySet<>();
    private final Map<String, AtomicInteger> sensitiveWordCountMap = new ConcurrentHashMap<>();
    private final TrieNode root = new TrieNode();

    private static final String FILE_NAME = "CensorWords.txt";

    // 在Spring启动后，异步加载敏感词
    @PostConstruct
    private void init() {
        loadSensitiveWordsAsync();  // 使用虚拟线程异步加载敏感词
    }

    // 使用虚拟线程异步加载敏感词
    public void loadSensitiveWordsAsync() {
        // 创建虚拟线程池
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        // 异步加载敏感词
        executor.submit(() -> {
            try {
                loadWordsFromFile();
                buildTrie();
            } catch (IOException e) {
                log.error("初始化敏感词失败：{}", e.getMessage());
                // e.printStackTrace();

            }
            log.info("初始化敏感词完成");
        });

        executor.shutdown();  // 关闭线程池
    }

    public void addSensitive(Set<String> addSensitive) {

            if (sensitiveWordSet.isEmpty()) {
                log.error("敏感词未初始化");
                return;
            }
            if (addSensitive != null && !addSensitive.isEmpty()) {
                sensitiveWordSet.addAll(addSensitive);
            }
            log.info("数据库敏感词添加部分完成");


    }

    public void removeSensitive(Set<String> addSensitive) {
            if (sensitiveWordSet.isEmpty()) {
                log.error("敏感词未初始化");
                return;
            }
            if (addSensitive != null && !addSensitive.isEmpty()) {
                sensitiveWordSet.removeAll(addSensitive);
            }
            log.info("数据库敏感词排除部分完成");


    }

    // 从文件加载敏感词
    private void loadWordsFromFile() throws IOException {
        try (InputStreamReader read = new InputStreamReader(
                Objects.requireNonNull(SensitiveWord.class.getClassLoader().getResourceAsStream(FILE_NAME)), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(read)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sensitiveWordSet.add(line);
            }
        }
    }

    // 构建 Trie 树
    private void buildTrie() {
        for (String word : sensitiveWordSet) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.isEnd = true;
        }
    }

    // 检查文本中的敏感词
    public Map<String, AtomicInteger> filterInfo(String str) {
        Map<String, AtomicInteger> resultMap = new ConcurrentHashMap<>();

        // 获取系统核心数
        int numCores = Runtime.getRuntime().availableProcessors();
        int totalLength = str.length();
        int chunkSize = Math.max(totalLength / (numCores * 2), 5000);

        // 使用虚拟线程池并行处理多个子任务
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        List<Callable<Void>> tasks = new ArrayList<>();

        // 拆分大文本为多个小任务
        for (int i = 0; i < totalLength; i += chunkSize) {
            int end = Math.min(i + chunkSize, totalLength);
            String textChunk = str.substring(i, end);

            // 为每个文本片段创建一个任务
            tasks.add(() -> {
                checkSensitiveWords(textChunk, resultMap);  // 调用处理方法
                return null;
            });
        }

        try {
            executor.invokeAll(tasks);  // 执行并行任务
        } catch (InterruptedException e) {
            log.error("文本任务对比失败：{}", e.getMessage());
        } finally {
            executor.shutdown();
        }

        return resultMap;
    }


    // 短文本处理
    public Map<String, AtomicInteger> filterInfoShortText(String str) {
        Map<String, AtomicInteger> resultMap = new HashMap<>();
        checkSensitiveWords(str, resultMap);  // 直接调用提取出来的方法
        return resultMap;
    }

    // 提取出的公共方法，检查字符串中的敏感词
    private void checkSensitiveWords(String str, Map<String, AtomicInteger> resultMap) {
        for (int i = 0; i < str.length(); i++) {
            TrieNode node = root;
            for (int j = i; j < str.length(); j++) {
                char c = str.charAt(j);
                node = node.children.get(c);
                if (node == null) break;

                if (node.isEnd) {
                    String word = str.substring(i, j + 1);
                    resultMap.computeIfAbsent(word, k -> new AtomicInteger(0)).incrementAndGet();
                }
            }
        }
    }

    // Trie 树节点
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
    }
}
