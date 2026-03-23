package com.example.cloud.msvc.cursos.clients;

import com.example.cloud.msvc.cursos.models.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios",url="localhost:8001")
public interface UserClientRest {

    @GetMapping("/{id}")
    User show(@PathVariable Long id);

    @PostMapping("/")
    User store(@RequestBody User user);

    @GetMapping("/users-Course")
    List<User> getUsersByCourse(@RequestParam List<Long> ids);

}
