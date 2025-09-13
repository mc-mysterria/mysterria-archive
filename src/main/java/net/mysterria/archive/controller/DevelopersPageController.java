package net.mysterria.archive.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.mysterria.archive.viewmodel.Developer;

@Controller
@RequestMapping("/archive/mvc")
public class DevelopersPageController {

    private static final List<Developer> developers = List.of(
        new Developer("ikeepcalm", "Bohdan Horokh", "Project Developer", "https://github.com/ikeepcalm"),
        new Developer("djecka", "Yevhen Svyacheniy", "Project Developer", "https://github.com/Djecka1337"),
        new Developer("esfer", "Esfer Useinov", "Project Developer", "https://github.com/EsferUs7")
    );

    @GetMapping({"", "/"})
    public String list(Model model) {
        model.addAttribute("developers", developers);
        model.addAttribute("count", developers.size());
        return "list";
    }

    @GetMapping("/developers/{nickname}")
    public String developerProfile(@PathVariable String nickname, Model model) {
        Optional<Developer> dev = developers.stream()
                .filter(d -> d.nickname().equalsIgnoreCase(nickname))
                .findFirst();

        if (dev.isEmpty()) {
            return "redirect:/archive";
        }

        model.addAttribute("developer", dev.get());
        return "developer";
    }
}
