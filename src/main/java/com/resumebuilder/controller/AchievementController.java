package com.resumebuilder.controller;

import com.resumebuilder.entity.Achievement;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.AchievementRepository;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementRepository achievementRepository;
    private final UserService userService;

    public AchievementController(AchievementRepository achievementRepository, UserService userService) {
        this.achievementRepository = achievementRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        User user = currentUser(session);
        model.addAttribute("achievementList", user.getAchievementList());
        model.addAttribute("achievement", new Achievement());
        return "achievements";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @ModelAttribute Achievement achievement) {
        User user = currentUser(session);
        achievement.setUser(user);
        achievementRepository.save(achievement);
        return "redirect:/achievements";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        achievementRepository.deleteById(id);
        return "redirect:/achievements";
    }

    private User currentUser(HttpSession session) {
        return userService.findById((Long) session.getAttribute("userId")).orElseThrow();
    }
}
