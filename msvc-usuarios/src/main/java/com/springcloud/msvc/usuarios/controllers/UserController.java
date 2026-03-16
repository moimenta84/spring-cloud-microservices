package com.springcloud.msvc.usuarios.controllers;

import com.springcloud.msvc.usuarios.models.entity.User;
import com.springcloud.msvc.usuarios.services.UserService;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> store(@Valid @RequestBody User user, BindingResult result){

        if (result.hasErrors()){
            return validated(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result,@PathVariable Long id){

        if (result.hasErrors()){
            return validated(result);
        }
        
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

    private static @Nullable ResponseEntity<Map<String, String>>validated(BindingResult result) {
        if(result.hasErrors()){
            Map<String,String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }
}
