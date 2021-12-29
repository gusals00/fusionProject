package persistence.dao;

import org.apache.commons.dbcp2.DelegatingResultSet;
import persistence.dto.AdminDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class AdminDAO {
    private final DataSource ds;
    public AdminDAO(DataSource ds){
        this.ds = ds;
    }

    public List<AdminDTO> findAllAdmins(){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM ADMIN";
        List<AdminDTO> adminDTOS = new ArrayList<>();

        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            while(rs.next()) {
                AdminDTO adminDTO = new AdminDTO();

                String adminId =rs.getString("admin_id");
                String adminPw = rs.getString("admin_pw");

                adminDTO.setAdminId(adminId);
                adminDTO.setAdminPw(adminPw);

                adminDTOS.add(adminDTO);

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
        return adminDTOS;
    }

    public int insertAdmin(AdminDTO adminDTO){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = "INSERT INTO ADMIN VALUES (?,?)";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, adminDTO.getAdminId());
            stmt.setString(2, adminDTO.getAdminPw());

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

    public int deleteAdmin(String adminId){
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            String sql = null;

            sql = "delete from ADMIN where admin_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, adminId);

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

    public boolean isLoginOk(AdminDTO adminDTO){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT count(*) FROM ADMIN where admin_id='"+adminDTO.getAdminId()+"' and admin_pw = '"+adminDTO.getAdminPw()+"'";
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

    public AdminDTO findAdminById(AdminDTO adminDTO){
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String selectQuery = "SELECT * FROM ADMIN WHERE ADMIN_ID= '"+adminDTO.getAdminId()+"'";
        AdminDTO newAdminDTO=new AdminDTO();
        try{
            conn = ds.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectQuery);

            if(rs.next()) {

                String adminId =rs.getString("admin_id");
                String adminPw = rs.getString("admin_pw");

                newAdminDTO.setAdminId(adminId);
                newAdminDTO.setAdminPw(adminPw);

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
        return newAdminDTO;
    }
}
