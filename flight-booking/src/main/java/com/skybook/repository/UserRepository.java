package com.skybook.repository;

import com.skybook.dto.UserDTO;
import com.skybook.model.User;
import com.skybook.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    
    private final FileStorageService fileStorageService;
    private static final String USERS_FILE = "users";
    
    public User save(User user) {
        List<UserDTO> dtos = fileStorageService.loadList(USERS_FILE, UserDTO.class);
        
        if (user.getId() == null) {
            user.setId(generateId(dtos));
        }
        
        UserDTO newDto = UserDTO.fromUser(user);
        
        // Remove old version if updating
        dtos = dtos.stream()
                .filter(dto -> !dto.getId().equals(user.getId()))
                .collect(Collectors.toList());
        
        dtos.add(newDto);
        fileStorageService.save(USERS_FILE, dtos);
        return user;
    }
    
    public Optional<User> findById(Long id) {
        return fileStorageService.loadList(USERS_FILE, UserDTO.class).stream()
                .filter(dto -> dto.getId().equals(id))
                .map(UserDTO::toUser)
                .findFirst();
    }
    
    public Optional<User> findByEmail(String email) {
        return fileStorageService.loadList(USERS_FILE, UserDTO.class).stream()
                .map(UserDTO::toUser)
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }
    
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
    
    public List<User> findAll() {
        return fileStorageService.loadList(USERS_FILE, UserDTO.class).stream()
                .map(UserDTO::toUser)
                .collect(Collectors.toList());
    }
    
    public void delete(User user) {
        deleteById(user.getId());
    }
    
    public void deleteById(Long id) {
        List<UserDTO> dtos = fileStorageService.loadList(USERS_FILE, UserDTO.class).stream()
                .filter(dto -> !dto.getId().equals(id))
                .collect(Collectors.toList());
        fileStorageService.save(USERS_FILE, dtos);
    }
    
    private Long generateId(List<UserDTO> dtos) {
        return dtos.stream()
                .mapToLong(UserDTO::getId)
                .max()
                .orElse(0L) + 1;
    }
}