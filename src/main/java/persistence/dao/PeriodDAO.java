package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.PeriodDTO;
import persistence.mapper.PeriodMapper;

import java.util.List;

public class PeriodDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public PeriodDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<PeriodDTO> findAllPeriod(){
        List<PeriodDTO> list=null;
        SqlSession session=sqlSessionFactory.openSession();
        PeriodMapper mapper=session.getMapper(PeriodMapper.class);

        try{
            list=mapper.findAllPeriod();
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return list;
    }

    public PeriodDTO findPeriodByPeriodName(String periodName){
        PeriodDTO periodDTO=null;
        SqlSession session=sqlSessionFactory.openSession();
        PeriodMapper mapper=session.getMapper(PeriodMapper.class);

        try{
            periodDTO=mapper.findPeriodByPeriodName(periodName);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return periodDTO;
    }
    /*
    public int updateOpenTime(Date curDate){
        int result=0;
        SqlSession session=sqlSessionFactory.openSession();
        PeriodMapper mapper=session.getMapper(PeriodMapper.class);

        try{
            result=mapper.updateOpenTime(curDate);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return result;
    }*/

    public int updateOpenTime(PeriodDTO periodDTO){
        int result=0;
        SqlSession session=sqlSessionFactory.openSession();
        PeriodMapper mapper=session.getMapper(PeriodMapper.class);

        try{
            result=mapper.updateOpenTime(periodDTO);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return result;
    }
    public int updateCloseTime(PeriodDTO periodDTO){
        int result=0;
        SqlSession session=sqlSessionFactory.openSession();
        PeriodMapper mapper=session.getMapper(PeriodMapper.class);

        try{
            result=mapper.updateCloseTime(periodDTO);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return result;
    }

    public boolean isAvailableRegister(int periodId){//해당 교과목 수강 신청 가능 상태 확인(교과목의 경우 level을 넘겨주면 됨)
        boolean result = false;
        SqlSession session=sqlSessionFactory.openSession();
        PeriodMapper mapper=session.getMapper(PeriodMapper.class);

        try{
            result=mapper.isAvailablePeriod(periodId);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
        }finally {
            session.close();
        }
        return result;
    }

    public int updateAllTime(PeriodDTO periodDTO){
        int result=0;
        SqlSession session=sqlSessionFactory.openSession();
        PeriodMapper mapper=session.getMapper(PeriodMapper.class);

        try{
            result=mapper.updateTime(periodDTO);
            session.commit();
        }catch(Exception e){
            e.printStackTrace();
            session.rollback();
            return result;
        }finally {
            session.close();
        }
        return result;
    }
}
