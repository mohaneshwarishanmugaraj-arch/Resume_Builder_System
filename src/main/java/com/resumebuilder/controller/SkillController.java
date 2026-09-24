package com.resumebuilder.controller;

import com.resumebuilder.entity.Skill;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.SkillRepository;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/skills")
public class SkillController {

    private final SkillRepository skillRepository;
    private final UserService userService;

    public SkillController(SkillRepository skillRepository, UserService userService) {
        this.skillRepository = skillRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        User user = currentUser(session);
        model.addAttribute("skillList", user.getSkillList());
        model.addAttribute("skill", new Skill());
        return "skills";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @ModelAttribute Skill skill) {
        User user = currentUser(session);
        skill.setUser(user);
        skillRepository.save(skill);
        return "redirect:/skills";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        skillRepository.deleteById(id);
        return "redirect:/skills";
    }

    private User currentUser(HttpSession session) {
        return userService.findById((Long) session.getAttribute("userId")).orElseThrow();
    }
}
