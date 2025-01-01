package com.example.food_web2.controller;


import com.example.food_web2.dto.request.FoodRequest;
import com.example.food_web2.dto.response.ApiResponse;
import com.example.food_web2.dto.response.FoodResponse;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.service.FoodService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
//@RestController
//@RequestMapping("/foods")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodController {
    FoodService foodService;

    @GetMapping({"/", "/admin/foods"})
    public String getAll(HttpSession session, Model model) {
        // Fetch all foods
        List<FoodResponse> foods = foodService.getAll();
        List<FoodResponse> result = new ArrayList<>(foods);
        // Add foods to the model
        UserResponse user = (UserResponse) session.getAttribute("user");
        // Kiểm tra vai trò người dùng
        if (user != null && "Admin".equals(user.getRole())) {
            model.addAttribute("foods", result); // Admin cần danh sách đầy đủ
            return "/admin/product/qlsp"; // Giao diện Admin
        }
        Collections.shuffle(result);
        model.addAttribute("foods", result);
        return "/index";
    }


    @GetMapping("/foods/{id}")
    String findById(@PathVariable int id, Model model) {
        FoodResponse foodResponse = foodService.findById(id);
        model.addAttribute("food", foodResponse);
        return "/user/foods/sanpham";
    }

    @GetMapping({"/foods/name", "/admin/product/search"})
    String getByName(@RequestParam(value = "search") String name, Model model, HttpSession httpSession) {
        model.addAttribute("searchTerm", name);
        UserResponse user = (UserResponse) httpSession.getAttribute("user");
        List<FoodResponse> findById = new ArrayList<>();
        try {
            int id = Integer.parseInt(name);
            findById.add(foodService.findById(id));
        } catch (NumberFormatException e) {
        }
        if (!findById.isEmpty()) {
            model.addAttribute("foodsSearch", findById);
            return "/admin/product/qlsp";
        } else {
            List<FoodResponse> findByName = foodService.findByName(name);
            List<FoodResponse> findByBrandName = foodService.findByBrandName(name);
            model.addAttribute("foodsSearch", findByBrandName == null ? findByName : findByBrandName);
        }
        if (user != null && "Admin".equals(user.getRole())) {
            return "/admin/product/qlsp";
        } else {
            return "/user/foods/search";
        }
    }

    @PostMapping("/foods/create")
    public String create(@RequestParam("name") String name,
                         @RequestParam("description") String description,
                         @RequestParam("brandName") String brandName,
                         @RequestParam("price") double price,
                         @RequestParam("quantity") int quantity,
                         @RequestParam("location") String location,
                         @RequestParam("imageFile") MultipartFile file,
                         Model model) {
        FoodRequest foodRequest = FoodRequest.builder().name(name)
                .description(description)
                .brandName(brandName)
                .price(price)
                .quantity(quantity)
                .location(location)
                .build();
        try {
            if (!file.isEmpty()) {
                // Lưu file vào thư mục upload
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String filePath ="E:\\Users\\ploan\\Do_An_Chuyen_Nghanh\\Food_Web2\\src\\main\\resources\\static\\assets\\images\\foods\\" + fileName;
                file.transferTo(new java.io.File(filePath));

                // Lưu đường dẫn file vào DTO
                foodRequest.setImages("/assets/images/foods/" + fileName);
            }
            model.addAttribute("message", "Thêm sản phẩm thành công");
            // Gọi service để cập nhật database
            foodService.create(foodRequest);
            return "redirect:/admin/foods";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/foods/update/{id}")
    public String showFoodUpdateForm(@PathVariable int id, Model model) {
        FoodResponse foodResponse = foodService.findById(id);
        model.addAttribute("food", foodResponse);
        return "/admin/product/updateFood";
    }

    @PostMapping("/foods/update")
    public String update(@RequestParam("id") int id,
                         @RequestParam("name") String name,
                         @RequestParam("description") String description,
                         @RequestParam("brandName") String brandName,
                         @RequestParam("price") double price,
                         @RequestParam("quantity") int quantity,
                         @RequestParam("location") String location,
                         @RequestParam("imageFile") MultipartFile file){
        FoodRequest foodRequest = FoodRequest.builder().name(name)
                .description(description)
                .brandName(brandName)
                .price(price)
                .quantity(quantity)
                .location(location)
                .build();
        try {
            if (!file.isEmpty()) {
                // Lưu file vào thư mục upload
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                String filePath ="E:\\Users\\ploan\\Do_An_Chuyen_Nghanh\\Food_Web2\\src\\main\\resources\\static\\assets\\images\\foods\\" + fileName;
                file.transferTo(new java.io.File(filePath));

                // Lưu đường dẫn file vào DTO
                foodRequest.setImages("/assets/images/foods/" + fileName);
            }else {
                foodRequest.setImages(foodService.findById(id).getImages());
            }

            // Gọi service để cập nhật database
            foodService.update(id,foodRequest);

            return "redirect:/admin/foods";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/admin/foods/delete")
    public String delete(@RequestParam("id") int id, @RequestParam("href") String href) {
        foodService.delete(id);
        return "redirect:"+href;
    }

}
