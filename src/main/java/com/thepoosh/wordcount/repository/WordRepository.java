package com.thepoosh.wordcount.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.thepoosh.wordcount.domain.Word;

@Repository
public interface WordRepository extends MongoRepository<Word, String> {
}
