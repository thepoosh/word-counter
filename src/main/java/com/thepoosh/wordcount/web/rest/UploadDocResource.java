package com.thepoosh.wordcount.web.rest;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thepoosh.wordcount.domain.ResourceType;
import com.thepoosh.wordcount.service.StringReaderService;

@RestController
@RequestMapping("/document")
public class UploadDocResource {

    private Logger logger = LoggerFactory.getLogger(UploadDocResource.class);

    @Autowired
    StringReaderService readerService;

    @GetMapping("/get")
    public ResponseEntity<String> getSomething() {
        return ResponseEntity.ok("fine");
    }

    @PostMapping("/uplaod")
    public ResponseEntity<String> uploadSimpleString(@RequestParam String resource,
                                                     @RequestParam ResourceType type) {
        try {
            readerService.readFromResource(resource, type);
        } catch (IOException e) {
            logger.error("Failed to read and parse resource " + resource + " of type " + type.getName(), e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
        return ResponseEntity.ok("coolio");
    }
}
