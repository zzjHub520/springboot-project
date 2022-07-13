package com.airport.service.impl;

import com.airport.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SyncServiceImpl implements SyncService {
    private static final Logger log = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Override
    @Async("syncExecutorPool")
    public void syncTest() {
        try {
            int i = new Random().nextInt(1000);
            log.info("syncServiceImpl.syncTest.随机睡眠 start :{}毫秒", i);
            Thread.sleep(i);
            log.info("syncServiceImpl.syncTest.随机睡眠   end :{}毫秒", i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
