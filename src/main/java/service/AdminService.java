package service;

import persistence.dao.AdminDAO;
import persistence.dto.AdminDTO;

public class AdminService {
    private final AdminDAO adminDAO;

    public AdminService(AdminDAO adminDAO){
        this.adminDAO = adminDAO;
    }

    public synchronized int insertAdmin(AdminDTO adminDTO){//관리자 삽입
        return adminDAO.insertAdmin(adminDTO);
    }

    public boolean isLoginOk(AdminDTO adminDTO){// 로그인 가능 여부 호가인
        return adminDAO.isLoginOk(adminDTO);
    }

    public AdminDTO findAdminById(AdminDTO adminDTO){// id로 관리자 dto 찾기
        return adminDAO.findAdminById(adminDTO);
    }
}
