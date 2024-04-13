package com.sally.webcrawler.controller;

import com.sally.webcrawler.entity.Keyword;
import com.sally.webcrawler.entity.MyUser;
import com.sally.webcrawler.entity.News;
import com.sally.webcrawler.repository.KeywordRepository;
import com.sally.webcrawler.repository.MyUserRepository;
import com.sally.webcrawler.repository.NewsRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class KeywordController {

    private final MyUserRepository userRepository;
    private final KeywordRepository keywordRepository;
    private final NewsRepository newsRepository;

    public KeywordController(MyUserRepository userRepository, KeywordRepository keywordRepository, NewsRepository newsRepository) {
        this.userRepository = userRepository;
        this.keywordRepository = keywordRepository;
        this.newsRepository = newsRepository;
    }


    @GetMapping("/user/keywords")
    public String listUserKeywords (Model model , @RequestParam (value = "", required = false) String deleted) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, List<News>> newsMap = new HashMap<String, List<News>>();

        MyUser currentUser = userRepository.findByEmail(authentication.getName()).get();

        List<Keyword> currentUserKeywords = currentUser.getKeywords();

        for(Keyword keyword : currentUserKeywords) {
            newsMap.put(keyword.getName(), newsRepository.findByTitleContainingIgnoreCase(keyword.getName()));
        }

        model.addAttribute("keywords", currentUserKeywords);
        model.addAttribute("news", newsMap);
        if (deleted != null) {
            model.addAttribute("deleted", deleted.equals("success")
                    ? "Keyword successfully deleted!"
                    : "Failed to delete the keyword");
        }

        return "userKeywords";
    }

    @PostMapping("/user/addKeyword")
    public String addNewKeyword(String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MyUser currentUser = userRepository.findByEmail(authentication.getName()).get();

        Keyword keywordToAdd;

        Optional<Keyword> keywordFromDB = keywordRepository.findByName(name);

        if(keywordFromDB.isPresent()) {
            keywordToAdd = keywordFromDB.get();
        } else {
            Keyword newKeyword = new Keyword();
            newKeyword.setName(name);
            keywordToAdd = keywordRepository.save(newKeyword);
        }

        currentUser.getKeywords().add(keywordToAdd);

        userRepository.save(currentUser);

        return "redirect:/user/keywords";
    }
    @PostMapping("/user/deleteKeyword/{id}")public String deleteUserKeyword(@PathVariable("id") Long id) {
        Optional<Keyword> keywordToDelete = keywordRepository.findById(id);
        if(keywordToDelete.isEmpty()) {
            return "redirect:/user/keywords?deleted=failure";
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUser currentUser = userRepository.findByEmail(authentication.getName()).get();
        List<Keyword> userKeywords = currentUser.getKeywords();
        int indexToDelete = userKeywords.indexOf(keywordToDelete.get());
        if (indexToDelete == -1) { // if the keyword is not in user's keywords list
            return "redirect:/user/keywords?deleted=failure";
        }
        userKeywords.remove(indexToDelete);
        currentUser.setKeywords(userKeywords);
        userRepository.save(currentUser);
        return "redirect:/user/keywords?deleted=success";
    }
}
