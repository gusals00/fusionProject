package service;

import persistence.dao.ProfessorDAO;
import persistence.dto.AdminDTO;
import persistence.dto.ProfessorDTO;

import java.util.List;

public class ProfessorService {
    private final ProfessorDAO professorDAO;

    public ProfessorService(ProfessorDAO professorDAO){
        this.professorDAO = professorDAO;
    }

    public int insertProfessor(ProfessorDTO professorDTO){//교수 삽입
        return professorDAO.insertProfessor(professorDTO);
    }

    public List<ProfessorDTO> readAllProfessors(){//전체 교수 조회
        List<ProfessorDTO> allProfessors = professorDAO.findAllProfessors();
        return allProfessors;
    }

    public synchronized int updatePhoneNumber(ProfessorDTO professorDTO){//교수 전화번호 변경

        String professorId=professorDTO.getProfessorId();
        String professorPhoneNumber=professorDTO.getProfessorPhoneNumber();
        return professorDAO.updateProfessorByProfessorPhoneNumber(professorId,professorPhoneNumber);

    }

    public boolean isLoginOk(ProfessorDTO professorDTO){return professorDAO.isLoginOk(professorDTO);}

    public ProfessorDTO findProfessorById(ProfessorDTO professorDTO){return professorDAO.findProfessor(professorDTO.getProfessorId());}

    public ProfessorDTO findByName(String findProfessorName){
        return professorDAO.findByName(findProfessorName);
    }

    public synchronized int updatePw(ProfessorDTO professorDTO){//교수 패스워드 변경

        String professorId=professorDTO.getProfessorId();
        String professorPw=professorDTO.getProfessorPw();
        return professorDAO.updateProfessorByProfessorPw(professorId,professorPw);
    }

    public synchronized int updateName(ProfessorDTO professorDTO){//교수 이름 변경

        String professorId=professorDTO.getProfessorId();
        String professorName=professorDTO.getProfessorName();
        return professorDAO.updateProfessorByProfessorName(professorId,professorName);
    }

    public synchronized int updateSSN(ProfessorDTO professorDTO){//교수 주민번호 변경

        String professorId=professorDTO.getProfessorId();
        String ssn = professorDTO.getSsn();
        return professorDAO.updateProfessorBySSN(professorId,ssn);
    }

    public synchronized int updateEmail(ProfessorDTO professorDTO){//교수 이메일 변경

        String professorId=professorDTO.getProfessorId();
        String eMail = professorDTO.getEMail();
        return professorDAO.updateProfessorByEMail(professorId,eMail);
    }

}
