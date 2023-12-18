package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PaymentController {

    @GetMapping(value = {"/", "/index"})
    public String index(final Model model) {
        model.addAttribute("title", "My Title");
        return "index";
    }

    @GetMapping(value = "/pay")
    public String pay(final Model model) {
        model.addAttribute("title", "My Title");
        return "index";
    }

    @PostMapping(value = "/pay-return-url")
    public String paymentNotification(final Model model) {
        model.addAttribute("title", "My Title");
        return "index";
    }

}
