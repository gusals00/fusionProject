package service;

import persistence.dao.LectureRoomDAO;
import persistence.dao.LectureTimeDAO;
import persistence.dto.LectureTimeDTO;

public class LectureTimeService {
    private final LectureTimeDAO lectureTimeDAO;

    public LectureTimeService(LectureTimeDAO lectureTimeDAO){ this.lectureTimeDAO = lectureTimeDAO; }

    public LectureTimeDTO selectLectureTimeId(int lectureTimeId){// 교과목 시간 찾기
        return lectureTimeDAO.selectLectureTimeDTO( lectureTimeId);
    }



}
