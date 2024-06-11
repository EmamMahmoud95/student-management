package com.boubyan.studentmanagement.service.user;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.boubyan.studentmanagement.entity.User;
import com.boubyan.studentmanagement.exceptions.customexceptions.BadRequestException;
import com.boubyan.studentmanagement.mapper.StudentMapper;
import com.boubyan.studentmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StudentMapper studentMapper;

    @Override
    public void insertStudent(com.boubyan.studentmanagement.model.Student studentModel) {
        validateUsername(studentModel);
        User student = studentMapper.mapUserEntityFromStudentModel(studentModel);
        userRepository.save(student);
    }

    @Override
    public User getStudentById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("student not found"));
    }

    private void validateUsername(com.boubyan.studentmanagement.model.Student studentModel) {
        userRepository.findUserByUsername(studentModel.getUsername())
                .ifPresent(user -> {
                    throw new BadRequestException("can't register with this username");
                });
    }
}
