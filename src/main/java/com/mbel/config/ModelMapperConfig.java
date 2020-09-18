package com.mbel.config;

import com.mbel.dto.FetchProductSetDto;
import com.mbel.model.Product;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
	public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(Product.class, FetchProductSetDto.class);
	    return modelMapper;
    }

}
