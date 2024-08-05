package com.vk.comment;

import com.vk.comment.domain.dto.CommentSaveDto;
import com.vk.comment.service.ApCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;


@SpringBootTest
public class TestCommentApplication {

    @Autowired
    private ApCommentService apCommentService;

    @Test
    void  testComment(){
        long i=1722583380009L;
        long time = new Date().getTime();
        System.out.println(time-i);
        // CommentSaveDto saveDto = new CommentSaveDto();
        // saveDto.setContent("");
        // apCommentService.saveComment(saveDto);
    }

}