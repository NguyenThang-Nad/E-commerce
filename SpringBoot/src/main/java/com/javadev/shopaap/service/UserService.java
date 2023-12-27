package com.javadev.shopaap.service;

import com.javadev.shopaap.dto.UserDTO;
import com.javadev.shopaap.entity.UserEntity;
import com.javadev.shopaap.exception.DataNotFoundException;

public interface UserService {
    UserEntity createUser(UserDTO userDTO) throws DataNotFoundException;
   String login(String phoneNumber,String passWord) throws Exception;
}
