package com.winkey.practice.palindservice.controller;

import com.winkey.practice.palindservice.dto.PageResult;
import com.winkey.practice.palindservice.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Venkatesh Rajendran
 */

@RestController
public class AppController {

    private AppService appService;

    @Autowired
    public AppController(AppService appService){
        this.appService = appService;
    }

    @GetMapping(value = "/list")
    public PageResult getPageResult(@RequestParam("page") Integer page, @RequestParam(name = "count",required = false) Integer count ){
        return appService.getPageResult(page, count);
    }

    @PutMapping(value = "/persist")
    public PageResult putData(@RequestBody Map<String, String> data ){
        return appService.putData(data.get("value"));
    }

    @GetMapping(value = "/auto")
    public PageResult getAutoCompleteResult(@RequestParam("data") String data){
        return appService.getAutoCompleteResult(data);
    }

    @GetMapping(value = "/auto")
    public PageResult streamResult(@RequestParam("data") String data) {
        return appService.getAutoCompleteResult(data);
    }

}
