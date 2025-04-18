package com.vk.ai.template.impl;

import com.vk.common.core.utils.uuid.UUID;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class SseStreamHandler<T> {

    private UUID uuid;
    private Long timeOut=60000L;
    private Flowable<T> flowable;
    private Function<T, Optional<String>> contentMapper;
    private Runnable onComplete = () -> {
    };
    private Consumer<Throwable> onError = e -> {
    };
    private Consumer<Throwable> onEmitterError = e -> {
    };
    private Runnable onTimeout = () -> {
    };

    private SseStreamHandler() {
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private final SseStreamHandler<T> handler = new SseStreamHandler<>();

        public Builder<T> timeOut(Long timeOut) {
            handler.timeOut = timeOut;
            return this;
        }

        public Builder<T> uuid(UUID uuid) {
            handler.uuid = uuid;
            return this;
        }

        public Builder<T> flowable(Flowable<T> flowable) {
            handler.flowable = flowable;
            return this;
        }

        public Builder<T> contentMapper(Function<T, Optional<String>> contentMapper) {
            handler.contentMapper = contentMapper;
            return this;
        }

        public Builder<T> onComplete(Runnable onComplete) {
            handler.onComplete = onComplete;
            return this;
        }

        public Builder<T> onError(Consumer<Throwable> onError) {
            handler.onError = onError;
            return this;
        }

        public Builder<T> onEmitterError(Consumer<Throwable> onEmitterError) {
            handler.onEmitterError = onEmitterError;
            return this;
        }

        public Builder<T> onTimeout(Runnable onTimeout) {
            handler.onTimeout = onTimeout;
            return this;
        }

        public SseEmitter build() {
            SseEmitter emitter = new SseEmitter(handler.timeOut);

            emitter.onCompletion(handler.onComplete);
            emitter.onTimeout(handler.onTimeout);
            emitter.onError(handler.onEmitterError);

            try {
                handler.flowable
                        .doOnError(error -> {
                            log.error("流处理出错 uuid:{} error:{}", handler.uuid, error.getMessage());
                            try {
                                emitter.send("Error occurred: " + error.getMessage(), MediaType.TEXT_EVENT_STREAM);
                            } catch (IOException ioEx) {
                                emitter.completeWithError(ioEx);
                                handler.onEmitterError.accept(ioEx);
                            }
                            handler.onError.accept(error);
                        })
                        .flatMap(item -> handler.contentMapper.apply(item).map(Flowable::just).orElse(Flowable.empty()))
                        .doOnNext(content -> {
                            try {
                                emitter.send(content, MediaType.TEXT_EVENT_STREAM);
                            } catch (IOException e) {
                                emitter.completeWithError(e);
                                handler.onEmitterError.accept(e);
                                log.error("doOnNext 处理出错 uuid:{} error:{}", handler.uuid, e.getMessage());
                            }
                        })
                        .doOnTerminate(emitter::complete)
                        .subscribe();
            } catch (Exception e) {
                emitter.completeWithError(e);
                handler.onError.accept(e);
                log.error("flowable 处理出错 uuid:{} error:{}", handler.uuid, e.getMessage());
            }

            return emitter;
        }
    }
}
