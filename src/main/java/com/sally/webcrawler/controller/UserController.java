package com.sally.webcrawler.controller;

import com.sally.webcrawler.entity.Keyword;
import com.sally.webcrawler.entity.MyUser;
import com.sally.webcrawler.repository.KeywordRepository;
import com.sally.webcrawler.repository.MyUserRepository;
import com.sally.webcrawler.service.StatisticsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private final MyUserRepository userRepository;
    private final KeywordRepository keywordRepository;
    private final PasswordEncoder passwordEncoder;
    private final StatisticsService statisticsService;

    public UserController(MyUserRepository userRepository, KeywordRepository keywordRepository, PasswordEncoder passwordEncoder, StatisticsService statisticsService) {
        this.userRepository = userRepository;
        this.keywordRepository = keywordRepository;
        this.passwordEncoder = passwordEncoder;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(MyUser user) {
        user.setRoles("User");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/")
    public String homepage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<Object[]> top3KeywordsWithUserCount = keywordRepository.findTop3KeywordsWithUserCount();
        List<Object[]> top3UsersWithKeywordCount = userRepository.findTop3UsersWithKeywordCount();

        boolean isLoggedIn = !authentication.getName().equals("anonymousUser");

        model.addAttribute("topkeywords", top3KeywordsWithUserCount);
        model.addAttribute("topusers", top3UsersWithKeywordCount);
        model.addAttribute("isLoggedIn", isLoggedIn);

        if(!isLoggedIn) {
            return "index";
        }

        MyUser currentUser = userRepository.findByEmail(authentication.getName()).get();

        List<Keyword> currentUserKeywords = currentUser.getKeywords();

        model.addAttribute("keywords", currentUserKeywords);

        Map<String, Long> todaysCounts = new HashMap<>();
        Map<String, Long> yesterdaysCounts = new HashMap<>();
        Map<String, Long> tdbyesterdaysCounts = new HashMap<>();

        currentUserKeywords.forEach(keyword -> {
            todaysCounts.put(keyword.getName(), statisticsService.getTodaysNewsCountForKeyword(keyword.getName()));
            yesterdaysCounts.put(keyword.getName(), statisticsService.getYesterdaysNewsCountForKeyword(keyword.getName()));
            tdbyesterdaysCounts.put(keyword.getName(), statisticsService.getTheDayBeforeYesterdaysNewsCountForKeyword(keyword.getName()));
        });

        model.addAttribute("todaysCounts", todaysCounts);
        model.addAttribute("yesterdaysCounts", yesterdaysCounts);
        model.addAttribute("tdbyesterdaysCounts", tdbyesterdaysCounts);

        return "index";
    }
}
