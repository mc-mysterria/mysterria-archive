package net.mysterria.archive.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import net.mysterria.archive.database.entity.ArchiveDeveloper;

@Controller
@RequestMapping("/archive/mvc")
public class DevelopersController {

    private static final List<ArchiveDeveloper> ARCHIVE_DEVELOPERS = List.of(
        new ArchiveDeveloper("ikeepcalm", "Bohdan Horokh", "Project Developer", "https://github.com/ikeepcalm"),
        new ArchiveDeveloper("djecka", "Yevhen Sviachenyi", "Project Developer", "https://github.com/Djecka-2019"),
        new ArchiveDeveloper("esfer", "Esfer Useinov", "Project Developer", "https://github.com/EsferUs7")
    );

    @GetMapping({"", "/"})
    public String list(Model model) {
        model.addAttribute("developers", ARCHIVE_DEVELOPERS);
        model.addAttribute("count", ARCHIVE_DEVELOPERS.size());
        return "list";
    }

    @GetMapping("/developers/{nickname}")
    public String developerProfile(@PathVariable String nickname, Model model) {
        Optional<ArchiveDeveloper> dev = ARCHIVE_DEVELOPERS.stream()
                .filter(d -> d.nickname().equalsIgnoreCase(nickname))
                .findFirst();

        if (dev.isEmpty()) {
            return "redirect:/archive";
        }

        model.addAttribute("developer", dev.get());
        return "developer";
    }
}
