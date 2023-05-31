package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMapper extends AbstractMapper<User, UserDTO>{

    @Override
    public User toEntity(UserDTO userDTO) {
        log.info("Mapping user {} to entity", userDTO);
        User user = new User();
        BeanUtils.copyProperties(userDTO , user);
        return user;
    }

    @Override
    public UserDTO toDto(User user) {
        log.info("Mapping user {} to DTO", user);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setFirstName(user.getFirstName());
        userDTO.setRole(user.getRole().name());
        return userDTO;
    }
}
