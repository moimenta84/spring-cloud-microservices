package com.example.cloud.msvc.cursos.models.entity;

import jakarta.persistence.*;

// JPA entity that represents the join table between courses and users
@Entity
@Table(name="courses_users")
public class CoursesUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the ID of the user from msvc-users; unique to avoid duplicate enrollments
    @Column(name = "user_id", unique = true)
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Two CoursesUsers records are equal if they share the same userId
    @Override
    public boolean equals(Object obj) {

        if(this == obj){
            return true;
        }

        if(!(obj instanceof CoursesUsers)){
            return false;
        }
        CoursesUsers o = (CoursesUsers) obj;
        return this.userId != null && this.userId.equals(o.userId);
    }
}
