package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = {"/", "/index"})
    public String index(final Model model) {
        model.addAttribute("title", "My Title");
        return "index";
    }
}