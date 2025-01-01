package com.example.food_web2.controller;

import com.example.food_web2.dto.request.OrderDetailRequest;
import com.example.food_web2.dto.request.OrderRequest;
import com.example.food_web2.dto.response.FoodResponse;
import com.example.food_web2.dto.response.OrderResponse;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.service.CustomerService;
import com.example.food_web2.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
//@RequestMapping()
//@RestController
//@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;
    CustomerService customerService;

    @GetMapping({"/order/findAll", "/admin/orders"})
    public String findAll(HttpSession session, Model model) {
        List<OrderResponse> orderResponses = orderService.findAll();
        orderResponses = orderResponses.stream()
                .sorted(Comparator.comparingInt(order -> {
                    if ("Chờ xác nhận".equals(order.getStatus())) {
                        return 0; // Ưu tiên trạng thái "Chờ xác nhận"
                    } else if ("Chờ lấy hàng".equals(order.getStatus())) {
                        return 1;
                    } else if ("Chờ giao hàng".equals(order.getStatus())) {
                        return 2;
                    } else if ("Giao hàng thành công".equals(order.getStatus())) {
                        return 3;
                    } else {
                        return 4; // Các trạng thái khác
                    }
                }))
                .toList();
        UserResponse user = (UserResponse) session.getAttribute("user");
        if (user != null && "Admin".equals(user.getRole())) {
            model.addAttribute("orders", orderResponses); // Admin cần danh sách đầy đủ
            return "/admin/order/qldh"; // Giao diện Admin
        }
        return "/admin/trangchu";
    }

