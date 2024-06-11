package com.boubyan.studentmanagement.controller;

import java.io.ByteArrayInputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.boubyan.studentmanagement.api.CoursesApi;
import com.boubyan.studentmanagement.model.CoursePage;
import com.boubyan.studentmanagement.security.SecurityHelper;
import com.boubyan.studentmanagement.service.course.CourseService;
import com.boubyan.studentmanagement.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CourseController implements CoursesApi {

    private final CourseService courseService;
    private final UserService userService;
    private final SecurityHelper securityHelper;

    @Override
    public ResponseEntity<CoursePage> getAllCourses(Integer page, Integer size) {
        return ResponseEntity.ok(courseService.getCourses(page, size));
    }

    @Override
    public ResponseEntity<Void> registerToCourse(Long courseId) {
        Long userId = securityHelper.getUserId();
        courseService.registerToCourse(courseId, userId);
        return ResponseEntity.noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> cancelCourse(Long courseId) {
        Long userId = securityHelper.getUserId();
        courseService.cancelCourse(courseId, userId);
        return ResponseEntity.noContent()
                .build();
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> exportRegisteredCourses() {
        Long userId = securityHelper.getUserId();
        ByteArrayInputStream pfd = courseService.exportRegisteredCourses(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=courses.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pfd));

    }
}
