package com.workintech.minitwitter.service;

import com.workintech.minitwitter.dao.MemberDao;
import com.workintech.minitwitter.dao.TweetDao;
import com.workintech.minitwitter.entity.Member;
import com.workintech.minitwitter.entity.Tweet;
import com.workintech.minitwitter.exceptions.TweetException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TweetServiceImp implements TweetService{
    private TweetDao tweetDao;
    private MemberDao memberDao;

    @Autowired
    public TweetServiceImp(TweetDao tweetDao, MemberDao memberDao) {
        this.tweetDao = tweetDao;
        this.memberDao = memberDao;
    }

    @Override
    public List<Tweet> findAll() {
        return tweetDao.findAll();
    }

    @Override
    public Tweet findById(int id) {
        Optional<Tweet> existingTweet = tweetDao.findById(id);
        if(existingTweet.isPresent()){
            return existingTweet.get();
        }
        throw new TweetException("This tweet doesn't exist: ", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public Tweet save(Tweet tweet) {
        return tweetDao.save(tweet);
    }

    @Override
    @Transactional
    public void delete(int id) {
        Optional<Tweet> existingTweet = tweetDao.findById(id);
        if(existingTweet.isPresent()){
            tweetDao.delete(existingTweet.get());
        } else {
            throw new TweetException("This tweet doesn't exist: ", HttpStatus.BAD_REQUEST);
        }
        }


    @Override
    @Transactional
    public Set<Member> saveLikes(int memberId, int id) {
        Optional<Member> existingMember = memberDao.findById(memberId);
        Optional<Tweet> existingTweet = tweetDao.findById(id);
        if (existingTweet.isPresent()) {
            Tweet tweet = existingTweet.get();
            for (Member liked : tweet.getLikedTweets()) {
                if (liked.getId() == existingMember.get().getId()) {
                    throw new TweetException("Already liked: ", HttpStatus.BAD_REQUEST);
                }
            }
            tweet.getLikedTweets().add(existingMember.get());
            tweet.setLikeCount(tweet.getLikeCount() + 1);
            tweetDao.save(tweet);
            return tweet.getLikedTweets();
        }
            throw new TweetException("This tweet doesn't exist: ", HttpStatus.BAD_REQUEST);

    }

    @Override
    public Set<Member> deleteLikes(int memberId, int id) {
        Optional<Tweet> existingTweet = tweetDao.findById(id);
        if (!existingTweet.isPresent()) {
            throw new TweetException("This tweet doesn't exist: ", HttpStatus.BAD_REQUEST);
        }

        Tweet tweet = existingTweet.get();
        Optional<Member> existingMember = memberDao.findById(memberId);
        if (!existingMember.isPresent()) {
            throw new TweetException("Member not found", HttpStatus.NOT_FOUND);
        }

        Member memberToRemove = existingMember.get();
        if (tweet.getLikedTweets().contains(memberToRemove)) {
            tweet.getLikedTweets().remove(memberToRemove);
            tweet.setLikeCount(tweet.getLikeCount() - 1);
            tweetDao.save(tweet);
        } else {
            throw new TweetException("Not liked yet: ", HttpStatus.BAD_REQUEST);
        }

        return tweet.getLikedTweets();
    }




    @Override
    public Set<Member> saveRetweets(int memberId, int id) {
        Optional<Member> existingMember = memberDao.findById(memberId);
        Optional<Tweet> existingTweet = tweetDao.findById(id);
        if (existingTweet.isPresent()) {
            Tweet tweet = existingTweet.get();
            for (Member retweeted : tweet.getRetweetedTweets()) {
                if (retweeted.getId() == existingMember.get().getId()) {
                    throw new TweetException("Already retweeted: ", HttpStatus.BAD_REQUEST);
                }
            }
            tweet.getRetweetedTweets().add(existingMember.get());
            tweet.setReTweetCount(tweet.getReTweetCount() + 1);
            tweetDao.save(tweet);
            return tweet.getRetweetedTweets();
        }
        throw new TweetException("This tweet doesn't exist: ", HttpStatus.BAD_REQUEST);

    }

    @Override
    @Transactional
    public Set<Member> deleteRetweets(int memberId, int id) {
        Optional<Member> existingMember = memberDao.findById(memberId);
        Optional<Tweet> existingTweet = tweetDao.findById(id);
        if (existingTweet.isPresent()) {
            Tweet tweet = existingTweet.get();
            Member memberToRemove = null;
            for (Member retweeted : tweet.getRetweetedTweets()) {
                if (retweeted.getId() == existingMember.get().getId()) {
                    memberToRemove = retweeted;
                    break;
                }
            }
            if (memberToRemove != null) {
                tweet.getRetweetedTweets().remove(memberToRemove);
                tweet.setReTweetCount(tweet.getReTweetCount() - 1);
                tweetDao.save(tweet);
            } else {
                throw new TweetException("Not retweeted yet: ", HttpStatus.BAD_REQUEST);
            }
            return tweet.getRetweetedTweets();
        }
        throw new TweetException("This tweet doesn't exist: ", HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public int likeCount(int id) {
        Optional<Tweet> tweet = tweetDao.findById(id);
        return tweet.get().getLikeCount();
    }

    @Override
    @Transactional
    public int reTweetCount(int id) {
        Optional<Tweet> tweet = tweetDao.findById(id);
        return tweet.get().getReTweetCount();
    }
}
