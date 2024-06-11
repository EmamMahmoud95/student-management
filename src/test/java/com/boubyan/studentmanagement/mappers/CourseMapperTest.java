package com.boubyan.studentmanagement.mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.xml.crypto.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.boubyan.studentmanagement.entity.Course;
import com.boubyan.studentmanagement.mapper.CourseMapper;

public class CourseMapperTest {

    private CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);


    @Test
    public void testMapCourseEntityToCourseResponse() {

        Course course = new Course();
        course.setId(1L);
        course.setName("Test Course");
        course.setDescription("Test Description");
        course.setInstructorName("Test Instructor");
        course.setDuration(10);
        LocalDate now = LocalDate.now();
        course.setDate(now);

        com.boubyan.studentmanagement.model.Course courseResponse = new com.boubyan.studentmanagement.model.Course();
        courseResponse.setId(1);
        courseResponse.setName("Test Course");
        courseResponse.setDescription("Test Description");
        courseResponse.setInstructorName("Test Instructor");
        courseResponse.setDuration(10);
        courseResponse.setDate(now);

        List<Course> courseEntities = Arrays.asList(course);
        List<com.boubyan.studentmanagement.model.Course> courseResponses = courseMapper.mapCourseEntityToCourseResponse(courseEntities);

        assertNotNull(courseResponses);
        assertEquals(1, courseResponses.size());
        assertEquals(courseResponse.getId(), courseResponses.get(0).getId());
        assertEquals(courseResponse.getName(), courseResponses.get(0).getName());
        assertEquals(courseResponse.getDescription(), courseResponses.get(0).getDescription());
        assertEquals(courseResponse.getInstructorName(), courseResponses.get(0).getInstructorName());
        assertEquals(courseResponse.getDuration(), courseResponses.get(0).getDuration());
        assertEquals(courseResponse.getDate(), courseResponses.get(0).getDate());
    }
}
