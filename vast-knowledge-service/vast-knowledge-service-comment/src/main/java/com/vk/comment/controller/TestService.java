package com.vk.comment.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    
    @Async
    public void test(){
        System.out.println("================> " + Thread.currentThread());
    }
    
}
