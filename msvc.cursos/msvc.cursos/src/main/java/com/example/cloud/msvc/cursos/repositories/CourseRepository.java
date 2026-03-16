package com.example.cloud.msvc.cursos.repositories;

import com.example.cloud.msvc.cursos.models.entity.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course,Long> {
}
