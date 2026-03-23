package com.example.cloud.msvc.cursos.services;

import com.example.cloud.msvc.cursos.clients.UserClientRest;
import com.example.cloud.msvc.cursos.models.entity.Course;
import com.example.cloud.msvc.cursos.models.entity.CoursesUsers;
import com.example.cloud.msvc.cursos.models.entity.User;
import com.example.cloud.msvc.cursos.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CourseService implementation.
 * Handles local CRUD operations via JPA and inter-service communication
 * with msvc-users through a Feign client.
 */
@Service
public class CourserServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repository;

    /** Feign client — proxies HTTP calls to msvc-users (localhost:8001) */
    @Autowired
    private UserClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Course> index() {
        return (List<Course>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> byId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return repository.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    /**
     * Assigns an existing user to a course.
     * 1. Finds the course locally by courseId.
     * 2. Fetches the user from msvc-users via Feign (GET /{id}).
     * 3. Creates a CoursesUsers join record and persists it.
     */
    @Override
    @Transactional
    public Optional<User> assignUser(User user, Long courseId) {

        Optional<Course> o = repository.findById(courseId);

        if(o.isPresent()) {

            User usuarioMsvc = client.show(user.getId());

            Course course = o.get();
            CoursesUsers coursersUsers = new CoursesUsers();
            coursersUsers.setUserId(usuarioMsvc.getId());

            course.addCourseUser(coursersUsers);
            repository.save(course);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    /**
     * Creates a new user in msvc-users and enrols them in a course.
     * 1. Finds the course locally by courseId.
     * 2. Creates the user in msvc-users via Feign (POST /).
     * 3. Creates a CoursesUsers join record and persists it.
     */
    @Override
    @Transactional
    public Optional<User> createUser(User user, Long courseId) {

        Optional<Course> o = repository.findById(courseId);

        if(o.isPresent()) {

            User usuarioNewMsvc = client.store(user);

            Course course = o.get();
            CoursesUsers coursersUsers = new CoursesUsers();
            coursersUsers.setUserId(usuarioNewMsvc.getId());

            course.addCourseUser(coursersUsers);
            repository.save(course);
            return Optional.of(usuarioNewMsvc);
        }
        return Optional.empty();
    }

    /**
     * Removes a user assignment from a course.
     * 1. Finds the course locally by courseId.
     * 2. Fetches the user from msvc-users via Feign to confirm it exists.
     * 3. Removes the CoursesUsers join record and persists the change.
     */
    @Override
    @Transactional
    public Optional<User> desAssignUser(User user, Long courseId) {

        Optional<Course> o = repository.findById(courseId);

        if(o.isPresent()) {

            User usuarioMsvc = client.show(user.getId());

            Course course = o.get();
            CoursesUsers coursersUsers = new CoursesUsers();
            coursersUsers.setUserId(usuarioMsvc.getId());

            course.removeCourseUser(coursersUsers);
            repository.save(course);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> forIdUser(Long id) {
        Optional<Course> o = repository.findById(id);
        if (o.isPresent()) {
            Course course = o.get();
            if (!course.getCourseUsers().isEmpty()) {
                List<Long> ids = course.getCourseUsers()
                        .stream()
                        .map(CoursesUsers::getUserId)
                        .collect(Collectors.toList());

                List<User> users = client.getUsersByCourse(ids);
                course.setUsers(users);
            }
            return Optional.of(course);
        }
        return Optional.empty();
    }
}
