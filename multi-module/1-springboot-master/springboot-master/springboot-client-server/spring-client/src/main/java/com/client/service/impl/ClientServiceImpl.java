package com.client.service.impl;

import com.client.service.ClientService;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

    @Override
    public String selectName(String name) {
        return name;
    }

}
