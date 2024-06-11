package com.boubyan.studentmanagement.service.pdf;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.boubyan.studentmanagement.entity.Course;

public interface PdfService {
    /**
     * generate pdf from the given courses list.
     *
     * @param courses list of courses to genrate the pdf
     * @return pdf as ByteArrayInputStream
     */
    ByteArrayInputStream generateCoursePdf(List<Course> courses);
}
