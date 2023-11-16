package com.workintech.minitwitter.service;

import com.workintech.minitwitter.entity.Member;
import com.workintech.minitwitter.entity.Tweet;


import java.util.List;
import java.util.Set;

public interface TweetService {

    List<Tweet> findAll();
    Tweet findById(int id);
    Tweet save(Tweet tweet);
    void delete(int id);
    Set<Member> saveLikes (int memberId, int id);
    Set<Member> deleteLikes (int memberId, int id);
    Set<Member> saveRetweets (int memberId, int id);
    Set<Member> deleteRetweets(int memberId, int id);

    int likeCount(int id);
    int reTweetCount(int id);
}
