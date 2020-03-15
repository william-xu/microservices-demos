package org.xwl.demo.boot.tp.druid.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
    @GetMapping("/")
    public String druidIndex() {
    	return "redirect:/druid/index";
    }
    
}
