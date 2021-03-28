package dev.shoito.multitenancy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model) {
        final String host =
                ServletUriComponentsBuilder.fromCurrentContextPath().build().getHost().split("\\.")[0];
        System.out.println(host);
        model.addAttribute("tenant", host);
        model.addAttribute("message", "Hello");
        return "hello";
    }
}
