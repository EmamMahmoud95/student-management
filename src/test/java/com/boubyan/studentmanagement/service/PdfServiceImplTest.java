package com.boubyan.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.boubyan.studentmanagement.entity.Course;
import com.boubyan.studentmanagement.service.pdf.PdfServiceImpl;

public class PdfServiceImplTest {
    private PdfServiceImpl pdfService;

    @BeforeEach
    public void setUp() {
        pdfService = new PdfServiceImpl();
    }

    @Test
    public void testGenerateCoursePdf_Success() throws IOException {
        List<Course> courses = new ArrayList<>();
        Course course = new Course();
        course.setName("Java 101");
        course.setDescription("Introduction to Java");
        course.setInstructorName("John Doe");
        course.setDuration(10);
        course.setDate(LocalDate.now());
        courses.add(course);

        ByteArrayInputStream pdfStream = pdfService.generateCoursePdf(courses);

        assertNotNull(pdfStream);
    }

    @Test
    public void testGenerateCoursePdfEmptyCourses() {
        List<Course> courses = new ArrayList<>();
        ByteArrayInputStream pdfStream = pdfService.generateCoursePdf(courses);
        assertNotNull(pdfStream);

    }

    @Test
    public void testGenerateCoursePdf_NullCourses() {
        ByteArrayInputStream pdfStream = pdfService.generateCoursePdf(null);
        assertNotNull(pdfStream);
    }
}
