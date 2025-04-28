package org.polina.practice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.polina.practice.entity.User;
import org.polina.practice.exception.UserNotFoundException;
import org.polina.practice.repository.UserRepository;
import org.polina.practice.service.UserService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(MessageFormat
                        .format("Пользователь с ID {0} не найден!", id)));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User updatedUser = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(MessageFormat
                        .format("Пользователь с ID {0} не найден!", id)));
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        return userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
