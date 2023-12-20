package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Handles requests to the root ("/") and "/index" endpoints,
     *                     rendering the index view.
     * @param model The Model object.
     * @return The name of the view to be rendered, typically "index".
     */
    @GetMapping(value = {"/", "/index"})
    public String index(final Model model) {
        model.addAttribute("title", "My Title");
        return "index";
    }
}
