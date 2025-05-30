package com.user.mapper;

import com.user.binding.ActivateAccount;
import com.user.binding.LoginRequest;
import com.user.binding.RegistrationRequest;
import com.user.binding.UserResponse;
import com.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T13:34:47+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240524-2033, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(RegistrationRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setDateOfBirth( request.getDateOfBirth() );
        user.setEmail( request.getEmail() );
        user.setFullName( request.getFullName() );
        user.setGender( request.getGender() );
        user.setMobile( request.getMobile() );
        user.setSsn( request.getSsn() );

        return user;
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setDateOfBirth( user.getDateOfBirth() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setFullName( user.getFullName() );
        userResponse.setGender( user.getGender() );
        userResponse.setMobile( user.getMobile() );
        userResponse.setSsn( user.getSsn() );

        return userResponse;
    }

    @Override
    public User loginToEntity(LoginRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( request.getEmail() );
        user.setPassword( request.getPassword() );

        return user;
    }

    @Override
    public void updateUserFromActivateAccount(ActivateAccount request, User user) {
        if ( request == null ) {
            return;
        }

        if ( request.getNewPassword() != null ) {
            user.setPassword( request.getNewPassword() );
        }
        if ( request.getEmail() != null ) {
            user.setEmail( request.getEmail() );
        }
    }

    @Override
    public List<UserResponse> toUserResponseList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( users.size() );
        for ( User user : users ) {
            list.add( toUserResponse( user ) );
        }

        return list;
    }
}
