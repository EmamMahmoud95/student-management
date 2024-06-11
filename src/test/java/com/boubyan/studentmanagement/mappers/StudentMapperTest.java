package com.boubyan.studentmanagement.mappers;

import com.boubyan.studentmanagement.entity.User;
import com.boubyan.studentmanagement.mapper.StudentMapperImpl;
import com.boubyan.studentmanagement.model.Student;
import com.boubyan.studentmanagement.payload.response.UserInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {StudentMapperImpl.class, PasswordEncoder.class})
@ActiveProfiles("test")
public class StudentMapperTest {
    @InjectMocks
    private StudentMapperImpl userMapper;

    @Spy
    PasswordEncoder encoder;

    @Test
    public void testMapUserInformationJWT() {
        User user = new User();
        user.setId(1L);
        user.setUsername("John Doe");
        UserInformation userInformation = userMapper.mapUserInformationJWT(user);
        assertEquals(user.getId(), userInformation.getId());
        assertEquals(user.getUsername(), userInformation.getUsername());
    }

    @Test
    public void testMapUserInformationJWTNullUser() {
        UserInformation userInformation = userMapper.mapUserInformationJWT(null);
        assertNull(userInformation);
    }

    @Test
    public void testMapUserEntityFromUserModel() {
        Student userModel = new Student();
        userModel.setPassword("password123");
        userModel.setUsername("John Doe");
        User userEntity = userMapper.mapUserEntityFromStudentModel(userModel);
        assertEquals(userModel.getUsername(), userEntity.getUsername());
    }

    @Test
    public void testMapUserEntityFromUserModelNullUserModel() {
        User userEntity = userMapper.mapUserEntityFromStudentModel(null);
        assertNull(userEntity);
    }

    @Test
    public void testBuildPassword() {
        String plainPassword = "password123";
        when(encoder.encode(any())).thenReturn("encodedpass");
        String hashedPassword = userMapper.buildPassword(plainPassword);
        assertEquals("encodedpass", hashedPassword);
    }
}
