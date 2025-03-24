package com.vk.common.core.utils.ip;

import com.vk.common.core.utils.StringUtils;
import jakarta.annotation.PostConstruct;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Component
public class SearcherIpToAdder {

    private byte[] vIndex;
    private final String dbPath = "ip2region.xdb";

    @PostConstruct
    private void init() {
        // 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
        // try {
        //     vIndex = Searcher.loadVectorIndexFromFile(dbPath);
        // } catch (Exception e) {
        //     System.out.printf("failed to load vector index from `%s`: %s\n", dbPath, e);
        // }

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(dbPath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("File not found in classpath: " + dbPath);
            }
            vIndex = inputStream.readAllBytes();  // 读取文件内容到 byte[]
        } catch (Exception e) {
            System.out.printf("Failed to load vector index from `%s`: %s\n", dbPath, e);
        }
    }

    public String SearcherToAdder(String ip) {
        Searcher searcher;
        try {
            searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
        } catch (Exception e) {
            return null;
        }

        // 3、查询
        try {
            String region = searcher.search(ip);
            return getLocationAdder(region);

        } catch (Exception e) {
            System.err.printf("searcher close Exception(%s): %s\n", ip, e);
        }

        try {
            searcher.close();
        } catch (IOException e) {
            System.out.printf("searcher close IOException(%s): %s\n", ip, e);
        }

        return null;

    }

    private String getLocationAdder(String region) {
        // 中国|0|福建省|厦门市|电信
        String[] splitRegion = region.split("\\|");
        if (StringUtils.isNotEmpty(splitRegion)) {
            String province = splitRegion[2];
            if (!province.equals("0")) {
                return province.substring(0,province.length()-1);
            }
            return "未知IP";
        }
        return null;
    }


}
