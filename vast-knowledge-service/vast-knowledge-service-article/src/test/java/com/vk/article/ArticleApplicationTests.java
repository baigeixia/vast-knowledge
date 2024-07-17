package com.vk.article;

import com.vk.db.service.article.ArticleMgService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleApplicationTests  {

    @Autowired
    private ArticleMgService articleMgService;

    @Test
    void  contextLoads(){

        System.out.println(111);
    }
}
