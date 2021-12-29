package service;


import persistence.dao.DepartmentDAO;
import persistence.dto.DepartmentDTO;

public class DepartmentService {
    private final DepartmentDAO departmentDAO;

    public DepartmentService(DepartmentDAO departmentDAO){
        this.departmentDAO = departmentDAO;
    }

    public synchronized int insertDepartment(DepartmentDTO departmentDTO){ // 학과 삽입
        return departmentDAO.insertProfessor(departmentDTO);
    }

    public int selectDepartmentNumber(String departmentName){// 학과 이름으로 학과 번호 찾기
        return departmentDAO.selectDepartmentNumer(departmentName);
    }
}
