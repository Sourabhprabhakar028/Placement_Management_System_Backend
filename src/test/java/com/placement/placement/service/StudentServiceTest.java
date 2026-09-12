package com.placement.placement.service;

import com.placement.placement.dto.request.StudentRequest;
import com.placement.placement.dto.response.StudentResponse;
import com.placement.placement.entity.Student;
import com.placement.placement.exception.StudentNotFoundException;
import com.placement.placement.repository.StudentRepository;
import com.placement.placement.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student;
    private StudentRequest studentRequest;

    @BeforeEach
    void setUp() {
        student = new Student(
                "Sourabh Prabhakar",
                "sourabh@gmail.com",
                "CSE",
                85.5
        );
        student.setId(1L);

        studentRequest = new StudentRequest();
        studentRequest.setName("Sourabh Prabhakar");
        studentRequest.setEmail("sourabh@gmail.com");
        studentRequest.setBranch("CSE");
        studentRequest.setPercentage(85.5);
    }

    @Test
    void saveStudent_Success() {
        when(studentRepository.existsByEmail(studentRequest.getEmail()))
                .thenReturn(false);
        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        StudentResponse response = studentService.saveStudent(studentRequest);

        assertNotNull(response);
        assertEquals("Sourabh Prabhakar", response.getName());
        assertEquals("sourabh@gmail.com", response.getEmail());
        assertEquals("CSE", response.getBranch());
        assertEquals(85.5, response.getPercentage());
        verify(studentRepository, times(1)).save(any(Student.class));
        System.out.println("✅ saveStudent_Success passed!");
    }

    @Test
    void saveStudent_DuplicateEmail_ThrowsException() {
        when(studentRepository.existsByEmail(studentRequest.getEmail()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.saveStudent(studentRequest)
        );

        assertEquals("Email already exists: sourabh@gmail.com",
                exception.getMessage());
        verify(studentRepository, never()).save(any(Student.class));
        System.out.println("✅ saveStudent_DuplicateEmail passed!");
    }

    @Test
    void getAllStudents_Success() {
        Student student2 = new Student("Rahul", "rahul@gmail.com", "IT", 88.0);
        student2.setId(2L);

        when(studentRepository.findAll())
                .thenReturn(List.of(student, student2));

        List<StudentResponse> responses = studentService.getAllStudents();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Sourabh Prabhakar", responses.get(0).getName());
        assertEquals("Rahul", responses.get(1).getName());
        System.out.println("✅ getAllStudents_Success passed!");
    }

    @Test
    void getStudentById_Success() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        StudentResponse response = studentService.getStudentById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Sourabh Prabhakar", response.getName());
        System.out.println("✅ getStudentById_Success passed!");
    }

    @Test
    void getStudentById_NotFound_ThrowsException() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception = assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudentById(99L)
        );

        assertEquals("Student not found with id 99",
                exception.getMessage());
        System.out.println("✅ getStudentById_NotFound passed!");
    }

    @Test
    void updateStudent_Success() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class)))
                .thenReturn(student);

        StudentResponse response = studentService.updateStudent(1L, studentRequest);

        assertNotNull(response);
        verify(studentRepository, times(1)).save(any(Student.class));
        System.out.println("✅ updateStudent_Success passed!");
    }

    @Test
    void deleteStudent_Success() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(any(Student.class));

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).delete(any(Student.class));
        System.out.println("✅ deleteStudent_Success passed!");
    }

    @Test
    void deleteStudent_NotFound_ThrowsException() {
        when(studentRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.deleteStudent(99L)
        );

        verify(studentRepository, never()).delete(any(Student.class));
        System.out.println("✅ deleteStudent_NotFound passed!");
    }
}