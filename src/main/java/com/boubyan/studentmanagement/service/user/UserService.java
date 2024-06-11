package com.boubyan.studentmanagement.service.user;

import com.boubyan.studentmanagement.entity.User;

public interface UserService {

    /**
     * create student with the given user details.
     *
     * @param student student details to create student entity
     */
    void insertStudent(com.boubyan.studentmanagement.model.Student student);

    /**
     * get Student By id.
     *
     * @param id long User id
     * @return User
     */
    User getStudentById(Long id);
}
