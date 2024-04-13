package com.sally.webcrawler.controller;

import com.sally.webcrawler.repository.KeywordRepository;
import com.sally.webcrawler.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WebcrawlerController {

    private final NewsRepository newsRepository;
    private final KeywordRepository keywordRepository;

    @Autowired
    public WebcrawlerController(NewsRepository newsRepository, KeywordRepository keywordRepository) {
        this.newsRepository = newsRepository;
        this.keywordRepository = keywordRepository;
    }
}
