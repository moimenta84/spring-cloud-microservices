package com.springcloud.msvc.usuarios.services;

import com.springcloud.msvc.usuarios.models.entity.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService{
    @Override
    public List<User> index() {
        return List.of();
    }

    @Override
    public Optional<User> byId(Long id) {
        return Optional.empty();
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
