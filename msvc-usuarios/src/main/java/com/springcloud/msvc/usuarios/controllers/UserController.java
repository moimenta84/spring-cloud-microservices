package com.springcloud.msvc.usuarios.controllers;

import com.springcloud.msvc.usuarios.models.entity.User;
import com.springcloud.msvc.usuarios.services.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/")
    public List<User>index(){
        return service.index();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){

        Optional<User> userOptional = service.byId(id);
        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userOptional.get());

    }

    @PostMapping
    public ResponseEntity<?> store(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody User user,@PathVariable Long id){

        Optional<User> userOptional = service.byId(id);

        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

      User userDb = userOptional.get();
      userDb.setName(user.getName());
      userDb.setEmail(user.getEmail());
      userDb.setPassword(user.getPassword());
      return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userDb));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id){

        Optional<User> userOptional = service.byId(id);
        if(userOptional.isEmpty()){
          return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
