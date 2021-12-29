package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.MyBatisConnectionFactory;
import persistence.dto.LectureDTO;
import persistence.dto.LectureRoomByTimeDTO;
import persistence.dto.OpenLectureDTO;
import persistence.dto.ProfessorDTO;
import persistence.mapper.LectureRoomByTimeMapper;
import persistence.mapper.LectureRoomMapper;
import persistence.mapper.LectureTimeMapper;
import service.LectureHistoryService;
import service.LectureRoomByService;
import service.OpenLectureService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LectureRoomByTimeDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public LectureRoomByTimeDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<LectureRoomByTimeDTO> selectAllLectureRoomByTime(int openLectureId){
        List<LectureRoomByTimeDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        LectureRoomByTimeMapper mapper = session.getMapper(LectureRoomByTimeMapper.class);

        try{
            list = mapper.selectAllLectureRoomByTime(openLectureId);
            session.commit();
        }finally{
            session.close();
        }
        return list;
    }

    public LectureRoomByTimeDTO selectlectureRoombyTimeDTO(int openLectureId, String buildingName, int lectureRoomNumber, String lectureDay, int lecturePeriod){
        List<LectureRoomByTimeDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        LectureRoomByTimeMapper lectureRoomByTimeMapper = session.getMapper(LectureRoomByTimeMapper.class);
        LectureRoomMapper lectureRoomMapper = session.getMapper(LectureRoomMapper.class);
        LectureTimeMapper lectureTimeMapper = session.getMapper(LectureTimeMapper.class);

        LectureRoomByTimeDTO lectureRoomByTimeDTO = null;
        try{
            int lectureRoomId = lectureRoomMapper.selectLectureRoomId(buildingName, lectureRoomNumber);
            int lectureTimeId = lectureTimeMapper.selectLectureTimeId(lectureDay,lecturePeriod);
            lectureRoomByTimeDTO = lectureRoomByTimeMapper.selectlectureRoombyTimeDTO(openLectureId, lectureRoomId,lectureTimeId);
            session.commit();
        }finally{
            session.close();
        }
        return lectureRoomByTimeDTO;
    }

}
