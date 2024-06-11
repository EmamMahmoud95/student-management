package com.boubyan.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.boubyan.studentmanagement.entity.Course;
import com.boubyan.studentmanagement.entity.StudentCourse;
import com.boubyan.studentmanagement.entity.User;
import com.boubyan.studentmanagement.exceptions.customexceptions.BadRequestException;
import com.boubyan.studentmanagement.exceptions.customexceptions.BusinessException;
import com.boubyan.studentmanagement.mapper.CourseMapper;
import com.boubyan.studentmanagement.model.CoursePage;
import com.boubyan.studentmanagement.repository.CourseRepository;
import com.boubyan.studentmanagement.repository.StudentCourseRepository;
import com.boubyan.studentmanagement.service.course.CourseServiceImpl;
import com.boubyan.studentmanagement.service.pdf.PdfService;
import com.boubyan.studentmanagement.service.user.UserService;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private UserService userService;

    @Mock
    private StudentCourseRepository studentCourseRepository;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    public void testGetCourses() {

        Course courseEntity = getCourse();

        com.boubyan.studentmanagement.model.Course courseResponse = new com.boubyan.studentmanagement.model.Course();
        courseResponse.setId(1);
        courseResponse.setName("Test Course");
        courseResponse.setDescription("Test Description");
        courseResponse.setInstructorName("Test Instructor");
        courseResponse.setDuration(10);
        courseResponse.setDate(LocalDate.now());
        List<Course> courseEntities = Arrays.asList(courseEntity);
        Page<Course> coursePage = new PageImpl<>(courseEntities);
        when(courseRepository.findAll(PageRequest.of(0, 10))).thenReturn(coursePage);
        when(courseMapper.mapCourseEntityToCourseResponse(courseEntities)).thenReturn(Arrays.asList(courseResponse));

        CoursePage result = courseService.getCourses(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getCourses()
                .size());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void testGetCoursesNoCoursesFound() {
        Page<Course> coursePage = new PageImpl<>(Arrays.asList());
        when(courseRepository.findAll(PageRequest.of(0, 10))).thenReturn(coursePage);

        BusinessException exception = assertThrows(BusinessException.class, () -> courseService.getCourses(0, 10));
        assertEquals("no courses found", exception.getMessage());
    }

    @Test
    public void testGetCoursesById() {

        Course courseEntity = getCourse();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));

        Course result = courseService.getCoursesById(1L);

        assertNotNull(result);
        assertEquals(courseEntity.getId(), result.getId());
        assertEquals(courseEntity.getName(), result.getName());
        assertEquals(courseEntity.getDescription(), result.getDescription());
        assertEquals(courseEntity.getInstructorName(), result.getInstructorName());
        assertEquals(courseEntity.getDuration(), result.getDuration());
        assertEquals(courseEntity.getDate(), result.getDate());
    }

    @Test
    public void testGetCoursesByIdNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.getCoursesById(1L);
        });
        assertEquals("no course found for the given id", exception.getMessage());
    }

    @Test
    public void testRegisterToCourse() {

        Course courseEntity = getCourse();
        User student = getUser();

        when(userService.getStudentById(1L)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));

        assertDoesNotThrow(() -> courseService.registerToCourse(1L, 1L));
        verify(studentCourseRepository, times(1)).save(any());
    }

    @Test
    public void testRegisterToCourseAlreadyRegistered() {

        Course courseEntity = getCourse();

        User student = getUser();

        when(userService.getStudentById(1L)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        doThrow(new DataIntegrityViolationException("Duplicate entry", new ConstraintViolationException(null, null, "UK_123"))).when(
                        studentCourseRepository)
                .save(any(StudentCourse.class));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.registerToCourse(1L, 1L);
        });

        assertEquals("course is already registered for this student", exception.getMessage());
    }

    @Test
    public void testRegisterToCourseInternalError() {

        Course courseEntity = getCourse();
        User student = getUser();
        when(userService.getStudentById(1L)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        doThrow(new DataIntegrityViolationException("Some database error")).when(studentCourseRepository)
                .save(any(StudentCourse.class));

        InternalError exception = assertThrows(InternalError.class, () -> {
            courseService.registerToCourse(1L, 1L);
        });

        assertTrue(exception.getMessage()
                .contains("Some database error"));
    }

    @Test
    public void testRegisterToCourseCourseNotFound() {

        User student = getUser();
        when(userService.getStudentById(1L)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.registerToCourse(1L, 1L);
        });

        assertEquals("no course found for the given id", exception.getMessage());
    }

    @Test
    public void testRegisterToCourseUserNotFound() {
        when(userService.getStudentById(1L)).thenThrow(new BadRequestException("user not found"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.registerToCourse(1L, 1L);
        });

        assertEquals("user not found", exception.getMessage());
    }

    @Test
    public void testCancelCourse() {
        Course courseEntity = getCourse();
        User student = getUser();
        when(userService.getStudentById(1L)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        when(studentCourseRepository.deleteByStudentIdAndCourseId(1L, 1L)).thenReturn(1);

        assertDoesNotThrow(() -> courseService.cancelCourse(1L, 1L));
        verify(studentCourseRepository, times(1)).deleteByStudentIdAndCourseId(1L, 1L);
    }

    @Test
    public void testCancelCourseNoRegistrationFound() {
        Course courseEntity = getCourse();
        User student = getUser();
        when(userService.getStudentById(1L)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        when(studentCourseRepository.deleteByStudentIdAndCourseId(1L, 1L)).thenReturn(0);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.cancelCourse(1L, 1L);
        });

        assertEquals("no registration found for this course", exception.getMessage());
    }

    @Test
    public void testCancelCourseCourseNotFound() {
        User student = getUser();
        when(userService.getStudentById(1L)).thenReturn(student);
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.cancelCourse(1L, 1L);
        });

        assertEquals("no course found for the given id", exception.getMessage());
    }

    @Test
    public void testCancelCourseUserNotFound() {
        when(userService.getStudentById(1L)).thenThrow(new BadRequestException("user not found"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.cancelCourse(1L, 1L);
        });

        assertEquals("user not found", exception.getMessage());
    }

    @Test
    public void testExportRegisteredCourses() {

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("PDF content".getBytes());

        StudentCourse studentCourse = getStudentCourse();
        when(studentCourseRepository.findByUserId(1L)).thenReturn(List.of(studentCourse));
        when(pdfService.generateCoursePdf(anyList())).thenReturn(byteArrayInputStream);

        ByteArrayInputStream result = courseService.exportRegisteredCourses(1L);

        assertNotNull(result);
        assertEquals(byteArrayInputStream, result);
        verify(studentCourseRepository, times(1)).findByUserId(1L);
        verify(pdfService, times(1)).generateCoursePdf(anyList());
    }

    @Test
    public void testExportRegisteredCoursesNoCoursesFound() {
        when(studentCourseRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            courseService.exportRegisteredCourses(1L);
        });

        assertEquals("no course found for this Student", exception.getMessage());
        verify(studentCourseRepository, times(1)).findByUserId(1L);
        verify(pdfService, times(0)).generateCoursePdf(anyList());
    }

    private static Course getCourse() {
        Course courseEntity = new Course();
        courseEntity.setId(1L);
        courseEntity.setName("Test Course");
        courseEntity.setDescription("Test Description");
        courseEntity.setInstructorName("Test Instructor");
        courseEntity.setDuration(10);
        courseEntity.setDate(LocalDate.now());
        return courseEntity;
    }

    private static StudentCourse getStudentCourse() {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourse(getCourse());
        studentCourse.setUser(getUser());
        return studentCourse;
    }

    private static User getUser() {
        User student = new User();
        student.setId(1L);
        student.setUsername("Test Student");
        return student;
    }
}
