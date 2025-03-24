package dev.pawan.sharemate.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import dev.pawan.sharemate.model.User;
import dev.pawan.sharemate.response.UserDTO;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "uid", source = "user.uid")
    UserDTO toDto(User user);
    
}