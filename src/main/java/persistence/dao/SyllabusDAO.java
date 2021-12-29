package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.SyllabusDTO;
import persistence.mapper.SyllabusMapper;

import java.util.List;

public class SyllabusDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public SyllabusDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<SyllabusDTO> findAllSyllabus(){
        List<SyllabusDTO> syllabusDTOList=null;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusMapper mapper = session.getMapper(SyllabusMapper.class);

        try{
            syllabusDTOList= mapper.findAllSyllabus();
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return syllabusDTOList;
    }

    public SyllabusDTO findSyllabusByOpenLectureId(int openLectureId){
        SyllabusDTO syllabusDTO=null;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusMapper mapper = session.getMapper(SyllabusMapper.class);

        try{
            syllabusDTO= mapper.findSyllabusByOpenLectureId(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return syllabusDTO;
    }

    public SyllabusDTO findSyllabusJoinWithWeekInfoByOpenLectureId(int openLectureId){
        SyllabusDTO syllabusDTO=null;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusMapper mapper = session.getMapper(SyllabusMapper.class);

        try{
            syllabusDTO= mapper.findSyllabusJoinWithWeekInfoByOpenLectureId(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return syllabusDTO;
    }

    public boolean isSyllabusExist(int openLectureid){
        boolean result=false;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusMapper mapper = session.getMapper(SyllabusMapper.class);

        try{
            result= mapper.isSyllabusExist(openLectureid);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return result;
    }

    public int insertSyllabus(SyllabusDTO syllabusDTO){
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusMapper mapper = session.getMapper(SyllabusMapper.class);

        try{
            result= mapper.insertSyllabus(syllabusDTO);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return result;
    }

    public int updateSyllabus(SyllabusDTO syllabusDTO){
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusMapper mapper = session.getMapper(SyllabusMapper.class);

        try{
            result= mapper.updateSyllabus(syllabusDTO);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return result;
    }

}


