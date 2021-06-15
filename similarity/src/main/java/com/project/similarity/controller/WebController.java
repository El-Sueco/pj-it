package com.project.similarity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController {

    @RequestMapping(path="/", method=RequestMethod.GET)
    public String about(){
        return "about";
    }

    @RequestMapping(path="/database", method=RequestMethod.GET)
    public String database(){
        return "database";
    }

    @RequestMapping(path="/check-more", method=RequestMethod.GET)
    public String checkMore(){
        return "check-more";
    }

    @RequestMapping(path="/manual", method=RequestMethod.GET)
    public String manual(){
        return "manual";
    }

}
