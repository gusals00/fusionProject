package service;

import persistence.dao.LectureDAO;
import persistence.dao.SyllabusDAO;
import persistence.dto.OpenLectureDTO;
import persistence.dto.SyllabusDTO;

public class SyllabusService {
    private final SyllabusDAO syllabusDAO;

    public SyllabusService(SyllabusDAO syllabusDAO){
        this.syllabusDAO = syllabusDAO;
    }

    public synchronized int insertSyllabus(SyllabusDTO syllabusDTO){// 강의 계획서 입력

        return syllabusDAO.insertSyllabus(syllabusDTO);
    }

    public boolean isSyllabusExist(OpenLectureDTO openLectureDTO){//강의 계획서 존재 여부
        return syllabusDAO.isSyllabusExist(openLectureDTO.getOpenLectureId());
    }

    public synchronized int updateSyllabus(SyllabusDTO syllabusDTO){// 강의 계획서 변경

        return syllabusDAO.updateSyllabus(syllabusDTO);
    }

    public SyllabusDTO findSyllabusByOpenLectureId(OpenLectureDTO openLectureDTO){//개설 교과목으로 강의 계획서 찾기
        int openLectureId=openLectureDTO.getOpenLectureId();
        return syllabusDAO.findSyllabusByOpenLectureId(openLectureId);
    }

}
