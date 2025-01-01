package com.example.food_web2.service;

import com.example.food_web2.dto.request.FoodRequest;
import com.example.food_web2.dto.response.FoodResponse;
import com.example.food_web2.entity.Food;
import com.example.food_web2.mapper.FoodMapper;
import com.example.food_web2.repository.BrandRepository;
import com.example.food_web2.repository.FoodRepository;
import com.example.food_web2.repository.OrderDetailRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodService {
    FoodRepository foodRepository;
    FoodMapper foodMapper;
    BrandRepository brandRepository;
    private final OrderDetailRepository orderDetailRepository;

    public FoodResponse create(FoodRequest foodRequest) {
        var food = foodMapper.toFood(foodRequest);
        var brand = brandRepository.findByName(foodRequest.getBrandName()).orElseThrow(() -> new RuntimeException("Brand not found"));
        food.setBrand(brandRepository.findById(brand.getId()).orElseThrow(() -> new RuntimeException("Brand not found")));
        return foodMapper.toFoodResponse(foodRepository.save(food));
    }

    public FoodResponse findById(int id) {
        var food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found"));
        return foodMapper.toFoodResponse(food);
    }


    public List<FoodResponse> findByName(String name) {
        // Fetch all foods with the specified name
        var foods = foodRepository.findByNameContaining(name);

        // Throw an exception if the list is empty
        if (foods.isEmpty()) {
            return null;
        }

        // Map the list of Food entities to a list of FoodResponse DTOs
        return foods.stream().map(foodMapper::toFoodResponse).toList();
    }

    public List<FoodResponse> findByBrandName(String brandName) {
        var foods = foodRepository.findByBrandName(brandName);
        if (foods.isEmpty()) {
            return null;
        }
        return foods.stream().map(foodMapper::toFoodResponse).toList();
    }


    public List<FoodResponse> getAll() {
        return foodRepository.findAll().stream().map(foodMapper::toFoodResponse).toList();
    }


    public FoodResponse update(int id, FoodRequest foodRequest) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("Food not found"));

        // Look up the Brand by name
        var brand = brandRepository.findByName(foodRequest.getBrandName())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        // Set the brand to the food
        food.setBrand(brand);

        // Update the Food entity using the mapper
        foodMapper.updateFood(food, foodRequest);

        // Save the updated food and return the response
        return foodMapper.toFoodResponse(foodRepository.save(food));
    }

    // sửa sau
    // thêm 1 fieltrạng thái để khi muốn xóa thì set trạng thái bằng false
    // vì khi xóa sp thì trong orderdetail vẫn phải còn chỉ có trong cartitem là mất
    @Transactional
    public void delete(int id) {//xóa food nhừng vẫn giữ id trong order
        var foodOptional = foodRepository.findById(id);
        if (foodOptional.isPresent()) {
            Food food = foodOptional.get();
            food.getOrderDetails().stream().map(orderDetail -> {
                orderDetail.setFood(null);
                orderDetailRepository.save(orderDetail);
                return orderDetail;
            });
            foodRepository.delete(food);
        }
    }
}
