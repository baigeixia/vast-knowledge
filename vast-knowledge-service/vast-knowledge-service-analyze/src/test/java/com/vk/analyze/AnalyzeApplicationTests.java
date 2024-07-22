package com.vk.analyze;

import com.vk.analyze.domain.AdChannel;
import com.vk.common.core.domain.R;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnalyzeApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void  testGetAnalyze(){
        Long channelId = 1L;
        // String url = "/channel/getOneInfo/{channelId}";
        String url = "/channel/getChannelList";

        // 构建 URL，并发送 GET 请求
        ResponseEntity<R> response = restTemplate.getForEntity(url, R.class);
        R body = response.getBody();
        Object data = body.getData();
        System.out.println(data);
    }
}
