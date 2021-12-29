package persistence.dao;

import persistence.dto.DepartmentDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {
    private final DataSource ds;
    public DepartmentDAO(DataSource ds){
        this.ds = ds;
    }
    public List<DepartmentDTO> findAllDepartments(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM DEPARTMENT";
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            while(rs.next()) {
                DepartmentDTO departmentDTO = new DepartmentDTO();

                int departmentNumber =rs.getInt("department_number");
                String departmentName = rs.getString("department_name");

                departmentDTO.setDepartmentNumber(departmentNumber);
                departmentDTO.setDepartmentName(departmentName);

                departmentDTOS.add(departmentDTO);

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
        return departmentDTOS;
    }

    public int insertProfessor(DepartmentDTO departmentDTO){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO DEPARTMENT VALUES (?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, departmentDTO.getDepartmentNumber());
            stmt.setString(2, departmentDTO.getDepartmentName());

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

    public int updateDepartmentByDepartmentNumber(int departmentNumber, String departmentName){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "UPDATE PROFESSOR SET department_name =? WHERE department_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, departmentName);
            stmt.setInt(2, departmentNumber);

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

    public int deleteDepartment(int departmentNumber){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "DELETE  Department where department_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, departmentNumber);

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

    public int selectDepartmentNumer(String departmentName){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet= null;
        int result = 0;

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "SELECT department_number FROM department WHERE department_name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, departmentName);

            resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                result = resultSet.getInt("department_number");
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
                if(conn != null && !stmt.isClosed()){
                    stmt.close();
                }
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
                if(conn != null && !resultSet.isClosed()){
                    resultSet.close();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        return result;
    }
}
