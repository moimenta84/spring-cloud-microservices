package com.example.cloud.msvc.cursos.repositories;

import com.example.cloud.msvc.cursos.models.entity.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course,Long> {
    @Modifying
    @Query("delete from CoursesUsers c where c.userId = ?1")
    void deleteByUserId(Long id);
}
