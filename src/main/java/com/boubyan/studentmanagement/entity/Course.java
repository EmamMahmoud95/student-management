package com.boubyan.studentmanagement.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "t_course")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    public static final String SEQ_T_COURSE_SEQUENCE = "seq_t_course_sequence";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_T_COURSE_SEQUENCE)
    @SequenceGenerator(name = SEQ_T_COURSE_SEQUENCE, sequenceName = SEQ_T_COURSE_SEQUENCE, allocationSize = 1)
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "DURATION")
    private Integer duration;
    @Column(name = "INSTRUCTOR_NAME")
    private String instructorName;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "COURSE_DATE")
    private LocalDate date;

}
