package com.vk.common.core.utils.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 虚拟线程工具类
 */
public class TaskVirtualExecutorUtil {

    private static final Logger log = LoggerFactory.getLogger(TaskVirtualExecutorUtil.class);

    public static void executeWith(Runnable task) {
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        try {
            executor.submit(task);
        } catch (Exception e) {
            log.error("Error executing task: {}", e.getMessage(), e);
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.warn("Executor did not terminate in the allotted time.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Executor termination interrupted: {}", e.getMessage(), e);
            }
        }
    }

    public static <V> V executeWith(Callable<V> task) {
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        try {
            // Submit the task and get a Future
            var future = executor.submit(task);
            // Get the result of the computation
            return future.get();
        } catch (Exception e) {
            log.error("Error executing task: {}", e.getMessage(), e);
            return null; // You can choose to return a default value or throw a runtime exception
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.warn("Executor did not terminate in the allotted time.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Executor termination interrupted: {}", e.getMessage(), e);
            }
        }
    }

}
