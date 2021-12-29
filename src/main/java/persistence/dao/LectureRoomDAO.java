package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.LectureRoomDTO;
import persistence.mapper.LectureRoomMapper;

public class LectureRoomDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public LectureRoomDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public int selectLectureRoomId(String buildingName, int lectureRoomNumber){
        SqlSession session = sqlSessionFactory.openSession();
        LectureRoomMapper mapper = session.getMapper(LectureRoomMapper.class);
        int lectureRoomId;
        try{
            lectureRoomId = mapper.selectLectureRoomId(buildingName, lectureRoomNumber);
            session.commit();
        }finally{
            session.close();
        }
        return lectureRoomId;
    }

    public LectureRoomDTO selectLectureRoomDTO(int lectureRoomId){
        SqlSession session = sqlSessionFactory.openSession();
        LectureRoomMapper mapper = session.getMapper(LectureRoomMapper.class);
        LectureRoomDTO lectureRoomDTO;
        try{
            lectureRoomDTO = mapper.selectLectureRoomDTO(lectureRoomId);
            session.commit();
        }finally{
            session.close();
        }
        return lectureRoomDTO;
    }

    public int insertRoom(LectureRoomDTO lectureRoomDTO){
        SqlSession session = sqlSessionFactory.openSession();
        LectureRoomMapper mapper = session.getMapper(LectureRoomMapper.class);
        int result=0;
        try{
            result = mapper.insertRoom(lectureRoomDTO.getBuildingName(),lectureRoomDTO.getLectureRoomNumber());
            session.commit();
        }finally{
            session.close();
        }
        return result;
    }
}
