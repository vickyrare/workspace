package io.codecrafts.controllers;

import io.codecrafts.services.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CopyRightController {

    @RequestMapping(value = "/copyright", method = RequestMethod.GET)
    @ResponseBody
    public String showCopyRight() {
        return "Copyright 2018 CodeCrafts";
    }
}