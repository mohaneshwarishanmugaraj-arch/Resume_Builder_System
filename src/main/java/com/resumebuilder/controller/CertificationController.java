package com.resumebuilder.controller;

import com.resumebuilder.entity.Certification;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.CertificationRepository;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/certifications")
public class CertificationController {

    private final CertificationRepository certificationRepository;
    private final UserService userService;

    public CertificationController(CertificationRepository certificationRepository, UserService userService) {
        this.certificationRepository = certificationRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        User user = currentUser(session);
        model.addAttribute("certificationList", user.getCertificationList());
        model.addAttribute("certification", new Certification());
        return "certifications";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @ModelAttribute Certification certification) {
        User user = currentUser(session);
        certification.setUser(user);
        certificationRepository.save(certification);
        return "redirect:/certifications";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        certificationRepository.deleteById(id);
        return "redirect:/certifications";
    }

    private User currentUser(HttpSession session) {
        return userService.findById((Long) session.getAttribute("userId")).orElseThrow();
    }
}
