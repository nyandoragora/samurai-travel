package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.Faq;
import com.example.samuraitravel.service.FaqService;

@Controller
@RequestMapping("/faqs")
public class FaqController {

    private final FaqService faqService;

    public FaqController(FaqService faqService) {
        this.faqService = faqService;
    }

    @GetMapping
    public String index(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Page<Faq> faqs;

        // ページネーション：1ページあたり5件
        PageRequest pageable = PageRequest.of(page, 5);

        if (keyword != null && !keyword.isEmpty()) {
            faqs = faqService.findAllFaqs("%" + keyword + "%", pageable);
        } else {
            faqs = faqService.getAllFaqs(pageable);
        }

        model.addAttribute("faqs", faqs);
        model.addAttribute("keyword", keyword);

        return "user/faq";
    }
}
