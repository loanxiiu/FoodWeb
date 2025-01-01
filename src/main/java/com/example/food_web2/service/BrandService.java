package com.example.food_web2.service;

import com.example.food_web2.dto.request.BrandRequest;
import com.example.food_web2.dto.response.BrandResponse;
import com.example.food_web2.mapper.BrandMapper;
import com.example.food_web2.mapper.FoodMapper;
import com.example.food_web2.repository.BrandRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandService {
    BrandRepository brandRepository;
    BrandMapper brandMapper;
    FoodMapper foodMapper;

    public BrandResponse create(BrandRequest brandRequest) {
        var brand= brandMapper.toBrand(brandRequest);
        brand = brandRepository.save(brand);
        return brandMapper.toBrandResponse(brand);
    }

    public List<BrandResponse> getAll() {
        return brandRepository.findAll().stream()
                .map(brand -> {
                    BrandResponse brandResponse = brandMapper.toBrandResponse(brand);
                    brandResponse.setFoods(foodMapper.mapFoods(brand.getFoods()));
                    return brandResponse;
                })
                .toList();
    }

    public BrandResponse findById(int id) {
        return brandRepository.findById(id).map(brandMapper::toBrandResponse).orElse(null);
    }

}
