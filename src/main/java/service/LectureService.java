package service;


import persistence.dao.LectureDAO;
import persistence.dto.LectureDTO;

import java.util.List;

public class LectureService {

    private final LectureDAO lectureDAO;

    public LectureService(LectureDAO lectureDAO){
        this.lectureDAO = lectureDAO;
    }

    public int insertLecture(LectureDTO lectureDTO){//교과목 삽입
        return lectureDAO.insertLecture(lectureDTO);
    }

    public List<LectureDTO> readAllLectures(){//모든 교과목 조회
        List<LectureDTO> allLecture = lectureDAO.findAllLecture();
        return allLecture;
    }

    public List<LectureDTO> readLectureByLevel(int level){//학년별 교과목 조회
        List<LectureDTO> lectureByLevel = lectureDAO.findLectureByLevel(level);
        return lectureByLevel;
    }

    public synchronized int updateLectureName(LectureDTO lectureDTO){//교과목 이름 변경
        return lectureDAO.updateLectureByName(lectureDTO);
    }

    public LectureDTO findLectureByLectureCode(String lectureCode){// 교과목 코드로 교과목 정보 찾기
        return lectureDAO.findByLectureCode(lectureCode);
    }

    public synchronized int deleteLecture(String lectureCode){// 교과목 코드로 교과목 삭제
        return lectureDAO.deleteLecture(lectureCode);
    }

    public synchronized int updateLectureByCondition(LectureDTO lectureDTO){// 교과목 내용 변경
        return lectureDAO.updateLectureByCondition(lectureDTO);
    }
}
