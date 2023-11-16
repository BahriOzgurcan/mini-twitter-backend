package com.workintech.minitwitter.dao;

import com.workintech.minitwitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetDao extends JpaRepository<Tweet, Integer> {
}
