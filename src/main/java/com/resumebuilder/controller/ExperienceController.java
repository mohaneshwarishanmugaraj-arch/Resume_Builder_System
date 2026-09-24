package com.resumebuilder.controller;

import com.resumebuilder.entity.Experience;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.ExperienceRepository;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceRepository experienceRepository;
    private final UserService userService;

    public ExperienceController(ExperienceRepository experienceRepository, UserService userService) {
        this.experienceRepository = experienceRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        User user = currentUser(session);
        model.addAttribute("experienceList", user.getExperienceList());
        model.addAttribute("experience", new Experience());
        return "experience";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @ModelAttribute Experience experience) {
        User user = currentUser(session);
        experience.setUser(user);
        experienceRepository.save(experience);
        return "redirect:/experience";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        experienceRepository.deleteById(id);
        return "redirect:/experience";
    }

    private User currentUser(HttpSession session) {
        return userService.findById((Long) session.getAttribute("userId")).orElseThrow();
    }
}
