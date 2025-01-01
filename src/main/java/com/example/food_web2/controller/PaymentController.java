package com.example.food_web2.controller;

import com.example.food_web2.dto.request.PaymentRequest;
import com.example.food_web2.dto.response.OrderResponse;
import com.example.food_web2.dto.response.PaymentResponse;
import com.example.food_web2.dto.response.UserResponse;
import com.example.food_web2.service.CartItemService;
import com.example.food_web2.service.OrderService;
import com.example.food_web2.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping("/payment")
//@RestController
//@RequestMapping("/payments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    PaymentService paymentService;
    OrderService orderService;
    private final CartItemService cartItemService;

    @GetMapping("/payment")
    String paymentPage(@RequestParam("listCartItemId") List<Integer> listCartItemId, @RequestParam("orderId") int orderId, HttpSession session, Model model) {
        UserResponse user = (UserResponse) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/dangnhap"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        OrderResponse orderResponse = orderService.findById(orderId);
        model.addAttribute("order", orderResponse); // Thêm vào model cho view
        model.addAttribute("listCartItemId", listCartItemId);
        return "/user/payment/thanhtoan";
    }

    @GetMapping({"/payment/findAll", "/admin/financialStatement"})
    public List<PaymentResponse> getAll() {
        return paymentService.getAll();
    }

    @GetMapping("/payment/{id}")
    public PaymentResponse getById(@PathVariable int id) {
        return paymentService.findById(id);
    }

    @GetMapping("/payment/order/{id}")
    public PaymentResponse getByOrderId(@PathVariable int id) {
        return paymentService.findByOrderId(id);
    }

    @PostMapping("/payment/create")
    public String create(
            @RequestParam(value = "listCartItemId", required = false) String listCartItemId,
            @RequestParam("orderId") int orderId,
            @RequestParam("paymentMethod") String paymentMethod,
            Model model) {

        // Parse the string input into a list of integers
        List<Integer> cartItemIds = new ArrayList<>();
        if (!listCartItemId.equals("[]")) {
            try {
                listCartItemId = listCartItemId.replaceAll("[\\[\\]]", ""); // Remove brackets
                String[] items = listCartItemId.split(",");
                for (String item : items) {
                    cartItemIds.add(Integer.parseInt(item.trim()));
                }
            } catch (NumberFormatException e) {
                model.addAttribute("error", "Invalid cart item IDs.");
                return "errorPage"; // Return an error page if parsing fails
            }
        }

        // Create the payment request
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderId)
                .paymentMethod(paymentMethod)
                .build();
        PaymentResponse paymentResponse = paymentService.create(paymentRequest);

        // Process the cart items
        if (!cartItemIds.isEmpty()) {
            for (Integer itemId : cartItemIds) {
                cartItemService.delete(itemId);
            }
        }

        model.addAttribute("paymentResponse", paymentResponse);
        return "/user/payment/orderSuccess";
    }


    @PutMapping("/payment/{id}")
    public PaymentResponse update(@PathVariable int id, @RequestBody PaymentRequest paymentRequest) {
        return paymentService.update(id, paymentRequest);
    }

    @DeleteMapping("/payment/{id}")
    public void delete(@PathVariable int id) {
        paymentService.delete(id);
    }



    @GetMapping("/day")
    public double getRevenueByDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return paymentService.getRevenueByDay(date);
    }

    @GetMapping("/month")
    public double getRevenueByMonth(@RequestParam int year, @RequestParam int month) {
        return paymentService.getRevenueByMonth(year, month);
    }

    @GetMapping("/year")
    public double getRevenueByYear(@RequestParam int year) {
        return paymentService.getRevenueByYear(year);
    }

    @GetMapping("/monthly/{year}")
    public Map<Integer, Double> getMonthlyRevenueByYear(@PathVariable int year) {
        return paymentService.getMonthlyRevenueByYear(year);
    }


    //Báo cáo thống kê
    @GetMapping("/admin/statistics")
    public String statisticsPage() {
        return "/admin/financialStatement/bctk";
    }

    @GetMapping("/admin/revenue/daily/current-month")
    @ResponseBody
    public List<Double> getDailyRevenueForCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        return paymentService.getRevenueForMonth(currentMonth, currentYear);
    }

    @GetMapping("/admin/revenue/month/current-year")
    @ResponseBody
    public List<Double> getMonthRevenueForCurrentYear() {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        return paymentService.getRevenueForYear(currentYear);
    }

    @GetMapping("/admin/revenue/fiveYear")
    @ResponseBody
    public List<Double> getRevenueForFiveYear() {
        List<Double> revenueForFiveYears = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        for (int i = 5; i > 0; i--) {
            double revenueByYear = paymentService.getRevenueByYear(currentYear - i+1);
            revenueForFiveYears.add(revenueByYear);
        }
        return revenueForFiveYears;
    }


}
