package com.example.cloud.msvc.cursos.controllers;

import com.example.cloud.msvc.cursos.models.entity.Course;
import com.example.cloud.msvc.cursos.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class CourseController {


    @Autowired
    private CourseService service;


    @GetMapping("/")
    public List<Course> index(){
        return service.index();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){

        Optional<Course> userOptional = service.byId(id);
        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userOptional.get());

    }

    @PostMapping
    public ResponseEntity<?> store(@RequestBody Course course){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Course course,@PathVariable Long id){

        Optional<Course> courseOptional = service.byId(id);

        if(courseOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        Course courseDb = courseOptional.get();
        courseDb.setName(course.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> destroy(@PathVariable Long id){

        Optional<Course> userOptional = service.byId(id);
        if(userOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }



}
