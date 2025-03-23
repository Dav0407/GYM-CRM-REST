package com.epam.gym_crm.controller;

import com.epam.gym_crm.model.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {

    @GetMapping("/hello")
    public Message hello() {
        return new Message("Hello, World!");
    }

    @GetMapping("/hello/{name}")
    public Message helloWithName(@PathVariable String name) {
        return new Message("Hello, " + name + "!");
    }
}