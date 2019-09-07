package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController{
    @RequestMapping(value="")
    public String index(){
        return "/static/home_page.html";
    }
}