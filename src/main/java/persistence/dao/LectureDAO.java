package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.LectureDTO;

import java.util.List;

public class LectureDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public LectureDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<LectureDTO> findAllLecture(){ //모든 교과목 찾기
        List<LectureDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.LectureMapper.selectLectureAll");
            session.commit();
        }finally{
            session.close();
        }
        return list;
    }

    public List<LectureDTO> findLectureByLevel(int lectureLevel){ //해당 학년 교과목 찾기
        List<LectureDTO> list = null;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            list = session.selectList("mapper.LectureMapper.selectLectureByLevel",lectureLevel);
            session.commit();
        }finally{
            session.close();
        }
        return list;
    }

    public int updateLectureByName(LectureDTO lectureDTO){ //교과목 이름 변경
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            result = session.update("mapper.LectureMapper.updateLectureName",lectureDTO);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally{
            session.close();
        }
        return result;
    }

    public int insertLecture(LectureDTO lectureDTO){ // 교과목 추가
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        try{
            result = session.insert("mapper.LectureMapper.insertLecture",lectureDTO);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally{
            session.close();
        }
        return result;
    }

    public int deleteLecture(String lectureCode){//교과목 삭제
        int result=0;
        SqlSession session=sqlSessionFactory.openSession();
        try{
            result = session.delete("mapper.LectureMapper.deleteLecture",lectureCode);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return result;
    }

    public LectureDTO findByLectureCode(String lectureCode){
        LectureDTO lectureDTO=null;
        SqlSession session=sqlSessionFactory.openSession();
        try{
            lectureDTO = session.selectOne("mapper.LectureMapper.selectLectureByLectureCode",lectureCode);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return lectureDTO;
    }

    public int updateLectureByCondition(LectureDTO lectureDTO){
        int result=0;
        SqlSession session=sqlSessionFactory.openSession();
        try{
            result = session.update("mapper.LectureMapper.updateLectureByCondition",lectureDTO);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return result;
    }
}

