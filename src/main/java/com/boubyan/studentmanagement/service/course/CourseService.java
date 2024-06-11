package com.boubyan.studentmanagement.service.course;

import java.io.ByteArrayInputStream;

import com.boubyan.studentmanagement.entity.Course;
import com.boubyan.studentmanagement.model.CoursePage;

public interface CourseService {

    /**
     * get Page of the courses by page and size.
     *
     * @param page the page of the courses to be loaded
     * @param size the size page of the courses to be loaded
     * @return Page of the found courses
     */
    CoursePage getCourses(Integer page, Integer size);

    /**
     * get Course By id or throw if not found.
     *
     * @param courseId long Course id
     * @return Course
     */
    Course getCoursesById(Long courseId);

    /**
     * register the given user id to the given Course id.
     *
     * @param courseId course id to be registered
     * @param userId   the user id that will register to the course
     */
    void registerToCourse(Long courseId, Long userId);

    /**
     * cancel the given user id from the given Course id.
     *
     * @param courseId course id to be cancelled
     * @param userId   the user id that will cancel the course
     */
    void cancelCourse(Long courseId, Long userId);

    /**
     * export all the courses that is registered by the given user id  as pdf.
     *
     * @param userId the user id that will export registered courses
     * @return pdf as ByteArrayInputStream
     */
    ByteArrayInputStream exportRegisteredCourses(Long userId);
}
