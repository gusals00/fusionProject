package persistence.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.SyllabusWeekInfoDTO;
import persistence.mapper.SyllabusWeekInfoMapper;

import java.util.List;

public class SyllabusWeekInfoDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public SyllabusWeekInfoDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<SyllabusWeekInfoDTO> findAllSyllabusWeekInfo(){
        List<SyllabusWeekInfoDTO> syllabusWeekInfoList=null;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusWeekInfoMapper mapper = session.getMapper(SyllabusWeekInfoMapper.class);

        try{
            syllabusWeekInfoList = mapper.findAllSyllabusWeekInfo();
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return syllabusWeekInfoList;
    }

    public List<SyllabusWeekInfoDTO> findSyllabusWeekInfoBySyllabusId(int syllabusId){
        List<SyllabusWeekInfoDTO> syllabusWeekInfoList=null;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusWeekInfoMapper mapper = session.getMapper(SyllabusWeekInfoMapper.class);

        try{
            syllabusWeekInfoList = mapper.findSyllabusWeekInfoBySyllabusId(syllabusId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return syllabusWeekInfoList;
    }

    public int insertSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO){
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusWeekInfoMapper mapper = session.getMapper(SyllabusWeekInfoMapper.class);

        try{
            result = mapper.insertSyllabusWeekInfo(syllabusWeekInfoDTO);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return result;
    }
    public int updateSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO){
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusWeekInfoMapper mapper = session.getMapper(SyllabusWeekInfoMapper.class);

        try{
            result= mapper.updateSyllabusWeekInfo(syllabusWeekInfoDTO);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return result;
    }
    public boolean isExistSyllabusWeekInfo(int syllabusId,int syllabusWeek){
        boolean result=false;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusWeekInfoMapper mapper = session.getMapper(SyllabusWeekInfoMapper.class);

        try{
            result=mapper.isExistSyllabusWeekInfo(syllabusId,syllabusWeek);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return result;
    }


    public int deleteSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO){
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        SyllabusWeekInfoMapper mapper = session.getMapper(SyllabusWeekInfoMapper.class);

        try{
            result=mapper.deleteSyllabusWeekInfo(syllabusWeekInfoDTO);
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
