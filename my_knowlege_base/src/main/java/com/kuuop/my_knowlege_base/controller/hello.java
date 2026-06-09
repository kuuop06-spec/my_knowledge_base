package com.kuuop.my_knowlege_base.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class hello {
    @GetMapping("/hello")
    public String hello(){
        return "fuck you bitch";
    }
}
