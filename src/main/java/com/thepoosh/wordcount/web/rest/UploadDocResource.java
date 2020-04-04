package com.thepoosh.wordcount.web.rest;

import io.github.jhipster.web.util.ResponseUtil;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.validator.routines.UrlValidator;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thepoosh.wordcount.domain.ResourceType;
import com.thepoosh.wordcount.domain.Word;
import com.thepoosh.wordcount.service.StringReaderService;

@RestController
@RequestMapping("/document")
public class UploadDocResource {

    private Logger logger = LoggerFactory.getLogger(UploadDocResource.class);

    @Autowired
    StringReaderService readerService;

    @GetMapping("/{word}")
    public ResponseEntity<Word> getWordStatistic(@PathVariable String word) {
        final Optional<Word> stats = readerService.getWord(word);
        return ResponseUtil.wrapOrNotFound(stats);
    }

    @PostMapping(value = "/upload", consumes = {"text/*"})
    public ResponseEntity<String> uploadSimpleString(@RequestBody String resource,
                                                     @RequestParam(required = false) ResourceType type) {
        if (type == null) {
            type = defineResourceType(resource);
        }

        try {
            readerService.readFromResource(resource, type);
        } catch (IOException e) {
            logger.error("Failed to read and parse resource " + resource + " of type " + type.getName(), e);
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
        return ResponseEntity.ok("coolio");
    }

    private ResourceType defineResourceType(String resource) {
        boolean isWeb = UrlValidator.getInstance().isValid(resource);
        if (isWeb) {
            return ResourceType.URL;
        }
        File file = new File(resource);
        if (file.exists()) {
            return ResourceType.PATH;
        }
        return ResourceType.TEXT;
    }


}
