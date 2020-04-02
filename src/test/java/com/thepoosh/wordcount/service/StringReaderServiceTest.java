package com.thepoosh.wordcount.service;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.thepoosh.wordcount.domain.ResourceType;
import com.thepoosh.wordcount.domain.Word;
import com.thepoosh.wordcount.repository.WordRepository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class StringReaderServiceTest {

    @Autowired
    WordRepository wordRepository;
    @Autowired
    StringReaderService readerService;

    @BeforeEach
    public void setup() {
        wordRepository.deleteAll();
    }

    @Test
    void readFromResource() throws IOException {
        final String path = "/mnt/aa3d1167-33cf-4be8-86fe-348ee6ad6516/development/lemonade/jhipster/src/test/resources/words/words.txt";
        readerService.readFromResource(path, ResourceType.PATH);
        final List<Word> all = wordRepository.findAll();
        assertNotEquals(0, all.size());
    }

    @Test
    public void readFromAPI() throws IOException {
        final String uri = "https://baconipsum.com/api/?type=meat-and-filler&paras=5&format=text";
        readerService.readFromResource(uri, ResourceType.URL);
        final List<Word> all = wordRepository.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    public void readFromString() throws IOException {
        final String payload = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        readerService.readFromResource(payload, ResourceType.TEXT);
        assertNotEquals(0, wordRepository.count());

    }

}
