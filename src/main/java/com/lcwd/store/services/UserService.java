package com.lcwd.store.services;

import com.lcwd.store.dtos.UserDto;
import com.lcwd.store.entities.User;

import java.util.List;

public interface UserService {
    //create
    UserDto createUser(UserDto userDto);
    //update
    UserDto updateUser(UserDto userDto,String userId);
    //delete
    void deleteUser(String userId);
    //get all users
    List<UserDto> getAllUser();
    //get single
    UserDto getUser(String userId);
    // get single by email
    UserDto getUserByEmail(String email);
    //search user
    List<UserDto> searchUser(String keyword);
    //other user specific features
}
