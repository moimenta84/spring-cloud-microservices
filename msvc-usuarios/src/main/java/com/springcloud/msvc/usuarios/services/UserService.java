package com.springcloud.msvc.usuarios.services;

import com.springcloud.msvc.usuarios.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List <User>index();
    Optional<User>byId(Long id);
    User save(User user);
    void delete(Long id);
    Optional<User>byEmail(String email);

}
