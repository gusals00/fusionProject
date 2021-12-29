package service;

import persistence.dao.SyllabusWeekInfoDAO;
import persistence.dto.SyllabusWeekInfoDTO;

import java.util.List;

public class SyllabusWeekInfoService {
    private final SyllabusWeekInfoDAO syllabusWeekInfoDAO;

    public SyllabusWeekInfoService(SyllabusWeekInfoDAO syllabusWeekInfoDAO){
        this.syllabusWeekInfoDAO = syllabusWeekInfoDAO;
    }

    public synchronized int insertSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO){//주차별 강의 정보 입력
        return syllabusWeekInfoDAO.insertSyllabusWeekInfo(syllabusWeekInfoDTO);
    }

    public synchronized int updateSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO){//주차별 강의 정보 변경
        return syllabusWeekInfoDAO.updateSyllabusWeekInfo(syllabusWeekInfoDTO);
    }

    public boolean isExistSyllabusWeekInfo(int syllabusId,int syllabusWeek){//해당 주차별 강의 정보 존재 여부
        return syllabusWeekInfoDAO.isExistSyllabusWeekInfo(syllabusId,syllabusWeek);
    }

    public List<SyllabusWeekInfoDTO> findSyllabusWeekInfoBySyllabusId(int syllabusId){//강의 계획서 id로 주차별 강의 정보 조회
        return syllabusWeekInfoDAO.findSyllabusWeekInfoBySyllabusId(syllabusId);
    }

    public synchronized int deleteSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO){//주차별 강의 정보 삭제
        return syllabusWeekInfoDAO.deleteSyllabusWeekInfo(syllabusWeekInfoDTO);
    }

}
