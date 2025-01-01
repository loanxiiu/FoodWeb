package com.example.food_web2.controller;

import com.example.food_web2.dto.request.BrandRequest;
import com.example.food_web2.dto.response.ApiResponse;
import com.example.food_web2.dto.response.BrandResponse;
import com.example.food_web2.service.BrandService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/brand")
//@RestController
//@RequestMapping("/brands")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

//    @GetMapping("/brand")
//    public String getAllBrands(Model model) {
//        model.addAttribute("brands", brandService.getAll());
//        return "/admin/product/qlsp";
//    }

    @GetMapping("/brand")
    @ResponseBody
    public List<BrandResponse> getAllBrands() {
        return brandService.getAll();  // Assuming brandService.getAll() returns a List<Brand>
    }


    @GetMapping("/{id}")
    BrandResponse getById(@PathVariable int id) {
        return brandService.findById(id);
    }

    @PostMapping("/brand")
    ApiResponse<BrandResponse> create(@RequestBody BrandRequest brandRequest) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.create(brandRequest))
                .build();
    }
}
