package com.example.cloud.msvc.cursos.services;

import com.example.cloud.msvc.cursos.models.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {

    List<Course> index();
    Optional<Course> byId(Long id);
    Course save(Course course);
    void delete(Long id);
}
