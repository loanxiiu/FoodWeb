package com.example.food_web2.mapper;


import com.example.food_web2.dto.request.UserRequest;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "role", ignore = true)
    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateUser(@MappingTarget User user, UserRequest userRequest);

//    @Named("mapUsers")
//    default List<UserResponse> mapUsers(List<User> users) {
//        if (users == null) {
//            return new ArrayList<>();
//        }
//        return users.stream()
//                .map(user -> {
//                    UserResponse userResponse = new UserResponse();
//                    userResponse.setName(user.getName());
//                    userResponse.setPassword(user.getPassword());
//                    userResponse.setEmail(user.getEmail());
//                    userResponse.setPhone(user.getPhone());
//                    userResponse.setAddress(user.getAddress());
//                    userResponse.setImage(user.getImage());
//                    userResponse.setRole(user.getRole());
//                    return userResponse;
//                })
//                .collect(Collectors.toList());
//    }
}
