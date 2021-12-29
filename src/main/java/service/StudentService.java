package service;


import persistence.dao.StudentDAO;
import persistence.dto.StudentDTO;

import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(StudentDAO studentDAO){
        this.studentDAO = studentDAO;
    }

    public int insertStudent(StudentDTO studentDTO){//학생 삽입
        return studentDAO.insertStudent(studentDTO);
    }

    public List<StudentDTO> readAllStudents(){//전체 학생 조회
        List<StudentDTO> allStudents = studentDAO.findAllStudents();

        return allStudents;
    }

    public synchronized int updateStudentName(StudentDTO studentDTO){//학생 이름 변경
        String studentId=studentDTO.getStudentId();
        String studentName=studentDTO.getStudentName();
        return studentDAO.updateStudentByStudentName(studentId,studentName);
    }
    public synchronized int updateStudentPw(StudentDTO studentDTO){//학생 비밀번호 변경
        String studentId=studentDTO.getStudentId();
        String studentPw=studentDTO.getStudentPw();
        return studentDAO.updateStudentbyPw(studentId,studentPw);
    }
    public StudentDTO findStudentByStudentId(String studentId){// studentId로 StudentDTO 찾아서 리턴
        return studentDAO.findStudent(studentId);
    }

    public boolean isLoginOk(StudentDTO studentDTO){// id,pw로 로그인
        return studentDAO.isLoginOk(studentDTO);
    }

}
