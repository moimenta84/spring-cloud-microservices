package com.example.cloud.msvc.cursos.services;

import com.example.cloud.msvc.cursos.models.entity.Course;
import com.example.cloud.msvc.cursos.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourserServiceImpl implements CourseService {

    @Autowired
    private CourseRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Course> index() {
        return  (List<Course>)repository.findAll();
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
}
