package com.resumebuilder.controller;

import com.resumebuilder.entity.ResumeGenerationLog;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.ResumeGenerationLogRepository;
import com.resumebuilder.repository.ResumeTemplateRepository;
import com.resumebuilder.service.PdfGenerationService;
import com.resumebuilder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class ResumeController {

    private final UserService userService;
    private final PdfGenerationService pdfGenerationService;
    private final ResumeTemplateRepository resumeTemplateRepository;
    private final ResumeGenerationLogRepository logRepository;

    public ResumeController(UserService userService, PdfGenerationService pdfGenerationService,
                             ResumeTemplateRepository resumeTemplateRepository,
                             ResumeGenerationLogRepository logRepository) {
        this.userService = userService;
        this.pdfGenerationService = pdfGenerationService;
        this.resumeTemplateRepository = resumeTemplateRepository;
        this.logRepository = logRepository;
    }

    @GetMapping("/resume/preview")
    public String preview(HttpSession session, Model model) {
        User user = userService.findById((Long) session.getAttribute("userId")).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("templates", resumeTemplateRepository.findByActiveTrue());
        return "resume-preview";
    }

    @GetMapping("/resume/download")
    public ResponseEntity<byte[]> download(HttpSession session,
                                            @RequestParam(defaultValue = "classic") String style) throws Exception {
        User user = userService.findById((Long) session.getAttribute("userId")).orElseThrow();
        byte[] pdf = pdfGenerationService.generateResumePdf(user, style);

        ResumeGenerationLog log = new ResumeGenerationLog();
        log.setUser(user);
        log.setTemplateUsed(style);
        log.setGeneratedAt(LocalDateTime.now());
        logRepository.save(log);

        String filename = user.getFullName().replaceAll("\\s+", "_") + "_Resume.pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
