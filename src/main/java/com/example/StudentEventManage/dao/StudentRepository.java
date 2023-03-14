package com.example.StudentEventManage.dao;

import com.example.StudentEventManage.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Integer> {
}
