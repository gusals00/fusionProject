package persistence.dao;

import persistence.dto.DepartmentDTO;
import persistence.dto.ProfessorDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {
    private final DataSource ds;
    public ProfessorDAO(DataSource ds){
        this.ds = ds;
    }

    public List<ProfessorDTO> findAllProfessors(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM PROFESSOR,DEPARTMENT WHERE PROFESSOR.department_number = DEPARTMENT.department_number";
        List<ProfessorDTO> professorDTOS = new ArrayList<>();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            while(rs.next()) {
                ProfessorDTO professorDTO = new ProfessorDTO();

                String professorId =rs.getString("professor_id");
                String professorPw = rs.getString("professor_pw");
                String professorName = rs.getString("professor_name");
                String professorSsn = rs.getString("SSN");
                String professorEmail = rs.getString("e_mail");
                String professorPhoneNumber = rs.getString("professor_phone_number");
                int departmentNumber=rs.getInt("department_number");
                String departmentName = rs.getString("department_name");

                departmentDTO.setDepartmentName(departmentName);
                departmentDTO.setDepartmentNumber(departmentNumber);

                professorDTO.setProfessorId(professorId);
                professorDTO.setProfessorPw(professorPw);
                professorDTO.setProfessorName(professorName);
                professorDTO.setSsn(professorSsn);
                professorDTO.setEMail(professorEmail);
                professorDTO.setProfessorPhoneNumber(professorPhoneNumber);
                professorDTO.setDepartmentNumber(departmentNumber);
                professorDTO.setDepartmentDTO(departmentDTO);

                professorDTOS.add(professorDTO);
                conn.commit();
            }

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
        return professorDTOS;
    }

    public int insertProfessor(ProfessorDTO professorDTO){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO PROFESSOR VALUES (?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, professorDTO.getProfessorId());
            stmt.setString(2, professorDTO.getProfessorPw());
            stmt.setString(3, professorDTO.getProfessorName());
            stmt.setString(4, professorDTO.getSsn());
            stmt.setString(5, professorDTO.getEMail());
            stmt.setString(6, professorDTO.getProfessorPhoneNumber());
            stmt.setInt(7, professorDTO.getDepartmentNumber());

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

    public int updateProfessorByProfessorPhoneNumber(String professorId, String professorPhoneNumber){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "UPDATE PROFESSOR SET professor_phone_number =? WHERE professor_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, professorPhoneNumber);
            stmt.setString(2, professorId);

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
    public int updateProfessorByProfessorPw(String professorId, String professorPw){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "UPDATE PROFESSOR SET professor_pw =? WHERE professor_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, professorPw);
            stmt.setString(2, professorId);

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

    public int updateProfessorByProfessorName(String professorId, String professorName){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "UPDATE PROFESSOR SET professor_name =? WHERE professor_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, professorName);
            stmt.setString(2, professorId);

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

    public int updateProfessorBySSN(String professorId, String ssn){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "UPDATE PROFESSOR SET SSN =? WHERE professor_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, ssn);
            stmt.setString(2, professorId);

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

    public int updateProfessorByEMail(String professorId, String eMail){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "UPDATE PROFESSOR SET e_mail =? WHERE professor_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, eMail);
            stmt.setString(2, professorId);

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




    public int deleteProfessor(String professorId){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "DELETE  Professor where professor_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, professorId);

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

    public ProfessorDTO findProfessor(String professorId){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM PROFESSOR,DEPARTMENT WHERE PROFESSOR.department_number = DEPARTMENT.department_number";
        ProfessorDTO professorDTO = new ProfessorDTO();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            while(rs.next()) {
                String tempProfessorId =rs.getString("professor_id");

                if (professorId .equals(tempProfessorId) ){
                    String professorPw = rs.getString("professor_pw");
                    String professorName = rs.getString("professor_name");
                    String professorSsn = rs.getString("SSN");
                    String professorEmail = rs.getString("e_mail");
                    String professorPhoneNumber = rs.getString("professor_phone_number");
                    int departmentNumber=rs.getInt("department_number");
                    String departmentName = rs.getString("department_name");

                    departmentDTO.setDepartmentName(departmentName);
                    departmentDTO.setDepartmentNumber(departmentNumber);

                    professorDTO.setProfessorId(professorId);
                    professorDTO.setProfessorPw(professorPw);
                    professorDTO.setProfessorName(professorName);
                    professorDTO.setSsn(professorSsn);
                    professorDTO.setEMail(professorEmail);
                    professorDTO.setProfessorPhoneNumber(professorPhoneNumber);
                    professorDTO.setDepartmentNumber(departmentNumber);
                    professorDTO.setDepartmentDTO(departmentDTO);
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
        return professorDTO;
    }

    public boolean isLoginOk(ProfessorDTO professorDTO){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT count(*) FROM PROFESSOR,DEPARTMENT WHERE PROFESSOR.department_number = DEPARTMENT.department_number" +
                " and professor_id='"+professorDTO.getProfessorId()+"' and professor_pw='"+professorDTO.getProfessorPw()+"'";
        int result=0;

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            if(rs.next()) {

                result=rs.getInt(1);

                conn.commit();
            }

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
        if (result==1)
                return true;
        return false;
    }

    public ProfessorDTO findByName(String findProfessorName){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM PROFESSOR,DEPARTMENT WHERE PROFESSOR.department_number = DEPARTMENT.department_number and professor_name='"+findProfessorName+"'";
        ProfessorDTO professorDTO = new ProfessorDTO();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            while(rs.next()) {
                String professorId =rs.getString("professor_id");
                String professorPw = rs.getString("professor_pw");
                String professorName = rs.getString("professor_name");
                String professorSsn = rs.getString("SSN");
                String professorEmail = rs.getString("e_mail");
                String professorPhoneNumber = rs.getString("professor_phone_number");
                int departmentNumber=rs.getInt("department_number");
                String departmentName = rs.getString("department_name");

                departmentDTO.setDepartmentName(departmentName);
                departmentDTO.setDepartmentNumber(departmentNumber);

                professorDTO.setProfessorId(professorId);
                professorDTO.setProfessorPw(professorPw);
                professorDTO.setProfessorName(professorName);
                professorDTO.setSsn(professorSsn);
                professorDTO.setEMail(professorEmail);
                professorDTO.setProfessorPhoneNumber(professorPhoneNumber);
                professorDTO.setDepartmentNumber(departmentNumber);
                professorDTO.setDepartmentDTO(departmentDTO);

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
        return professorDTO;
    }
}
