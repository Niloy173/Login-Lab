package com.example.loginLab.demo.mapper;

import com.example.loginLab.demo.dto.UserDto;
import com.example.loginLab.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {


    User dtoToEntity(UserDto userDto);

//    @Mapping(target = "password", source = "password", ignore = true)
    UserDto entityToDto(User user);
}
