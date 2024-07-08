package com.boruomi.business.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {
    @PostMapping("/t1")
    public void getFileContent(@RequestBody Map map){
        System.out.println(map);
        System.out.println(map);
    }
}
