package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;


    public List<StudentCourse> getAllStudentsWithCourses() {

        List<Student> students = studentRepository.findAll();
        List<StudentCourse> allStudentCourses = studentCourseRepository.findAll();

        Map<Long, List<StudentCourse>> studentCourseMap = new HashMap<>();

        for (StudentCourse studentCourse : allStudentCourses) {
            Long studentId = studentCourse.getStudent().getId();
            studentCourseMap.computeIfAbsent(studentId, k -> new ArrayList<>()).add(studentCourse);
        }

        List<StudentCourse> studentCourses = new ArrayList<>();

        for (Student student : students) {
            List<StudentCourse> studentCoursesByStudent = studentCourseMap.getOrDefault(student.getId(), new ArrayList<>());
            for (StudentCourse studentCourseByStudent : studentCoursesByStudent) {
                studentCourseByStudent.setStudent(student);
                studentCourses.add(studentCourseByStudent);
            }
        }

        return studentCourses;
    }

    public Optional<Student> findStudentWithHighestGpa() {
        List<Student> students = studentRepository.findAll();
        Student highestGpaStudent = null;
        double highestGpa = 0.0;
        for (Student student : students) {
            if (student.getGpa() > highestGpa) {
                highestGpa = student.getGpa();
                highestGpaStudent = student;
            }
        }
        return Optional.ofNullable(highestGpaStudent);
    }

    public String joinStudentNames() {
        List<Student> students = studentRepository.findAll();
        StringBuilder resultBuilder = new StringBuilder();
        for (Student student : students) {
            resultBuilder.append(student.getName()).append(", ");
        }
         if (!students.isEmpty()) {
            resultBuilder.setLength(resultBuilder.length() - 2);
        }
        return resultBuilder.toString();
    }
}

