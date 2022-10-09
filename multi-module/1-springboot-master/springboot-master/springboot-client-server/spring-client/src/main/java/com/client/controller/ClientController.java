package com.client.controller;

import com.client.service.ClientService;
import com.server.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ServerService serverService;

    @GetMapping("/selectClientName")
    public String selectClientName(String name) {
        String result = clientService.selectName(name);
        return result;
    }

    @GetMapping("/selectServerName")
    public String selectServerName(String name) {
        String result = serverService.selectName(name);
        return result;
    }

}
