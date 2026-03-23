package com.springcloud.msvc.usuarios.controllers;

import com.springcloud.msvc.usuarios.models.entity.User;
import com.springcloud.msvc.usuarios.services.UserService;
import jakarta.validation.Valid;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

        // Check if email is not empty and already exists in DB
        if(!user.getEmail().isEmpty() &&  service.byEmail(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje","Ya existe un usuario con ese email"));
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
        // Check if email has changed and already belongs to another user
        if(!user.getEmail().isEmpty() && !user.getEmail().equalsIgnoreCase(userDb.getEmail())
                && service.byEmail(user.getEmail()).isPresent()){
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje","Ya existe un usuario con ese email"));
        }
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
    @GetMapping("users-Course")
    public ResponseEntity<List<User>> getUsersByCourse(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.indexForIds(ids));
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
