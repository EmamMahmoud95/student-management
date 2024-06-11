package com.boubyan.studentmanagement.repository;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.boubyan.studentmanagement.entity.StudentCourse;

@Repository
public interface StudentCourseRepository extends JpaRepository<StudentCourse, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM StudentCourse sc WHERE sc.user.id = :studentId AND sc.course.id = :courseId")
    int deleteByStudentIdAndCourseId(@Param("studentId") Long userId, @Param("courseId") Long courseId);

    @Cacheable("student_courses")
    List<StudentCourse> findByUserId(Long userId);
}
