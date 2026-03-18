package com.example.cloud.msvc.cursos.services;

import com.example.cloud.msvc.cursos.models.entity.Course;
import com.example.cloud.msvc.cursos.models.entity.User;

import java.util.List;
import java.util.Optional;

// Service interface defining the business operations for course management
public interface CourseService {

    // Returns all courses
    List<Course> index();

    // Finds a course by its ID
    Optional<Course> byId(Long id);

    // Persists a new or existing course
    Course save(Course course);

    // Deletes a course by its ID
    void delete(Long id);

    // --- Methods that communicate with msvc-users microservice ---

    // Assigns an existing user (from msvc-users) to a course
    Optional<User> assignUser(User user, Long courseId);

    // Creates a new user in msvc-users and assigns it to a course
    Optional<User> createUser(User user, Long id);

    // Removes a user assignment from a course
    Optional<User> desAssignUser(User user, Long courseId);

}
