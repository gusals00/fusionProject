package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.LectureTimeDTO;
import persistence.mapper.LectureTimeMapper;

public class LectureTimeDAO {
    private SqlSessionFactory sqlSessionFactory = null;
    public LectureTimeDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public int selectLectureTimeId(String lectureDay, int lecturePeriod){
        SqlSession session = sqlSessionFactory.openSession();
        LectureTimeMapper mapper = session.getMapper(LectureTimeMapper.class);
        int lectureTimeId;
        try{
            lectureTimeId = mapper.selectLectureTimeId(lectureDay, lecturePeriod);
            session.commit();
        }finally{
            session.close();
        }
        return lectureTimeId;
    }

    public LectureTimeDTO selectLectureTimeDTO(int lectureTimeId){
        SqlSession session = sqlSessionFactory.openSession();
        LectureTimeMapper mapper = session.getMapper(LectureTimeMapper.class);
        LectureTimeDTO lectureTimeDTO;

        try{
            lectureTimeDTO = mapper.selectLectureTimeDTO(lectureTimeId);
            session.commit();
        }finally{
            session.close();
        }
        return lectureTimeDTO;
    }
}
