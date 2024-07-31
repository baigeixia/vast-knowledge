package com.vk.comment;

import com.vk.comment.domain.dto.CommentSaveDto;
import com.vk.comment.service.ApCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class TestCommentApplication {

    @Autowired
    private ApCommentService apCommentService;

    void  testComment(){
        CommentSaveDto saveDto = new CommentSaveDto();
        saveDto.setContent("");
        apCommentService.saveComment(saveDto);
    }

}