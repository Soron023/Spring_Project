package com.example.springbootapp.mapper;

import com.example.springbootapp.dto.ProductDto;
import com.example.springbootapp.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductDto toDto(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product toEntity(ProductDto dto);

    List<ProductDto> toDtoList(List<Product> products);
    List<Product> toEntityList(List<ProductDto> dtos);
} 