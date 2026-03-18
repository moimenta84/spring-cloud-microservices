package com.example.cloud.msvc.cursos.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

// JPA entity mapped to the "courses" table
@Entity
@Table(name="courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Course name, cannot be blank
    @NotBlank
    @Column(name="name")
    private String name;

    // One course can have many enrolled users; cascade handles insert/delete of join records
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id")
    private List<CoursesUsers> courseUsers;

    // Transient: populated at runtime from msvc-users, not persisted in this database
    @Transient
    private List<User> users;

    public Course() {
        courseUsers = new ArrayList<>();
        users = new ArrayList<>();
    }

    public Course(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Adds a user enrollment record to the course
    public void addCourseUser(CoursesUsers courseUser){
        courseUsers.add(courseUser);
    }

    // Removes a user enrollment record from the course
    public void removeCourseUser(CoursesUsers courseUser){
        courseUsers.remove(courseUser);
    }

    public List<CoursesUsers> getCourseUsers() {
        return courseUsers;
    }

    public void setCourseUsers(List<CoursesUsers> courseUsers) {
        this.courseUsers = courseUsers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
