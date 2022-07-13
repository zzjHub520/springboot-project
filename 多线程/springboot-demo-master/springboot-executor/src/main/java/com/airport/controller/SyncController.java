package com.airport.controller;

import com.airport.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(SyncController.class);

    protected final SyncService syncService;

    @Autowired
    public SyncController(SyncService syncService) {
        this.syncService = syncService;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("syncController.run");
        try {
            for (int i = 0; i < 10; i++) {
                syncService.syncTest();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
