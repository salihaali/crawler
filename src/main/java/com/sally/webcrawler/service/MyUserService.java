package com.sally.webcrawler.service;

import com.sally.webcrawler.entity.Keyword;
import com.sally.webcrawler.entity.MyUser;
import com.sally.webcrawler.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyUserService {

    @Autowired
    private MyUserRepository myUserRepository;

    public List<Keyword> getUserKeywords(Long userId) {
        MyUser user = myUserRepository.findById(userId).orElse(null);
        if (user != null) {
            return user.getKeywords();
        } else {
            return null;
        }
    }

    public List<MyUser> getAllUsers() {
        return myUserRepository.findAll();
    }
}
