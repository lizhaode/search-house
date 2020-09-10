package com.lizhao.searchhouse.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lizhao.searchhouse.entity.beike.BaseResponse;
import com.lizhao.searchhouse.entity.beike.HouseInfo;
import com.lizhao.searchhouse.http.BeiKeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BeikeHouse {

    @Autowired
    private BeiKeFeignClient client;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private ObjectMapper om = new ObjectMapper();


    @Scheduled(fixedDelay = 86400000L)
    public void start() throws JsonProcessingException {
        int stop;
        int startPage = 1;
        BaseResponse response;
        do {
            response = this.client.houseWithPage(startPage);
            startPage += 1;
            stop = response.getData().getNoMoreData();
            for (HouseInfo info : response.getData().getList()) {
                redisTemplate.convertAndSend("bei_ke", om.writeValueAsString(info));
            }
        } while (stop != 1);
    }
}
