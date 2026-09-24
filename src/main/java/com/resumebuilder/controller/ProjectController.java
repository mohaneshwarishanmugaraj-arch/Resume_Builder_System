package com.resumebuilder.controller;

import com.resumebuilder.entity.Project;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.ProjectRepository;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectController(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        User user = currentUser(session);
        model.addAttribute("projectList", user.getProjectList());
        model.addAttribute("project", new Project());
        return "projects";
    }

    @PostMapping("/add")
    public String add(HttpSession session, @ModelAttribute Project project) {
        User user = currentUser(session);
        project.setUser(user);
        projectRepository.save(project);
        return "redirect:/projects";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        projectRepository.deleteById(id);
        return "redirect:/projects";
    }

    private User currentUser(HttpSession session) {
        return userService.findById((Long) session.getAttribute("userId")).orElseThrow();
    }
}
