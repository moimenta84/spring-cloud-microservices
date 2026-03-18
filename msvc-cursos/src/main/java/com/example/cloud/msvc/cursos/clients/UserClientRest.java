package com.example.cloud.msvc.cursos.clients;

import com.example.cloud.msvc.cursos.models.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc_usuarios",url="localhost:8001")
public interface UserClientRest {

    @GetMapping("/{id}")
    User show(@PathVariable Long id);

    @PostMapping("/")
    User store(@RequestBody User user);



}
