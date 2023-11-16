package com.workintech.minitwitter.controller;

import com.workintech.minitwitter.dto.LikeRequest;
import com.workintech.minitwitter.dto.ReTweetRequest;
import com.workintech.minitwitter.entity.Member;
import com.workintech.minitwitter.entity.Tweet;
import com.workintech.minitwitter.exceptions.TweetException;
import com.workintech.minitwitter.service.MemberService;
import com.workintech.minitwitter.service.TweetServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/tweet")
public class TweetController {

    private TweetServiceImp tweetServiceImp;
    private MemberService memberService;

    @Autowired
    public TweetController(TweetServiceImp tweetServiceImp, MemberService memberService) {
        this.tweetServiceImp = tweetServiceImp;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public List<Tweet> get(){
        return tweetServiceImp.findAll();
    }

    @GetMapping("/{id}")
    public Tweet getById(@PathVariable int id){
        return tweetServiceImp.findById(id);
    }

    @PostMapping("/")
    public Tweet save(@RequestBody Tweet tweet){
        Tweet newTweet = tweet;
        newTweet.setCreatedAt(LocalDateTime.now());
        return tweetServiceImp.save(newTweet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        tweetServiceImp.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public Tweet edit(@RequestBody Tweet tweet, @PathVariable int id){
        Tweet existingTweet = tweetServiceImp.findById(id);
        existingTweet.setContent(tweet.getContent());
         tweetServiceImp.save(existingTweet);
         return existingTweet;
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<?> like(@PathVariable int id, @RequestBody LikeRequest likeRequest){
        try {
            Set<Member> likes = tweetServiceImp.saveLikes(likeRequest.memberId(), id);
            return ResponseEntity.ok(likes);
        } catch (TweetException tweetException) {
            return new ResponseEntity<>(tweetException.getMessage(), tweetException.getCode());
        }
    }

    @DeleteMapping("/like/{id}")
    public ResponseEntity<?> dislike(@PathVariable int id, @RequestBody LikeRequest likeRequest){
        try {
            Set<Member> likes = tweetServiceImp.deleteLikes(likeRequest.memberId(), id);
            return ResponseEntity.ok(likes);
        } catch (TweetException tweetException) {
            return new ResponseEntity<>(tweetException.getMessage(), tweetException.getCode());
        }
    }

    @GetMapping("/like/{id}")
    public int likeCount(@PathVariable int id){
        int likeCount = tweetServiceImp.likeCount(id);
        return likeCount;
    }

    @PostMapping("/rt/{id}")
    public ResponseEntity<?> retweet(@PathVariable int id, @RequestBody ReTweetRequest reTweetRequest){
        try {
            Set<Member> retweets = tweetServiceImp.saveRetweets(reTweetRequest.memberId(), id);
            return ResponseEntity.ok(retweets);
        } catch (TweetException tweetException) {
            return new ResponseEntity<>(tweetException.getMessage(), tweetException.getCode());
        }
    }

    @DeleteMapping("/rt/{id}")
    public ResponseEntity<?> deleteRetweet(@PathVariable int id, @RequestBody ReTweetRequest reTweetRequest){
        try {
            Set<Member> retweets = tweetServiceImp.deleteRetweets(reTweetRequest.memberId(), id);
            return ResponseEntity.ok(retweets);
        } catch (TweetException tweetException) {
            return new ResponseEntity<>(tweetException.getMessage(), tweetException.getCode());
        }
    }

    @GetMapping("/rt/{id}")
    public int RTCount(@PathVariable int id){
        int RTCount = tweetServiceImp.reTweetCount(id);
        return RTCount;
    }
}

