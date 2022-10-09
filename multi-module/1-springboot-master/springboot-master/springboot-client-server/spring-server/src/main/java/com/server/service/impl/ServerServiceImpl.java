package com.server.service.impl;

import com.server.service.ServerService;
import org.springframework.stereotype.Service;

@Service
public class ServerServiceImpl implements ServerService {

    @Override
    public String selectName(String name) {
        return name;
    }

}
