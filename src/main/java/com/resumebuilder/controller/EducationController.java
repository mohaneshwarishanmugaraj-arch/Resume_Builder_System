package com.resumebuilder.controller;

import com.resumebuilder.entity.Education;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.EducationRepository;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/education")
public class EducationController {

    private final EducationRepository educationRepository;
    private final UserService userService;

    public EducationController(EducationRepository educationRepository, UserService userService) {
        this.educationRepository = educationRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        User user = currentUser(session);
        model.addAttribute("educationList", user.getEducationList());
        model.addAttribute("education", new Education());
        return "education";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @ModelAttribute Education education) {
        User user = currentUser(session);
        education.setUser(user);
        educationRepository.save(education);
        return "redirect:/education";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        educationRepository.deleteById(id);
        return "redirect:/education";
    }

    private User currentUser(HttpSession session) {
        return userService.findById((Long) session.getAttribute("userId")).orElseThrow();
    }
}
