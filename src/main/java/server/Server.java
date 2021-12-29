package server;

import controller.AdminController;
import controller.LoginController;
import controller.ProfessorController;
import controller.StudentController;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.PooledDataSource;
import persistence.dao.*;
import service.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {

        final DataSource ds = PooledDataSource.getDataSource();
        //connection factory
        SqlSessionFactory sqlSessionFactory = MyBatisConnectionFactory.getSqlSessionFactory();


        // service
        AdminService adminService=new AdminService(new AdminDAO(ds));
        ProfessorService professorService=new ProfessorService(new ProfessorDAO(ds));
        StudentService studentService=new StudentService(new StudentDAO(ds));
        DepartmentService departmentService=new DepartmentService(new DepartmentDAO(ds));
        OpenLectureService openLectureService = new OpenLectureService(new OpenLectureDAO(sqlSessionFactory));
        LectureHistoryService  lectureHistoryService = new LectureHistoryService(new LectureHistoryDAO(sqlSessionFactory));
        SyllabusService syllabusService = new SyllabusService(new SyllabusDAO(sqlSessionFactory));
        SyllabusWeekInfoService syllabusWeekInfoService=new SyllabusWeekInfoService(new SyllabusWeekInfoDAO(sqlSessionFactory));
        LectureRoomByService lectureRoombyService=new LectureRoomByService(new LectureRoomByTimeDAO(sqlSessionFactory));
        LectureTimeService lectureTimeService=new LectureTimeService(new LectureTimeDAO(sqlSessionFactory));
        LectureRoomService lectureRoomService=new LectureRoomService(new LectureRoomDAO(sqlSessionFactory));
        LectureService lectureService=new LectureService(new LectureDAO(sqlSessionFactory));
        PeriodService periodService=new PeriodService(new PeriodDAO(sqlSessionFactory));
        LectureRoomByService lectureRoomByService=new LectureRoomByService(new LectureRoomByTimeDAO(sqlSessionFactory));

        //controller
        AdminController adminController = new AdminController(professorService, departmentService, studentService, lectureService, openLectureService,periodService,lectureRoombyService,lectureRoomService,lectureTimeService);
        StudentController studentController=new StudentController(studentService,professorService,openLectureService,lectureHistoryService,syllabusService,lectureRoombyService,lectureTimeService,lectureRoomService,syllabusWeekInfoService,lectureService,periodService);
        ProfessorController professorController=new ProfessorController(professorService,openLectureService,syllabusService,lectureRoomByService,lectureHistoryService,studentService,lectureRoomService,lectureTimeService,lectureService,syllabusWeekInfoService,periodService);


        ServerSocket ss=new ServerSocket();
        ss.setReuseAddress(true);
        InetAddress address=InetAddress.getLocalHost();
        SocketAddress addr=new InetSocketAddress(5000);
        ss.bind(addr);

        Socket s=null;
        System.out.println("클라이언트 접속을 대기중...");

        while(true){
            s=ss.accept();
            System.out.println("New client request received : " + s);

            System.out.println("Creating a new handler for this client...");

            ClientHandler handler = new ClientHandler(s,adminController,studentController,professorController);
            Thread thread = new Thread(handler);
            thread.start();// client handler 실행
        }

    }
}
