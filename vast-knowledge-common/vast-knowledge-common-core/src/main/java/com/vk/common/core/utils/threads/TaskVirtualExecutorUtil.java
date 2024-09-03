package com.vk.common.core.utils.threads;

import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskVirtualExecutorUtil {

    private static final Logger log = LoggerFactory.getLogger(TaskVirtualExecutorUtil.class);

    public static void executeWith(Runnable task) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(task);
        } catch (Exception e) {
            log.error("Error executing task: {}", e.getMessage(), e);
        }
    }
}
