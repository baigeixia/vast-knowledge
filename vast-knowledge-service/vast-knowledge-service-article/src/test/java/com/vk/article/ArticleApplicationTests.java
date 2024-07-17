package com.vk.article;

import com.vk.db.utils.MongoDBHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleApplicationTests  {

    @Autowired
    private MongoDBHelper mongoDBHelper;

    @Test
    void  contextLoads(){

        System.out.println(111);
    }
}
