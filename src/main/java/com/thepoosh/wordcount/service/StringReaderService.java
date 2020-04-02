package com.thepoosh.wordcount.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.common.collect.Iterables;
import com.thepoosh.wordcount.domain.ResourceType;
import com.thepoosh.wordcount.domain.Word;
import com.thepoosh.wordcount.repository.WordRepository;

@Service
public class StringReaderService {

    private final Logger logger = LoggerFactory.getLogger(StringReaderService.class);
    private final WordRepository wordRepository;
    private final OkHttpClient httpClient;

    public StringReaderService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
        httpClient = new OkHttpClient();
    }

    public void readFromResource(String payload, ResourceType type) throws IOException {
        switch (type) {
            case URL:
                Request request = new Request.Builder()
                    .url(payload)
                    .get()
                    .build();

                final Response res = httpClient.newCall(request).execute();
                if (!res.isSuccessful()) {
                    logger.info("bad request to {} with response status {}", payload, res.code());
                    return;
                }
                final ResponseBody body = res.body();
                if (body == null) {
                    logger.info("bad request with empty body to {}", payload);
                    return;
                }
                readFromStream(body.byteStream());
                return;
            case PATH:
                final InputStream inputStream = new FileInputStream(new File(payload));
                readFromStream(inputStream);
                break;
            case TEXT:
                storeWordCount(payload);
                break;
        }
    }

    /**
     * Takes an {@link InputStream} and reads it line by line in order to then store the word count
     * @param inputStream the data from file or net
     * @throws IOException if read fails
     */
    private void readFromStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            storeWordCount(line);
        }
    }

    /**
     * Reads a line of text and counts the number of appearances of each word and then stors it in Mongo
     * @param payload the line to read
     */
    private void storeWordCount(String payload) {
        Map<String, Long> wordCount = new HashMap<>();
        Arrays.stream(payload.split("\\s+"))
            .map(word -> word.toLowerCase().replaceAll("[^a-zA-Z0-9]", ""))
            .forEach(word -> wordCount.put(word, wordCount.getOrDefault(word, 0L) + 1));

        final Iterable<Word> stored = wordRepository.findAllById(wordCount.keySet());
        StreamSupport.stream(stored.spliterator(), true)
            .forEach(word -> word.increment(wordCount.remove(word.getWord())));
        final Collection<Word> newWordes = wordCount.entrySet().stream()
            .map(ent -> new Word(ent.getKey(), ent.getValue())).collect(Collectors.toList());

        wordRepository.saveAll(Iterables.concat(stored, newWordes));

    }
}
