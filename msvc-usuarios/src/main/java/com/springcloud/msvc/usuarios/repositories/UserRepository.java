package com.springcloud.msvc.usuarios.repositories;

import com.springcloud.msvc.usuarios.models.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository  extends CrudRepository<User,Long>{

    Optional<User> findByEmail(String email);
}
