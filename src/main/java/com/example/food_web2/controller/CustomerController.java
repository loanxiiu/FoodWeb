package com.example.food_web2.controller;

import com.example.food_web2.dto.request.CustomerRequest;
import com.example.food_web2.dto.response.ApiResponse;
import com.example.food_web2.dto.response.CustomerResponse;
import com.example.food_web2.entity.Customer;
import com.example.food_web2.service.CustomerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
//@RestController
//@RequestMapping("/customers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerController {
    CustomerService customerService;

    @GetMapping("/admin/customers")
    public String getAll(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "/admin/customer/qlkh";
    }

    @GetMapping("/admin/customer/search")
    public String getById(@RequestParam("search") int id, Model model) {
        model.addAttribute("searchTerm", id);
        model.addAttribute("customerResponse", customerService.findById(id));
        return "/admin/customer/qlkh";
    }

    @PostMapping("/customers/{id}")
    List<CustomerResponse> create() {
        return customerService.create();
    }

    @PutMapping("/customers/{id}")
    ApiResponse<CustomerResponse> update(@PathVariable int id, @RequestBody CustomerRequest customerRequest) {
        return ApiResponse.<CustomerResponse>builder()
                .result(customerService.update(id, customerRequest))
                .build();
    }

}
