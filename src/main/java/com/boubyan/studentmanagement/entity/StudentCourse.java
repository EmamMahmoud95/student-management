package com.boubyan.studentmanagement.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "t_student_course")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourse {

    public static final String SEQ_T_STUDENT_COURSE_SEQUENCE = "seq_t_student_course_sequence";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_T_STUDENT_COURSE_SEQUENCE)
    @SequenceGenerator(name = SEQ_T_STUDENT_COURSE_SEQUENCE, sequenceName = SEQ_T_STUDENT_COURSE_SEQUENCE, allocationSize = 1)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
