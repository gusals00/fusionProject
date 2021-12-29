package db_init;

import persistence.MyBatisConnectionFactory;
import persistence.PooledDataSource;
import persistence.dao.*;
import persistence.dto.*;
import service.*;

import javax.sql.DataSource;
import java.util.List;

public class Main {

    public static void main(String[] args){

        //DBINIT.SQL 실행 후 해당 JAVA 파일 실행. -> DB 초기화 완료됨.

        final DataSource ds = PooledDataSource.getDataSource();

        AdminDAO adminDAO = new AdminDAO(ds);
        AdminService adminService = new AdminService(adminDAO);
        ProfessorDAO professorDAO = new ProfessorDAO(ds);
        ProfessorService professorService = new ProfessorService(professorDAO);
        StudentDAO studentDAO = new StudentDAO(ds);
        StudentService studentService = new StudentService(studentDAO);
        DepartmentDAO departmentDAO = new DepartmentDAO(ds);
        DepartmentService departmentService = new DepartmentService(departmentDAO);
        LectureDAO lectureDAO = new LectureDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        LectureService lectureService = new LectureService(lectureDAO);
        OpenLectureDAO openLectureDAO = new OpenLectureDAO((MyBatisConnectionFactory.getSqlSessionFactory()));
        OpenLectureService openLectureService = new OpenLectureService(openLectureDAO);
        PeriodDAO periodDAO = new PeriodDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        PeriodService periodService=new PeriodService(periodDAO);
        LectureRoomByTimeDAO lectureRoomByTimeDAO = new LectureRoomByTimeDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        LectureRoomByService lectureRoomByService =new LectureRoomByService(lectureRoomByTimeDAO);
        LectureRoomDAO lectureRoomDAO = new LectureRoomDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        LectureTimeDAO lectureTimeDAO = new LectureTimeDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        LectureHistoryDAO lectureHistoryDAO=new LectureHistoryDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        LectureHistoryService lectureHistoryService=new LectureHistoryService(lectureHistoryDAO);
        SyllabusDAO syllabusDAO=new SyllabusDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        SyllabusService syllabusService = new SyllabusService(syllabusDAO);
        SyllabusWeekInfoDAO syllabusWeekInfoDAO=new SyllabusWeekInfoDAO(MyBatisConnectionFactory.getSqlSessionFactory());
        SyllabusWeekInfoService syllabusWeekInfoService=new SyllabusWeekInfoService(syllabusWeekInfoDAO);
        LectureRoomService lectureRoomService=new LectureRoomService(new LectureRoomDAO(MyBatisConnectionFactory.getSqlSessionFactory()));


        // 강의실 정보 추가
        for(int i=100;i<=500;i+=100){
            for(int j=1;j<41;j++){
                LectureRoomDTO lectureRoomDTO111 = new LectureRoomDTO(0,"D",i+j);
                lectureRoomService.insertRoom(lectureRoomDTO111);
            }
        }
        for(int i=100;i<=500;i+=100){
            for(int j=1;j<41;j++){
                LectureRoomDTO lectureRoomDTO111 = new LectureRoomDTO(0,"T",i+j);
                lectureRoomService.insertRoom(lectureRoomDTO111);
            }
        }
        for(int i=100;i<=500;i+=100){
            for(int j=1;j<41;j++){
                LectureRoomDTO lectureRoomDTO111 = new LectureRoomDTO(0,"G",i+j);
                lectureRoomService.insertRoom(lectureRoomDTO111);
            }
        }



        // ---------- 회원 CRU ------------ //

        // DTO 생성 //
        AdminDTO adminDTO = new AdminDTO("admin", "1234");
        DepartmentDTO departmentDTO = new DepartmentDTO(1, "컴소공");
        ProfessorDTO professorDTO = new ProfessorDTO("professor", "1234", "김선명", "123456-1234567", "a@naver.com", "010-1234-5678", 1, null);
        ProfessorDTO professorDTO2 = new ProfessorDTO("professor2", "1234", "김성렬", "123458-1234569", "b@naver.com", "010-5232-5678", 1, null);
        StudentDTO studentDTO = new StudentDTO("20180333", "990828", "김현민", "990828-1234567", 2, 1, null);
        StudentDTO studentDTO2 = new StudentDTO("20180004", "961128", "강대현", "961128-1234567", 2, 1, null);
        StudentDTO studentDTO3 = new StudentDTO("20180584", "990624", "성호창", "990624-1234567", 2, 1, null);
        StudentDTO studentDTO4 = new StudentDTO("20180089", "990128", "김광민", "990128-1234567", 2, 1, null);
        // 학과 삽입 //
        departmentService.insertDepartment(departmentDTO);

        // 관리자, 교수 , 학생 삽입 //
        adminService.insertAdmin(adminDTO);
        professorService.insertProfessor(professorDTO);
        professorService.insertProfessor(professorDTO2);
        studentService.insertStudent(studentDTO);
        studentService.insertStudent(studentDTO2);
        studentService.insertStudent(studentDTO3);
        studentService.insertStudent(studentDTO4);

        // ---------- 회원 READ ----------- //
        // 모든 교수 조회 //
        List<ProfessorDTO> professorDTOS = professorService.readAllProfessors();
        for(ProfessorDTO professorDTO1 : professorDTOS){
            System.out.println(professorDTO1.toString());
        }
        // 모든 학생 조회 //
        List<StudentDTO> studentDTOS = studentService.readAllStudents();
        for(StudentDTO studentDTO1 : studentDTOS){
            System.out.println(studentDTO1.toString());
        }



        // ------------ 교과목 CRU -------------- //

        //교과목 DTO 생성 //
        LectureDTO lectureDTO = new LectureDTO("CS0077", "C++프로그래밍", 2, 3);
        LectureDTO lectureDTO2 = new LectureDTO("CS0017", "운영체제", 2, 3);
        LectureDTO lectureDTO3 = new LectureDTO("CS0016", "컴퓨터네트워크", 2, 4);
        LectureDTO lectureDTO4 = new LectureDTO("CS0067", "융합프로젝트", 2, 2);
        LectureDTO lectureDTO5 = new LectureDTO("CS0080", "오픈소스소프트웨어", 2, 2);
        LectureDTO lectureDTO6 = new LectureDTO("CS0010", "자바프로그래밍", 1, 3);
        LectureDTO lectureDTO7 = new LectureDTO("CS0027", "디자인패턴", 3, 3);
        LectureDTO lectureDTO8 = new LectureDTO("CS0035", "컴파일러", 4, 3);


        //교과목 삽입//
        lectureService.insertLecture(lectureDTO);
        lectureService.insertLecture(lectureDTO2);
        lectureService.insertLecture(lectureDTO3);
        lectureService.insertLecture(lectureDTO4);
        lectureService.insertLecture(lectureDTO5);
        lectureService.insertLecture(lectureDTO6);
        lectureService.insertLecture(lectureDTO7);
        lectureService.insertLecture(lectureDTO8);

        //교과목 전체 조회//
        List<LectureDTO> lectureDTOS = lectureService.readAllLectures();
        for(LectureDTO lectureDTO1 : lectureDTOS){
            lectureDTO1.toString();
        }

        //교과목 학년별 조회//
        for(int i=1; i<=4; i++){
            System.out.println(i + "학년 -----------------");
            List<LectureDTO> lectureDTOS1 = lectureService.readLectureByLevel(i);
            for(LectureDTO lectureDTO1 : lectureDTOS1){
                System.out.println(lectureDTO1.toString());
            }
            System.out.println();
        }


        //개설교과목 DTO 생성//
        OpenLectureDTO openLectureDTO = new OpenLectureDTO(0,1,"CS0077","professor",3,0,null,null);
        OpenLectureDTO openLectureDTO2 = new OpenLectureDTO(0,1,"CS0017","professor",3,0,null,null);
        OpenLectureDTO openLectureDTO3 = new OpenLectureDTO(0,1,"CS0016","professor2",3,0,null,null);
        OpenLectureDTO openLectureDTO4 = new OpenLectureDTO(0,1,"CS0067","professor2",3,0,null,null);
        OpenLectureDTO openLectureDTO5 = new OpenLectureDTO(0,1,"CS0010","professor2",3,0,null,null);
        OpenLectureDTO openLectureDTO6 = new OpenLectureDTO(0,1,"CS0027","professor2",3,0,null,null);
        OpenLectureDTO openLectureDTO7 = new OpenLectureDTO(0,1,"CS0035","professor2",3,0,null,null);
        LectureTimeDTO lectureTimeDTO = new LectureTimeDTO(0,"월",1);
        LectureTimeDTO lectureTimeDTO2 = new LectureTimeDTO(0,"월",2);
        LectureTimeDTO lectureTimeDTO3 = new LectureTimeDTO(0,"월",3);
        LectureTimeDTO lectureTimeDTO4 = new LectureTimeDTO(0,"월",5);
        LectureTimeDTO lectureTimeDTO5 = new LectureTimeDTO(0,"월",6);
        LectureTimeDTO lectureTimeDTO6 = new LectureTimeDTO(0,"월",7);
        LectureTimeDTO lectureTimeDTO7 = new LectureTimeDTO(0,"화",1);
        LectureTimeDTO lectureTimeDTO8 = new LectureTimeDTO(0,"화",2);
        LectureTimeDTO lectureTimeDTO9 = new LectureTimeDTO(0,"화",3);
        LectureTimeDTO lectureTimeDTO11 = new LectureTimeDTO(0,"수",1);
        LectureTimeDTO lectureTimeDTO12 = new LectureTimeDTO(0,"수",2);
        LectureTimeDTO lectureTimeDTO13 = new LectureTimeDTO(0,"수",3);
        LectureTimeDTO lectureTimeDTO14 = new LectureTimeDTO(0,"수",4);
        LectureTimeDTO lectureTimeDTO15 = new LectureTimeDTO(0,"목",2);
        LectureTimeDTO lectureTimeDTO16 = new LectureTimeDTO(0,"목",3);
        LectureTimeDTO lectureTimeDTO17 = new LectureTimeDTO(0,"금",6);
        LectureTimeDTO lectureTimeDTO18 = new LectureTimeDTO(0,"금",7);
        LectureRoomDTO lectureRoomDTO = new LectureRoomDTO(0,"D",327);
        LectureRoomDTO lectureRoomDTO2 = new LectureRoomDTO(0,"T",330);
        LectureRoomDTO lectureRoomDTO3 = new LectureRoomDTO(0,"D",440);
        LectureRoomDTO lectureRoomDTO4 = new LectureRoomDTO(0,"T",312);

        openLectureService.insertOpenLecture(openLectureDTO, lectureTimeDTO, lectureRoomDTO);
        openLectureService.insertOpenLecture(openLectureDTO, lectureTimeDTO2, lectureRoomDTO);
        openLectureService.insertOpenLecture(openLectureDTO, lectureTimeDTO3, lectureRoomDTO);

        openLectureService.insertOpenLecture(openLectureDTO2, lectureTimeDTO5, lectureRoomDTO);
        openLectureService.insertOpenLecture(openLectureDTO2, lectureTimeDTO6, lectureRoomDTO);
        openLectureService.insertOpenLecture(openLectureDTO2, lectureTimeDTO7, lectureRoomDTO);

        openLectureService.insertOpenLecture(openLectureDTO3, lectureTimeDTO11, lectureRoomDTO3);
        openLectureService.insertOpenLecture(openLectureDTO3, lectureTimeDTO12, lectureRoomDTO3);
        openLectureService.insertOpenLecture(openLectureDTO3, lectureTimeDTO13, lectureRoomDTO3);
        openLectureService.insertOpenLecture(openLectureDTO3, lectureTimeDTO14, lectureRoomDTO3);

        openLectureService.insertOpenLecture(openLectureDTO4, lectureTimeDTO, lectureRoomDTO2);
        openLectureService.insertOpenLecture(openLectureDTO4, lectureTimeDTO2, lectureRoomDTO2);

        openLectureService.insertOpenLecture(openLectureDTO5, lectureTimeDTO15, lectureRoomDTO4);
        openLectureService.insertOpenLecture(openLectureDTO5, lectureTimeDTO16, lectureRoomDTO4);
        openLectureService.insertOpenLecture(openLectureDTO5, lectureTimeDTO17, lectureRoomDTO4);

        openLectureService.insertOpenLecture(openLectureDTO6, lectureTimeDTO5, lectureRoomDTO2);
        openLectureService.insertOpenLecture(openLectureDTO6, lectureTimeDTO6, lectureRoomDTO2);
        openLectureService.insertOpenLecture(openLectureDTO6, lectureTimeDTO7, lectureRoomDTO2);

        openLectureService.insertOpenLecture(openLectureDTO7, lectureTimeDTO8, lectureRoomDTO4);
        openLectureService.insertOpenLecture(openLectureDTO7, lectureTimeDTO9, lectureRoomDTO4);
        openLectureService.insertOpenLecture(openLectureDTO7, lectureTimeDTO18, lectureRoomDTO4);


        System.out.println("db 초기화를 완료했습니다.");

    }


}

