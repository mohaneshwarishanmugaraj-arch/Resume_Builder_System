package com.resumebuilder.controller;

import com.resumebuilder.entity.User;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {

    private final UserService userService;

    public DashboardController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = userService.findById((Long) session.getAttribute("userId")).orElseThrow();
        model.addAttribute("user", user);
        return "dashboard";
    }

    @GetMapping("/profile")
    public String profileForm(HttpSession session, Model model) {
        User user = userService.findById((Long) session.getAttribute("userId")).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(HttpSession session, @ModelAttribute User formUser, Model model) {
        User user = userService.findById((Long) session.getAttribute("userId")).orElseThrow();
        user.setFullName(formUser.getFullName());
        user.setPhone(formUser.getPhone());
        user.setAddress(formUser.getAddress());
        user.setProfileSummary(formUser.getProfileSummary());
        userService.save(user);
        session.setAttribute("userName", user.getFullName());
        model.addAttribute("user", user);
        model.addAttribute("saved", true);
        return "profile";
    }
}