//    @GetMapping("/admin/order")
//    public String getPortfolio(Model model) {
//        model.addAttribute("orders", orderService.findAll());
//        return "/admin/order/qldh";
//    }

    @GetMapping({"/admin/order/search", "/user/order/search"})
    public String findById(@RequestParam(value = "search") int id, Model model, HttpSession httpSession) {
        model.addAttribute("searchTerm", id);
        UserResponse user = (UserResponse) httpSession.getAttribute("user");
        if (user != null && "Admin".equals(user.getRole())) {
            model.addAttribute("orderResponse", orderService.findById(id));
            return "/admin/order/qldh";
        } else if (user != null && "Customer".equals(user.getRole())) {
            model.addAttribute("orderResponse", orderService.findById(id));
            return "/user/order/order";
        }
        return "/admin/trangchu";
    }

    @GetMapping({"/admin/order/customer/search", "/order"})
    public String findByCustomerId(@RequestParam(value = "search", required = false) Integer id, Model model, HttpSession httpSession) {
        if (id != null && id > 0) {
            model.addAttribute("searchTermCustomer", id);
        }
        UserResponse user = (UserResponse) httpSession.getAttribute("user");
        if (user != null && "Admin".equals(user.getRole())) {
            if (id != null && id > 0) {
                model.addAttribute("listOrderResponse", orderService.findByCustomerId(id));
            }
            return "/admin/order/qldh";
        } else if (user != null && "Customer".equals(user.getRole())) {
            List<OrderResponse> orderResponses = orderService.findByCustomerId(customerService.findByUserId(user.getId()).getId());
            orderResponses = orderResponses.stream()
                    .sorted(Comparator.comparingInt(order -> {
                        if ("Chờ xác nhận".equals(order.getStatus())) {
                            return 0; // Ưu tiên trạng thái "Chờ xác nhận"
                        } else if ("Chờ lấy hàng".equals(order.getStatus())) {
                            return 1;
                        } else if ("Chờ giao hàng".equals(order.getStatus())) {
                            return 2;
                        } else if ("Giao hàng thành công".equals(order.getStatus())) {
                            return 3;
                        }else {
                            return 4; // Các trạng thái khác
                        }
                    }))
                    .toList();
            model.addAttribute("listOrderResponse", orderResponses);
            return "/user/order/order";
        }
        return "/admin/trangchu";
    }

    @PostMapping("/order")
    public String createOrder(@RequestParam("listCartItemId") List<Integer> listCartItemId, @RequestParam int foodId, @RequestParam int quantity, HttpSession session, RedirectAttributes redirectAttributes) {
        UserResponse userResponse = (UserResponse) session.getAttribute("user");
        if (userResponse == null) {
            return "redirect:/user/dangnhap"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        // Tạo order request
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerId(userResponse.getId());

        // Thêm chi tiết đơn hàng
        List<OrderDetailRequest> orderDetails = new ArrayList<>();
        OrderDetailRequest detail = new OrderDetailRequest();
        detail.setFoodId(foodId);
        detail.setQuantity(quantity);
        orderDetails.add(detail);
        orderRequest.setOrderDetailRequests(orderDetails);

        // Lưu order
        OrderResponse orderResponse = orderService.create(orderRequest);

        // Chuyển thông tin order sang trang thanh toán
        redirectAttributes.addAttribute("orderId", orderResponse.getId());
        redirectAttributes.addAttribute("listCartItemId", listCartItemId);
        return "redirect:/payment"; // Tên của template thanh toán (payment.html)
    }


//    @PostMapping("/order/orders")
//    public String createOrder(
//            @RequestParam("foodIds") List<Integer> foodIds,
//            @RequestParam("quantities") List<Integer> quantities,
//            HttpSession session,
//            RedirectAttributes redirectAttributes) {
//
//        UserResponse userResponse = (UserResponse) session.getAttribute("user");
//        if (userResponse == null) {
//            return "redirect:/user/dangnhap"; // Chuyển hướng nếu chưa đăng nhập
//        }
//
//        // Tạo request order
//        OrderRequest orderRequest = new OrderRequest();
//        orderRequest.setCustomerId(userResponse.getId());
//
//        // Thêm các chi tiết đơn hàng
//        List<OrderDetailRequest> orderDetails = new ArrayList<>();
//        for (int i = 0; i < foodIds.size(); i++) {
//            OrderDetailRequest detail = new OrderDetailRequest();
//            detail.setFoodId(foodIds.get(i));
//            detail.setQuantity(quantities.get(i));
//            orderDetails.add(detail);
//        }
//        orderRequest.setOrderDetailRequests(orderDetails);
//
//        // Gửi yêu cầu tạo đơn hàng
//        OrderResponse orderResponse = orderService.create(orderRequest);
//
//        // Chuyển hướng sang trang thanh toán
//        redirectAttributes.addAttribute("orderId", orderResponse.getId());
//        return "redirect:/payment";
//    }


//    @PostMapping("/order/orders")
//    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest, HttpSession session, RedirectAttributes redirectAttributes) {
//        // Xử lý OrderRequest và tạo đơn hàng
//        try {
//            // Giả sử bạn sẽ gọi service để lưu đơn hàng vào database
//            UserResponse userResponse = (UserResponse) session.getAttribute("user");
//            if (userResponse != null) {
//                orderRequest.setCustomerId(userResponse.getId());
////                orderRequest.setCustomerId(customerService.findByUserId(userResponse.getId()).getId());
//            }
//            orderService.create(orderRequest);
//            return ResponseEntity.ok("Đặt hàng thành công!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã có lỗi xảy ra!");
//        }
//    }

    @PostMapping("/order/orders")
    public String createOrder(@RequestParam("listCartItemId") List<Integer> listCartItemId, @RequestBody OrderRequest orderRequest, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            UserResponse userResponse = (UserResponse) session.getAttribute("user");
            if (userResponse != null) {
                orderRequest.setCustomerId(userResponse.getId());
            }
            OrderResponse orderResponse = orderService.create(orderRequest);
            redirectAttributes.addAttribute("orderId", orderResponse.getId());
            redirectAttributes.addAttribute("listCartItemId", listCartItemId);
            return "redirect:/payment";
        } catch (Exception e) {
            return "redirect:/cart";
        }
    }


    @PostMapping({"admin/orders/update/{id}","/user/orders/update/{id}"})
    @ResponseBody
    OrderResponse update(@PathVariable("id") int id, @RequestBody OrderRequest orderRequest) {
        return orderService.update(id, orderRequest);
    }

    @GetMapping({"/admin/orders/delete", "/user/orders/delete"})
    public String delete(@RequestParam("id") int id, HttpSession session) {
        UserResponse userResponse = (UserResponse) session.getAttribute("user");
        if (userResponse != null) {
            if (userResponse.getRole().equals("Admin")) {
                orderService.delete(id);
                return "redirect:/admin/orders";
            } else {
                orderService.delete(id);
                return "redirect:/order";
            }
        }
        return "redirect:/user/dangnhap";
    }

    @DeleteMapping("/order/deleteAll")
    void deleteAll() {
        orderService.deleteAll();
    }

    @GetMapping("/admin/invoice")
    public String invoice() {
        return "/admin/invoice/qlhd";
    }
}