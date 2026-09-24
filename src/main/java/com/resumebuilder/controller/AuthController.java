package com.resumebuilder.controller;

import com.resumebuilder.entity.User;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (userService.emailExists(user.getEmail())) {
            result.rejectValue("email", "duplicate", "An account with this email already exists");
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.register(user);
        model.addAttribute("registered", true);
        return "login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                         HttpSession session, Model model) {
        Optional<User> authenticated = userService.authenticate(email, password);
        if (authenticated.isEmpty()) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
        User user = authenticated.get();
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFullName());
        session.setAttribute("role", user.getRole().name());
        return user.getRole().name().equals("ADMIN") ? "redirect:/admin/dashboard" : "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
