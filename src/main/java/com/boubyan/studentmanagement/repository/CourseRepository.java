package com.boubyan.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boubyan.studentmanagement.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}
