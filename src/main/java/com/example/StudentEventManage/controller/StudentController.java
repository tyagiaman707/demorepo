package com.example.StudentEventManage.controller;

import com.example.StudentEventManage.dao.StudentRepository;
import com.example.StudentEventManage.exception.ResourceNotFoundException;
import com.example.StudentEventManage.model.Student;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {
    @Autowired
    StudentRepository studentRepository;
    @PostMapping("/add-student")
    public ResponseEntity<String> addStudent(@RequestBody Student student){
        JSONObject jsonObject= new JSONObject(student);
        List<String> validationList = validateStudent(jsonObject);

        if(validationList.isEmpty()){
            Student student1 = setStudent(jsonObject);
            studentRepository.save(student1);
            return new ResponseEntity<>("Student created",HttpStatus.CREATED);
        }
        else {
            String[] answer = Arrays.copyOf(validationList.toArray(),validationList.size(),String[].class);
            return new ResponseEntity<>("Please pass these mandatory parameters-"+Arrays.toString(answer),HttpStatus.BAD_REQUEST);
        }
    }

    private Student setStudent(JSONObject jsonObject) {
        Student student= new Student();
        if(jsonObject.has("firstName")){
            String fname= jsonObject.getString("firstName");
            student.setFirstName(fname);
        }
        if(jsonObject.has("phoneNumber")){
            String number = jsonObject.getString("phoneNumber");
            student.setPhoneNumber(number);
        }
        if(jsonObject.has("lastName")){
            String lname = jsonObject.getString("lastName");
            student.setLastName(lname);
        }
        return student;
    }

    private List<String> validateStudent(JSONObject jsonObject) {
        List<String> errorlist= new ArrayList<>();
        if(!jsonObject.has("firstName")){
            errorlist.add("firstName");
        }
        if(!jsonObject.has("lastName")){
            errorlist.add("lastName");
        }
        if(!jsonObject.has("phoneNumber")){
            errorlist.add("phoneNumber");
        }
        return errorlist;
    }

    @GetMapping("/find-all-student")
    public List<Student> getAllStudent(){
        List studentList = studentRepository.findAll();
        return studentList;
    }
    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id){
        Student student = studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not exist with:"+id));
        return ResponseEntity.ok(student);
    }
    @PutMapping("/updateDepartment/{id}")
    public ResponseEntity<Student> updateDepartment (@PathVariable int id , @RequestBody Student student){
        Student student1 = studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not exist with:"+id));
        student1.setDepartment(student.getDepartment());
        return ResponseEntity.ok(student1);
    }
    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable int id){
        Student student1 = studentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Student not exist with:"+id));
        studentRepository.delete(student1);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
