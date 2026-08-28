package com.library.service;

import com.library.entity.User;
import com.library.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User addUser(User user) {
        if (userRepository.countByEmpRollNo(user.getEmpRollNo()) > 0) {
            throw new RuntimeException("A user with emp/roll no " + user.getEmpRollNo() + " already exists.");
        }
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User existing = getUserById(id);
        existing.setName(userDetails.getName());
        existing.setUserType(userDetails.getUserType());
        existing.setEmpRollNo(userDetails.getEmpRollNo());
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
