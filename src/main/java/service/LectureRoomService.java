package service;

import persistence.dao.LectureRoomByTimeDAO;
import persistence.dao.LectureRoomDAO;
import persistence.dto.LectureRoomDTO;

public class LectureRoomService {

    private final LectureRoomDAO lectureRoomDAO;

    public LectureRoomService(LectureRoomDAO lectureRoomDAO){ this.lectureRoomDAO = lectureRoomDAO; }

    public LectureRoomDTO selectLectureRoomDTO(int lectureRoomId){//강의실 id 로 강의실 찾기
        return lectureRoomDAO.selectLectureRoomDTO(lectureRoomId);
    }
    public int insertRoom(LectureRoomDTO lectureRoomDTO){ // 강의실 추가
        return lectureRoomDAO.insertRoom(lectureRoomDTO);
    }
}
