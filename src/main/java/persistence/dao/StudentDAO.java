package persistence.dao;

import persistence.dto.DepartmentDTO;
import persistence.dto.StudentDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private final DataSource ds;
    public StudentDAO(DataSource ds){
        this.ds = ds;
    }
    public List<StudentDTO> findAllStudents(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM STUDENT,DEPARTMENT WHERE STUDENT.department_number = DEPARTMENT.department_number";
        List<StudentDTO> studentDTOS = new ArrayList<>();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            while(rs.next()) {
                StudentDTO studentDTO = new StudentDTO();

                String studentId =rs.getString("student_id");
                String studentPw = rs.getString("student_pw");
                String studentName=rs.getString("student_name");
                String ssn=rs.getString("ssn");
                int studentLevel=rs.getInt("student_level");
                int departmentNumber=rs.getInt("department_number");
                String departmentName = rs.getString("department_name");

                departmentDTO.setDepartmentName(departmentName);
                departmentDTO.setDepartmentNumber(departmentNumber);

                studentDTO.setStudentId(studentId);
                studentDTO.setStudentPw(studentPw);
                studentDTO.setStudentName(studentName);
                studentDTO.setSsn(ssn);
                studentDTO.setStudentLevel(studentLevel);
                studentDTO.setDepartmentNumber(departmentNumber);
                studentDTO.setDepartmentDTO(departmentDTO);

                studentDTOS.add(studentDTO);

            }
            conn.commit();

        } catch(SQLException e){
            System.out.println("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }  finally{
            try{
                if(conn != null && !rs.isClosed()){
                    rs.close();
                }
                if(conn != null && !stmt.isClosed()){
                    stmt.close();
                }
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return studentDTOS;
    }

    public int insertStudent(StudentDTO studentDTO){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO STUDENT VALUES (?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, studentDTO.getStudentId());
            stmt.setString(2, studentDTO.getStudentPw());
            stmt.setString(3, studentDTO.getStudentName());
            stmt.setString(4, studentDTO.getSsn());
            stmt.setInt(5, studentDTO.getStudentLevel());
            stmt.setInt(6, studentDTO.getDepartmentNumber());

            count = stmt.executeUpdate();
            conn.commit();
        } catch(SQLException e){
            System.out.println("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }  finally{
            try{
                if(conn != null && !stmt.isClosed()){
                    stmt.close();
                }
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return count;
    }

    public int updateStudentByStudentName(String studentId, String studentName){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "UPDATE STUDENT SET student_name =? WHERE student_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentName);
            stmt.setString(2, studentId);

            count = stmt.executeUpdate();
            conn.commit();
        } catch(SQLException e){
            System.out.println("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }  finally{
            try{
                if(conn != null && !stmt.isClosed()){
                    stmt.close();
                }
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return count;
    }

    public int deleteStudent(String studentId){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "delete from STUDENT where student_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentId);

            count = stmt.executeUpdate();
            conn.commit();
        } catch(SQLException e){
            System.out.println("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }  finally{
            try{
                if(conn != null && !stmt.isClosed()){
                    stmt.close();
                }
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return count;
    }

    public StudentDTO findStudent(String studentId){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM STUDENT,DEPARTMENT WHERE STUDENT.department_number = DEPARTMENT.department_number";
        StudentDTO studentDTO = new StudentDTO();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            while(rs.next()) {
                String tempStudentId =rs.getString("student_id");

                if (studentId.equals(tempStudentId)) {
                    String studentPw = rs.getString("student_pw");
                    String studentName = rs.getString("student_name");
                    String ssn = rs.getString("ssn");
                    int studentLevel = rs.getInt("student_level");
                    int departmentNumber = rs.getInt("department_number");
                    String departmentName = rs.getString("department_name");

                    departmentDTO.setDepartmentName(departmentName);
                    departmentDTO.setDepartmentNumber(departmentNumber);

                    studentDTO.setStudentId(studentId);
                    studentDTO.setStudentPw(studentPw);
                    studentDTO.setStudentName(studentName);
                    studentDTO.setSsn(ssn);
                    studentDTO.setStudentLevel(studentLevel);
                    studentDTO.setDepartmentNumber(departmentNumber);
                    studentDTO.setDepartmentDTO(departmentDTO);
                    break;
                }

            }
            conn.commit();

        } catch(SQLException e){
            System.out.println("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }  finally{
            try{
                if(conn != null && !rs.isClosed()){
                    rs.close();
                }
                if(conn != null && !stmt.isClosed()){
                    stmt.close();
                }
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return studentDTO;
    }

    public boolean isLoginOk(StudentDTO studentDTO){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT count(*) FROM STUDENT,DEPARTMENT WHERE STUDENT.department_number = DEPARTMENT.department_number " +
                "and student_id='"+studentDTO.getStudentId()+"' and student_pw='"+studentDTO.getStudentPw()+"'";
        int result=0;

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            if(rs.next()) {

                result=rs.getInt(1);
            }
            conn.commit();

        } catch(SQLException e){
            System.out.println("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }  finally{
            try{
                if(conn != null && !rs.isClosed()){
                    rs.close();
                }
                if(conn != null && !stmt.isClosed()){
                    stmt.close();
                }
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        if(result==1)
            return true;
        return false;
    }
    public int updateStudentbyPw(String studentId,String studentPw){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "UPDATE STUDENT SET student_pw =? WHERE student_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentPw);
            stmt.setString(2, studentId);

            count = stmt.executeUpdate();
            conn.commit();
        } catch(SQLException e){
            System.out.println("error : " + e);
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }  finally{
            try{
                if(conn != null && !stmt.isClosed()){
                    stmt.close();
                }
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return count;
    }
}
