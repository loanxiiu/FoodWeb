package com.example.food_web2.service;

import com.example.food_web2.dto.request.CustomerRequest;
import com.example.food_web2.dto.request.UserRequest;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.entity.Customer;
import com.example.food_web2.entity.User;
import com.example.food_web2.mapper.UserMapper;
import com.example.food_web2.repository.CustomerRepository;
import com.example.food_web2.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    CustomerService customerService;
    private final CartService cartService;

    public UserResponse authenticate(UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());
        if (!userOptional.isEmpty()) {
            User user = userOptional.get();
            return user.getPassword().equals(userRequest.getPassword()) ? userMapper.toUserResponse(user) : null;
        }
        return null;
    }

    public UserResponse create(UserRequest userRequest) {
        userRequest.setRole("Customer");
        log.info("Service: createUser");
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("User with email " + userRequest.getEmail() + " already exists");
        }
        User user = userMapper.toUser(userRequest);
        user = userRepository.save(user);
        customerService.create();
        cartService.create();
        return userMapper.toUserResponse(user);
    }

    public UserResponse update(int id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(user, userRequest);
        userRepository.save(user);
        cartService.create();
        return userMapper.toUserResponse(userRepository.save(user));
    }

//    public UserResponse getMyInfo() {
//        var context = SecurityContextHolder.getContext();
//        String name = context.getAuthentication().getName();
//        User user = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//        return userMapper.toUserResponse(user);
//    }

    //    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse findById(int id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
        return userMapper.toUserResponse(user);
    }
}
