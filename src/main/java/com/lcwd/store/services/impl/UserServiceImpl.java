package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.PageableResponse;
import com.lcwd.store.dtos.UserDto;
import com.lcwd.store.entities.User;
import com.lcwd.store.exceptions.ResourceNotFoundException;
import com.lcwd.store.helper.Helper;
import com.lcwd.store.repositories.UserRepository;
import com.lcwd.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${user.profile.image.path}")
    private String imagePath;
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Override
    public UserDto createUser(UserDto userDto) {
//        generate userid in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //convert dto to entity
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);
        //entity to dto
        UserDto newDto = entityToDto(savedUser);
        return newDto;
    }
    
    
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new
                ResourceNotFoundException("User not Found with given id"));
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
        User user = userRepository.findById(userId).orElseThrow(() -> new
                ResourceNotFoundException("User not Found with given id"));
        //delete user profile img
        String fullPath = imagePath + user.getImageName();
        
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("User image not found in folder");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //delete user
        userRepository.delete(user);
        
    }
    
    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        //page start at 0
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy)).descending() : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);
        return pageableResponse;
    }
    
    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not Found with given id"));
        return entityToDto(user);
    }
    
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User not found with this email"));
        
        return entityToDto(user);
    }
    
    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }
    
    private UserDto entityToDto(User savedUser) {
//        UserDto userDto= UserDto.builder().userId(savedUser.getUserId()).
//                name(savedUser.getName()).
//                email(savedUser.getEmail()).
//                password(savedUser.getPassword()).
//                about(savedUser.getAbout()).
//                gender(savedUser.getGender()).imageName(savedUser.getImageName()).build();
        return mapper.map(savedUser, UserDto.class);
    }
    
    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder().userId(userDto.getUserId()).
//                name(userDto.getName()).
//                email(userDto.getEmail()).
//                password(userDto.getPassword()).
//                about(userDto.getAbout()).
//                gender(userDto.getGender()).imageName(userDto.getImageName()).build();
        return mapper.map(userDto, User.class);
    }
}
