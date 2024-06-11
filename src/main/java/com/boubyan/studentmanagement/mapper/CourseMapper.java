package com.boubyan.studentmanagement.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.mapstruct.Mapper;

import com.boubyan.studentmanagement.entity.Course;

@Mapper(componentModel = "spring", imports = { LocalDateTime.class })
public abstract class CourseMapper {

    public abstract List<com.boubyan.studentmanagement.model.Course> mapCourseEntityToCourseResponse(List<Course> course);

}
