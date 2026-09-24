package com.resumebuilder.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.resumebuilder.entity.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

/**
 * Builds a formatted resume PDF from a User's profile data.
 * "styleKey" (from ResumeTemplate) selects the accent color / layout,
 * giving the effect of multiple selectable templates.
 */
@Service
public class PdfGenerationService {

    public byte[] generateResumePdf(User user, String styleKey) throws DocumentException {
        Color accent = "modern".equalsIgnoreCase(styleKey) ? new Color(30, 90, 160) : new Color(40, 40, 40);

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font nameFont = new Font(Font.HELVETICA, 22, Font.BOLD, accent);
        Font sectionFont = new Font(Font.HELVETICA, 13, Font.BOLD, accent);
        Font bodyFont = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY);
        Font smallFont = new Font(Font.HELVETICA, 9, Font.ITALIC, Color.GRAY);

        // Header
        Paragraph name = new Paragraph(user.getFullName(), nameFont);
        name.setAlignment(Element.ALIGN_CENTER);
        document.add(name);

        StringBuilder contact = new StringBuilder(user.getEmail());
        if (user.getPhone() != null && !user.getPhone().isBlank()) contact.append("  |  ").append(user.getPhone());
        if (user.getAddress() != null && !user.getAddress().isBlank()) contact.append("  |  ").append(user.getAddress());
        Paragraph contactPara = new Paragraph(contact.toString(), smallFont);
        contactPara.setAlignment(Element.ALIGN_CENTER);
        contactPara.setSpacingAfter(12f);
        document.add(contactPara);

        addDivider(document, accent);

        if (user.getProfileSummary() != null && !user.getProfileSummary().isBlank()) {
            addSectionTitle(document, "Profile Summary", sectionFont);
            document.add(new Paragraph(user.getProfileSummary(), bodyFont));
        }

        if (!user.getEducationList().isEmpty()) {
            addSectionTitle(document, "Education", sectionFont);
            for (Education e : user.getEducationList()) {
                document.add(new Paragraph(e.getDegree() + " - " + e.getInstitution(), bodyFont));
                document.add(new Paragraph(nullSafe(e.getBoardOrUniversity()) + "   " +
                        nullSafe(e.getYearOfPassing()) + "   " + nullSafe(e.getPercentageOrCgpa()), smallFont));
                document.add(Chunk.NEWLINE);
            }
        }

        if (!user.getExperienceList().isEmpty()) {
            addSectionTitle(document, "Experience", sectionFont);
            for (Experience ex : user.getExperienceList()) {
                document.add(new Paragraph(ex.getJobRole() + " - " + ex.getCompanyName(), bodyFont));
                document.add(new Paragraph(nullSafe(ex.getStartDate()) + " to " + nullSafe(ex.getEndDate()), smallFont));
                if (ex.getDescription() != null) document.add(new Paragraph(ex.getDescription(), bodyFont));
                document.add(Chunk.NEWLINE);
            }
        }

        if (!user.getSkillList().isEmpty()) {
            addSectionTitle(document, "Skills", sectionFont);
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            for (Skill s : user.getSkillList()) {
                table.addCell(cellNoBorder(s.getSkillName(), bodyFont));
                table.addCell(cellNoBorder(nullSafe(s.getProficiencyLevel()), smallFont));
            }
            document.add(table);
            document.add(Chunk.NEWLINE);
        }

        if (!user.getProjectList().isEmpty()) {
            addSectionTitle(document, "Projects", sectionFont);
            for (Project p : user.getProjectList()) {
                document.add(new Paragraph(p.getTitle(), bodyFont));
                if (p.getTechnologiesUsed() != null)
                    document.add(new Paragraph("Technologies: " + p.getTechnologiesUsed(), smallFont));
                if (p.getDescription() != null) document.add(new Paragraph(p.getDescription(), bodyFont));
                document.add(Chunk.NEWLINE);
            }
        }

        if (!user.getCertificationList().isEmpty()) {
            addSectionTitle(document, "Certifications", sectionFont);
            for (Certification c : user.getCertificationList()) {
                document.add(new Paragraph(c.getCertificationName() + " - " + nullSafe(c.getIssuingOrganization())
                        + " (" + nullSafe(c.getIssueDate()) + ")", bodyFont));
            }
            document.add(Chunk.NEWLINE);
        }

        if (!user.getAchievementList().isEmpty()) {
            addSectionTitle(document, "Achievements", sectionFont);
            for (Achievement a : user.getAchievementList()) {
                document.add(new Paragraph("- " + a.getTitle() +
                        (a.getDateAchieved() != null ? " (" + a.getDateAchieved() + ")" : ""), bodyFont));
            }
        }

        document.close();
        return out.toByteArray();
    }

    private void addSectionTitle(Document document, String title, Font font) throws DocumentException {
        Paragraph p = new Paragraph(title, font);
        p.setSpacingBefore(8f);
        p.setSpacingAfter(4f);
        document.add(p);
    }

    private void addDivider(Document document, Color color) throws DocumentException {
        PdfPTable line = new PdfPTable(1);
        line.setWidthPercentage(100);
        line.getDefaultCell().setBorder(Rectangle.BOTTOM);
        line.getDefaultCell().setBorderColor(color);
        line.addCell(cellNoBorder(" ", new Font(Font.HELVETICA, 2)));
        document.add(line);
    }

    private PdfPTable table(int cols) {
        return new PdfPTable(cols);
    }

    private com.lowagie.text.pdf.PdfPCell cellNoBorder(String text, Font font) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(3f);
        return cell;
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }
}
