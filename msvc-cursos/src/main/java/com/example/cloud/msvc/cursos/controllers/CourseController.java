package com.example.cloud.msvc.cursos.controllers;

import com.example.cloud.msvc.cursos.models.entity.Course;
import com.example.cloud.msvc.cursos.models.entity.User;
import com.example.cloud.msvc.cursos.services.CourseService;
import feign.FeignException;
import org.springframework.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// REST controller that exposes CRUD endpoints for Course management
@RestController
@RequestMapping
public class CourseController {

    // Injects the course service to handle business logic
    @Autowired
    private CourseService service;

    // Returns the list of all courses
    @GetMapping("/")
    public List<Course> index(){
        return service.index();
    }

    // Returns a single course by its ID, or 404 if not found
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){

        Optional<Course> userOptional = service.forIdUser(id);
        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userOptional.get());
    }

    // Creates a new course after validating the request body
    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody Course course, BindingResult result){

        if(result.hasErrors()){
            return validated(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(course));
    }

    // Updates an existing course by ID after validating the request body
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Course course, BindingResult result, @PathVariable Long id){

        Optional<Course> courseOptional = service.byId(id);
        if(result.hasErrors()){
            return validated(result);
        }

        if(courseOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Course courseDb = courseOptional.get();
        courseDb.setName(course.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDb));
    }

    // Deletes a course by ID, returns 404 if not found or 204 on success
    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id){

        Optional<Course> userOptional = service.byId(id);
        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Assigns an existing user to a course by course ID, returns 404 if user not found in msvc-users
    @PutMapping("/assigned-user/{courseId}")
    public ResponseEntity<?> assignedUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> userOptional;

        try {
            userOptional = service.assignUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "No existe usuario en msvc-users: " + e.getMessage()));
        }

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
    }

    // Creates a new user and associates it with a course by course ID via msvc-users
    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> userOptional;

        try {
            userOptional = service.createUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "No se pudo crear el usuario: " + e.getMessage()));
        }

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.get());
    }

    // Removes a user from a course by course ID, communicates with msvc-users via Feign
    @DeleteMapping("/delete-user/{courseId}")
    public ResponseEntity<?> deleteUser(@RequestBody User user, @PathVariable Long courseId) {
        Optional<User> userOptional;
        try {
            userOptional = service.desAssignUser(user, courseId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "No se pudo eliminar el usuario: " + e.getMessage()));
        }

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    // Builds a map of field validation errors and returns a 400 Bad Request response
    private static @Nullable ResponseEntity<Map<String, String>> validated(BindingResult result) {
        if(result.hasErrors()){
            Map<String,String> errors = new HashMap<>();
            result.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }
}
