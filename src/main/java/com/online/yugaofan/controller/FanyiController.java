package com.online.yugaofan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/fanyi")
public class FanyiController {

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file) {
        return "upload success";
    }
}
