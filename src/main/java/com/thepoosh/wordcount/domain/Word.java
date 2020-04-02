package com.thepoosh.wordcount.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@Document(collection = "words")
public class Word {

    @Getter
    @Setter
    @Id
    private String word;

    @Getter
    @Setter
    private long count;

    public Word() {
    }

    public Word(String word, long count) {
        this.word = word;
        this.count = count;
    }

    public void increment(long additional) {
        count += additional;
    }
}
