package com.boubyan.studentmanagement.mapper;

import com.boubyan.studentmanagement.entity.User;
import com.boubyan.studentmanagement.payload.response.UserInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotNull;

@Mapper(componentModel = "spring")
public abstract class StudentMapper {
    @Autowired
    PasswordEncoder encoder;

    /**
     * map UserInformation from the given user.
     *
     * @param user user entity to map the needed UserInformation
     * @return mapped UserInformation
     */
    public abstract UserInformation mapUserInformationJWT(User user);

    /**
     * map UserEntity From the given student Model.
     *
     * @param student student Model to  map user entity
     * @return mapped user Entity
     */
    @Mapping(target = "password", source = "password", qualifiedByName = "buildPassword")
    @Mapping(target = "userType",constant = "Student")
    public abstract User mapUserEntityFromStudentModel(com.boubyan.studentmanagement.model.Student student);


    /**
     * encode the given string password using PasswordEncoder.
     *
     * @param password the string password to be encoded
     * @return encoded String password
     */
    @Named("buildPassword")
    public String buildPassword(@NotNull String password) {
        return encoder.encode(password);
    }
}
