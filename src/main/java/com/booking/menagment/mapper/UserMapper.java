package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserDTO>{

    //unfinished
    @Override
    public User toEntity(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO , user);
        return user;
    }
    @Override
    public UserDTO toDto(User u) {
        UserDTO user = new UserDTO();
        BeanUtils.copyProperties(u, user);
        return user;
    }
}
