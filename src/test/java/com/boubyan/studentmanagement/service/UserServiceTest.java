package com.boubyan.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.boubyan.studentmanagement.entity.User;
import com.boubyan.studentmanagement.exceptions.customexceptions.BadRequestException;
import com.boubyan.studentmanagement.mapper.StudentMapper;
import com.boubyan.studentmanagement.model.Student;
import com.boubyan.studentmanagement.repository.UserRepository;
import com.boubyan.studentmanagement.service.user.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository studentRepository;

    @Mock
    private StudentMapper userMapper;

    @Test
    public void testInsertUser() {
        Student studentModel = new Student();
        studentModel.setUsername("Emam");
        studentModel.setPassword("test123");
        User userEntity = new User();
        userEntity.setId(132L);
        userEntity.setUsername("test");

        when(userMapper.mapUserEntityFromStudentModel(any())).thenReturn(userEntity);
        when(studentRepository.findUserByUsername(any())).thenReturn(Optional.empty());

        userService.insertStudent(studentModel);

        verify(studentRepository, times(1)).findUserByUsername(any());
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    public void testInsertUsernameExists() {
        Student studentModel = new Student(); // create a User model instance as needed
        studentModel.setUsername("Emam");
        studentModel.setPassword("test123");
        User userEntity = new User();
        userEntity.setId(132L);
        userEntity.setUsername("Emam");

        when(studentRepository.findUserByUsername(any())).thenReturn(Optional.of(userEntity));

        assertThrows(BadRequestException.class, () -> userService.insertStudent(studentModel));

        verify(studentRepository, times(1)).findUserByUsername(any());
        verify(userMapper, times(0)).mapUserEntityFromStudentModel(any());
        verify(studentRepository, times(0)).save(any());
    }

    @Test
    public void testGetStudentById() {

        User userEntity = new User();
        userEntity.setId(132L);
        userEntity.setUsername("Emam");

        when(studentRepository.findById(any())).thenReturn(Optional.of(userEntity));

        User user = userService.getStudentById(123L);
        assertNotNull(user);
        verify(studentRepository, times(1)).findById(any());
    }

    @Test
    public void testGetStudentByIdNoFound() {

        when(studentRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(BadRequestException.class, () -> userService.getStudentById(123L));
        verify(studentRepository, times(1)).findById(any());
    }
}
