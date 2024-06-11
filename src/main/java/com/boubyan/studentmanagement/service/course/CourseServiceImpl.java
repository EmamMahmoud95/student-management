package com.boubyan.studentmanagement.service.course;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.boubyan.studentmanagement.entity.Course;
import com.boubyan.studentmanagement.entity.StudentCourse;
import com.boubyan.studentmanagement.entity.User;
import com.boubyan.studentmanagement.exceptions.customexceptions.BadRequestException;
import com.boubyan.studentmanagement.exceptions.customexceptions.BusinessException;
import com.boubyan.studentmanagement.mapper.CourseMapper;
import com.boubyan.studentmanagement.model.CoursePage;
import com.boubyan.studentmanagement.repository.CourseRepository;
import com.boubyan.studentmanagement.repository.StudentCourseRepository;
import com.boubyan.studentmanagement.service.pdf.PdfService;
import com.boubyan.studentmanagement.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;

    private final StudentCourseRepository studentCourseRepository;

    private final PdfService pdfService;

    @Override
    @Cacheable("courses")
    public CoursePage getCourses(Integer page, Integer size) {
        log.debug("start get All courses ...");
        Page<Course> courses = courseRepository.findAll(PageRequest.of(page, size));
        if (courses.getContent()
                .isEmpty()) {
            log.debug("no courses found");
            throw new BusinessException("no courses found", HttpStatus.NOT_FOUND);
        } else {
            List<com.boubyan.studentmanagement.model.Course> coursesResponse = courseMapper.mapCourseEntityToCourseResponse(courses.getContent());
            CoursePage coursePage = new CoursePage();
            coursePage.setCourses(coursesResponse);
            coursePage.setTotalPages(courses.getTotalPages());
            coursePage.setTotalElements(courses.getTotalElements());
            return coursePage;
        }

    }

    @Override
    @Cacheable("course")
    public Course getCoursesById(Long courseId) {
        log.debug("start get course by id...");
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new BadRequestException("no course found for the given id"));
    }

    @Override
    @CacheEvict(value = "student_courses", allEntries = true)
    public void registerToCourse(Long courseId, Long userId) {
        log.debug("start register to course...");

        User student = userService.getStudentById(userId);
        Course course = getCoursesById(courseId);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourse(course);
        studentCourse.setUser(student);

        try {
            studentCourseRepository.save(studentCourse);
            log.debug("student registered to this course");
        } catch (DataIntegrityViolationException exception) {

            if (exception.getCause() instanceof ConstraintViolationException) {
                log.debug("course is already registered for this student");
                throw new BadRequestException("course is already registered for this student");
            } else {
                log.error(exception.getMessage(), exception);
                throw new InternalError("database Error");
            }
        }
    }

    @Override
    @CacheEvict(value = "student_courses", allEntries = true)
    public void cancelCourse(Long courseId, Long userId) {
        log.debug("start course cancellation ...");

        userService.getStudentById(userId);
        getCoursesById(courseId);
        long count = studentCourseRepository.deleteByStudentIdAndCourseId(userId, courseId);
        if (count == 0) {
            log.debug("no registration found for this course");
            throw new BadRequestException("no registration found for this course");
        }
        log.debug("course cancelled successfully.");
    }

    @Override
    public ByteArrayInputStream exportRegisteredCourses(Long userId) {
        log.debug("start export registered courses ...");
        List<Course> courses = studentCourseRepository.findByUserId(userId)
                .stream()
                .map(StudentCourse::getCourse)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(courses)) {
            throw new BadRequestException("no registered courses found for this Student");
        }
        ByteArrayInputStream byteArrayInputStream = pdfService.generateCoursePdf(courses);
        log.debug("registered courses exported successfully.");
        return byteArrayInputStream;
    }

}
