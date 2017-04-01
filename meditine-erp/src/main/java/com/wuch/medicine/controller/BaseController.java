package com.wuch.medicine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wubo5 on 2017/1/20.
 */
@Controller
public class BaseController {
    @RequestMapping(value = "/login")
    public String loginPage(){
        return "login";
    }

    @RequestMapping(value = "/zct")
    public String zct(){
        return "zct";
    }

    @RequestMapping(value = "/lf")
    public String lf(){
        return "lf";
    }

}
