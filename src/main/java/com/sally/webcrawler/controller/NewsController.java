package com.sally.webcrawler.controller;

import com.sally.webcrawler.entity.News;
import com.sally.webcrawler.repository.NewsRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class NewsController {

    private static final String NOVINITE_BG_URL = "https://novinite.bg";
    private static final String DNEVNIK_BG_URL = "https://www.dnevnik.bg/";
    private static final String VESTI_BG_URL = "https://www.vesti.bg/";

    private final NewsRepository newsRepository;

    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping("/download-all-news")
    public String fetchAllNews(Model model) {
        List<News> noviniteBgNews = extractNoviniteBgNews();
        List<News> dnevnikBgNews = extractDnevnikBgNews();
        List<News> vestiBgNews = extractVestiBgNews();

        newsRepository.saveAll(noviniteBgNews);
        newsRepository.saveAll(dnevnikBgNews);
        newsRepository.saveAll(vestiBgNews);

        model.addAttribute("novinitebgnewscount", noviniteBgNews.size());
        model.addAttribute("dnevnikbgnewscount", dnevnikBgNews.size());
        model.addAttribute("vestibgnewscount", vestiBgNews.size());

        return "downloadnews";
    }

    public static List<News> extractNoviniteBgNews() {
        List<News> news = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(NOVINITE_BG_URL).get();
            Elements articles = doc.select("article");
            articles.forEach(newsCard -> {
                String title = newsCard.select("h2 a").text();
                String url = newsCard.select("h2 a").attr("href");
                News newsToAdd = new News();
                newsToAdd.setTitle(title);
                newsToAdd.setUrl(url);
                newsToAdd.setDateTimeAdded(LocalDateTime.now());
                news.add(newsToAdd);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }
    public static List<News> extractDnevnikBgNews() {
        List<News> news = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(DNEVNIK_BG_URL).get();
            Elements articles = doc.select("article");
            articles.forEach(newsCard -> {
                String title = newsCard.select("h3 a").text();
                String url = newsCard.select("h3 a").attr("href");
                News newsToAdd = new News();
                newsToAdd.setTitle(title);
                newsToAdd.setUrl(url);
                newsToAdd.setDateTimeAdded(LocalDateTime.now());
                news.add(newsToAdd);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }
    public static List<News> extractVestiBgNews() {
        List<News> news = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(VESTI_BG_URL).get();
            Elements articles = doc.select(".card-item");
            articles.forEach(newsCard -> {
                String title = newsCard.select("h2 a").text();
                String url = newsCard.select("h2 a").attr("href");
                News newsToAdd = new News();
                newsToAdd.setTitle(title);
                newsToAdd.setUrl(url);
                newsToAdd.setDateTimeAdded(LocalDateTime.now());
                news.add(newsToAdd);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return news;
    }
}
