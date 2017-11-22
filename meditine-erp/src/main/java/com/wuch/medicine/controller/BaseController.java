package com.wuch.medicine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wubo5 on 2017/1/20.
 */
@Controller
@RequestMapping("/index")
public class BaseController {
    @RequestMapping(value = "/login")
    public String loginPage(){
        return "login";
    }
}
