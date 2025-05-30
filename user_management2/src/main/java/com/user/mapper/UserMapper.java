package com.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.user.binding.ActivateAccount;
import com.user.binding.LoginRequest;
import com.user.binding.RegistrationRequest;
import com.user.binding.UserResponse;
import com.user.entity.User;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    // Map registration request to User entity
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "accStatus", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    User toEntity(RegistrationRequest request);

    // Map User entity to UserResponse DTO
    UserResponse toUserResponse(User user);

    // Map LoginRequest to User entity
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "mobile", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "ssn", ignore = true)
    @Mapping(target = "accStatus", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    User loginToEntity(LoginRequest request);

    // Map ActivateAccount to User entity for updating
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "mobile", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    @Mapping(target = "ssn", ignore = true)
    @Mapping(target = "accStatus", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(source = "newPassword", target = "password")
    void updateUserFromActivateAccount(ActivateAccount request, @MappingTarget User user);

    // Convert list of User entities to list of UserResponse DTOs
    List<UserResponse> toUserResponseList(List<User> users);
}
