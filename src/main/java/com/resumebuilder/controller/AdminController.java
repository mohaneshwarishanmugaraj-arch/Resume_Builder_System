package com.resumebuilder.controller;

import com.resumebuilder.entity.ResumeTemplate;
import com.resumebuilder.repository.ResumeGenerationLogRepository;
import com.resumebuilder.repository.ResumeTemplateRepository;
import com.resumebuilder.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ResumeTemplateRepository resumeTemplateRepository;
    private final UserRepository userRepository;
    private final ResumeGenerationLogRepository logRepository;

    public AdminController(ResumeTemplateRepository resumeTemplateRepository,
                            UserRepository userRepository,
                            ResumeGenerationLogRepository logRepository) {
        this.resumeTemplateRepository = resumeTemplateRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("totalTemplates", resumeTemplateRepository.count());
        model.addAttribute("totalResumesGenerated", logRepository.count());
        return "admin/dashboard";
    }

    @GetMapping("/templates")
    public String templates(Model model) {
        model.addAttribute("templates", resumeTemplateRepository.findAll());
        model.addAttribute("template", new ResumeTemplate());
        return "admin/templates";
    }

    @PostMapping("/templates/add")
    public String addTemplate(@ModelAttribute ResumeTemplate template) {
        resumeTemplateRepository.save(template);
        return "redirect:/admin/templates";
    }

    @PostMapping("/templates/toggle/{id}")
    public String toggleTemplate(@PathVariable Long id) {
        resumeTemplateRepository.findById(id).ifPresent(t -> {
            t.setActive(!t.isActive());
            resumeTemplateRepository.save(t);
        });
        return "redirect:/admin/templates";
    }

    @PostMapping("/templates/delete/{id}")
    public String deleteTemplate(@PathVariable Long id) {
        resumeTemplateRepository.deleteById(id);
        return "redirect:/admin/templates";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/users";
    }
}
