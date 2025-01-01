package com.example.food_web2.mapper;

import com.example.food_web2.dto.request.BrandRequest;
import com.example.food_web2.dto.response.BrandResponse;
import com.example.food_web2.entity.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    @Mapping(target = "foods", ignore = true)
    Brand toBrand(BrandRequest brandRequest);

    BrandResponse toBrandResponse(Brand brand);
}
