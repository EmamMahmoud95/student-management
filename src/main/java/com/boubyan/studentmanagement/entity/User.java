package com.boubyan.studentmanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "t_user", schema = "student_management")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    public static final String SEQ_T_USER_SEQUENCE = "seq_t_user_sequence";

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_T_USER_SEQUENCE)
    @SequenceGenerator(name = SEQ_T_USER_SEQUENCE, sequenceName = SEQ_T_USER_SEQUENCE, allocationSize = 1)
    private Long id;

    @Column(name = "USERNAME")

    private String username;
    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE")
    private UserType userType;
    @Column(name = "PASSWORD")
    private String password;

}
