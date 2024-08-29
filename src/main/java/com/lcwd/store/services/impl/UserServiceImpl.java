package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.UserDto;
import com.lcwd.store.entities.User;
import com.lcwd.store.repositories.UserRepository;
import com.lcwd.store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDto createUser(UserDto userDto) {
//        generate userid in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //convert dto to entity
        User user=dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        //entity to dto
        UserDto newDto=entityToDto(savedUser);
        return newDto;
    }
    
   
    
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found with given id"));
        user.setName(userDto.getName());
        //email ypdate kar skte ho
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        
        User updateUser = userRepository.save(user);
        UserDto userDto1 = entityToDto(updateUser);
        return userDto1;
    }
    
    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found with given id"));
        //delete
        userRepository.delete(user);
    
    }
    
    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }
    
    @Override
    public UserDto getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found with given id"));
        return entityToDto(user);
    }
    
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email"));
        
        return entityToDto(user);
    }
    
    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }
    
    private UserDto entityToDto(User savedUser) {
        UserDto userDto= UserDto.builder().userId(savedUser.getUserId()).
                name(savedUser.getName()).
                email(savedUser.getEmail()).
                password(savedUser.getPassword()).
                about(savedUser.getAbout()).
                gender(savedUser.getGender()).imageName(savedUser.getImageName()).build();
        return  userDto;
    }
    
    private User dtoToEntity(UserDto userDto) {
        User user = User.builder().userId(userDto.getUserId()).
                name(userDto.getName()).
                email(userDto.getEmail()).
                password(userDto.getPassword()).
                about(userDto.getAbout()).
                gender(userDto.getGender()).imageName(userDto.getImageName()).build();
        
        return user;
    }
}
