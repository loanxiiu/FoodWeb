package com.example.food_web2.controller;

import com.example.food_web2.dto.request.UserRequest;
import com.example.food_web2.dto.response.ApiResponse;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.service.FoodService;
import com.example.food_web2.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
//@RequestMapping("/user")
//@RestController
//@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/admin")
    String showAdminPage() {
        return "/admin/trangchu";
    }

    @GetMapping({"/user/account", "/admin/account"})
    String getMyInfo(HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/dangnhap"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }
        if (user.getRole().equals("Admin")) {
            return "/admin/account/qltk";
        }
        return "/user/account/qltk";
    }

    @GetMapping("/user/dangnhap")
    public String showLogin() {
        return "/user/dangnhap/dangnhap"; // Thymeleaf will look for src/main/resources/templates/user/dangnhap.html
    }

    @GetMapping("/user/dangki")
    public String showRegister() {
        return "/user/dangki/dangki";
    }

    @PostMapping("/user/dangnhap")
    public String login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        Model model,
                        HttpSession session) {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(username);
        userRequest.setPassword(password);
        UserResponse userResponse = userService.authenticate(userRequest);
        if (userResponse != null) {
            if (userResponse.getRole().equals("Admin")) {
                session.setAttribute("user", userResponse);
                return "redirect:/admin";
            } else {
                session.setAttribute("user", userResponse);
                return "redirect:/";
            }
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "/user/dangnhap/dangnhap";
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Xóa toàn bộ thông tin trong session
        return "redirect:/"; // Chuyển hướng về trang đăng nhập
    }


    @GetMapping("/user")
    public ApiResponse<List<UserResponse>> getAll() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .build();
    }

    @GetMapping("/user/{id}")
    ApiResponse<UserResponse> findById(@PathVariable int id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.findById(id))
                .build();
    }

    @PostMapping("/user/create")
    public String createUser(@RequestParam("email") String email,
                             @RequestParam("password") String password,
                             HttpSession session) {
        UserRequest userRequest = UserRequest.builder()
                .email(email)
                .password(password)
                .build();
        session.setAttribute("user", userService.create(userRequest));
        return "redirect:/";
    }

    @PostMapping({"/admmin/account/update", "/account/update"})
    public String update(@RequestParam("id") int id,
                         @RequestParam("name") String name,
                         @RequestParam("address") String address,
                         @RequestParam("email") String email,
                         @RequestParam("phone") String phone,
                         @RequestParam("imageFile") MultipartFile file,
                         Model model, HttpSession session) {
        UserRequest userRequest = UserRequest.builder()
                .name(name)
                .address(address)
                .email(email)
                .phone(phone)
                .build();
        try {
            if (!file.isEmpty()) {
                // Lưu file vào thư mục upload
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String filePath = "E:\\Users\\ploan\\Do_An_Chuyen_Nghanh\\Food_Web2\\src\\main\\resources\\static\\assets\\images\\avatar\\" + fileName;
                file.transferTo(new java.io.File(filePath));

                // Lưu đường dẫn file vào DTO
                userRequest.setImage("/assets/images/avatar/" + fileName);
            } else {
                userRequest.setImage(userService.findById(id).getImage());
            }

            // Gọi service để cập nhật database
            UserResponse userResponse = userService.update(id, userRequest);
            session.setAttribute("user", userResponse);
            model.addAttribute("user", userRequest);
            model.addAttribute("message", "Sửa thông tin thành công");
            return "/admin/account/qltk";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    //    @PutMapping("/{id}")
    @PostMapping("/user/edit")
    String update(@RequestParam(value = "name") String name,
                  @RequestParam(value = "address") String address,
                  @RequestParam(value = "orderId") String orderId,
                  HttpSession session,
                  RedirectAttributes redirectAttributes) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        UserRequest userRequest = UserRequest.builder()
                .name(name == null || name.isEmpty() ? user.getName() : name)
                .address(address == null || address.isEmpty() ? user.getAddress() : address)
                .phone(user.getPhone())
                .role(user.getRole())
                .image(user.getImage())
                .email(user.getEmail())
                .build();
        UserResponse userResponse = userService.update(user.getId(), userRequest);
        session.setAttribute("user", userResponse);
        redirectAttributes.addAttribute("orderId", orderId);
        return "redirect:/payment";
    }
}
