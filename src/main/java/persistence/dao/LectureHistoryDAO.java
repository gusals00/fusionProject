package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.LectureHistoryDTO;
import persistence.dto.LectureRoomByTimeDTO;
import persistence.mapper.LectureHistoryMapper;
import persistence.mapper.LectureRoomByTimeMapper;
import persistence.mapper.OpenLectureMapper;

import java.util.List;

public class LectureHistoryDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public LectureHistoryDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public int getTotalPage(int openLectureId,int pageSize){//해당 개설 교과목 듣는 학생 수 출력하는 총 페이지 수
        double totalCnt=0;
        int totalPage =0;
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper mapper = session.getMapper(LectureHistoryMapper.class);

        try{
            totalCnt = mapper.getRegistrationStudentCnt(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        totalPage=(int)Math.ceil(totalCnt/pageSize);

        return totalPage;
    }

    public List<String> findStudentIdsForPage(int openLectureId,int pageNumber,int pageSize){// pageNumber -> 몇번째 페이지, pageSize -> 한 페이지당 몇개 정보 출력
        int curPageRow = pageSize*(pageNumber-1);
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper mapper = session.getMapper(LectureHistoryMapper.class);
        List<String> list = null;

        try{
            list = mapper.findStudentIdByRegistrationOpenLectureId(openLectureId,curPageRow,pageSize);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    public boolean isOverMaxStudentCredit(int openLectureId, String studentId){//최대 수강신청 학점 넘었는가?
        boolean result=true;
        final int MAX_CREDIT = 21;
        int studentTotalCredit=0;
        int curStudentCreditSum=0;
        int registerOpenLectureCredit=0;
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper histroyMapper = session.getMapper(LectureHistoryMapper.class);
        OpenLectureMapper openMapper = session.getMapper(OpenLectureMapper.class);

        try{
            curStudentCreditSum=histroyMapper.registeredStudentCreditSum(studentId);//수강 신청 완료된 교과목 학점 합
            registerOpenLectureCredit=openMapper.getCredit(openLectureId);//현재 수강 신청한 교과목 학점
            studentTotalCredit = curStudentCreditSum+registerOpenLectureCredit;

            if(studentTotalCredit<=MAX_CREDIT){
                result=false;
            }

            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;

    }

    public boolean isRegisterSameLectureCode(int openLectureId,String studentId){//수강 신청 과목 중 같은 과목 코드가 존재하는가?
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper histroyMapper = session.getMapper(LectureHistoryMapper.class);
        OpenLectureMapper openMapper = session.getMapper(OpenLectureMapper.class);


        try{
            List<String> lectureCodesByStudentId = histroyMapper.getLectureCodesByStudentId(studentId);//수강 신청 완료된 lecture code들 리턴
            String registerLectureCode = openMapper.getLectureCode(openLectureId);//현재 수강신청하려는 lectureCode
            for(String lectureCode:lectureCodesByStudentId){

                if (lectureCode.equals(registerLectureCode)){
                    return true;
                }
            }
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return false;
    }
    public int getStudentCnt(int openLectureId){//해당 개설 교과목 듣는 학생 수 리턴
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper mapper = session.getMapper(LectureHistoryMapper.class);

        try{
            result = mapper.getRegistrationStudentCnt(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return result;
    }

    public boolean isOverStudentNumber(int openLectureId){
        boolean result=true;
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper mapper = session.getMapper(LectureHistoryMapper.class);

        try{
            result = mapper.isOverStudentNumber(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return result;
    }

    public int addCurStudentNumber(int openLectureId){
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper mapper = session.getMapper(LectureHistoryMapper.class);

        try{
            result = mapper.addCurStudentNumber(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return result;
    }

//    public boolean isDuplicateTime(int openLectureId, String studentId){
//        boolean result = true;
//        SqlSession session = sqlSessionFactory.openSession();
//        LectureHistoryMapper lectureHistoryMapper = session.getMapper(LectureHistoryMapper.class);
//        LectureRoomByTimeMapper lectureRoomByTimeMapper = session.getMapper(LectureRoomByTimeMapper.class);
//
//        try{
//            List<LectureRoomByTimeDTO> compareList = lectureRoomByTimeMapper.selectAllLectureRoomByTime(openLectureId);
//            List<LectureHistoryDTO> list = lectureHistoryMapper.getStudentLectureHistory(studentId);
//            for(LectureHistoryDTO lectureHistoryDTO : list){
//                List<LectureRoomByTimeDTO> timeList = lectureRoomByTimeMapper.selectAllLectureRoomByTime(lectureHistoryDTO.getOpenLectureId());
//                for(LectureRoomByTimeDTO lectureRoomByTimeDTO : timeList){
//                    for(LectureRoomByTimeDTO lectureRoomByTimeDTO1 : compareList){
//                        if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureTimeId() == lectureRoomByTimeDTO1.getLectureTimeDTO().getLectureTimeId()){
//                            return true;
//                        }
//                    }
//                }
//            }
//        } catch(Exception e){
//            e.printStackTrace();
//            session.rollback();
//        } finally {
//            session.close();
//        }
//
//        return false;
//    }

    public boolean isDuplicateTime(int openLectureId, String studentId){
        boolean result = true;
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper lectureHistoryMapper = session.getMapper(LectureHistoryMapper.class);
        LectureRoomByTimeMapper lectureRoomByTimeMapper = session.getMapper(LectureRoomByTimeMapper.class);

        try{
            List<LectureHistoryDTO> lectureHistory = lectureHistoryMapper.findLectureHistory(studentId);
            List<LectureRoomByTimeDTO> lectureRoomByTimeDTOS = lectureRoomByTimeMapper.selectAllLectureRoomByTime(openLectureId);
            for(LectureHistoryDTO lectureHistoryDTO : lectureHistory){
                for(LectureRoomByTimeDTO lectureRoomByTimeDTO : lectureHistoryDTO.getLectureRoomByTimeDTO()) {
                    for (LectureRoomByTimeDTO lectureRoomByTimeDTO2 : lectureRoomByTimeDTOS) {
                        if(lectureRoomByTimeDTO.getLectureTimeId() == lectureRoomByTimeDTO2.getLectureTimeId()){
                            return true;
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return false;
    }

    public List<LectureHistoryDTO> getStudentLectureHistory(String studentId){
        List<LectureHistoryDTO> list = null;

        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper mapper = session.getMapper(LectureHistoryMapper.class);

        try{
            list = mapper.getStudentLectureHistory(studentId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    public int insertLectureHistory(String studentId, int openLectureId){
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper mapper = session.getMapper(LectureHistoryMapper.class);
        int result=0;
        try{
            result = mapper.insertLectureHistory(studentId,openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return result;
    }

    public int deleteLectureHistory(String studentId, int openLectureId){
        SqlSession session = sqlSessionFactory.openSession();
        LectureHistoryMapper mapper = session.getMapper(LectureHistoryMapper.class);
        int result=0;
        try{
            result = mapper.deleteLectureHistory(studentId,openLectureId);
            if(result==0){
                return result;
            }
            mapper.minusCurStudentNumber(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return result;
    }


}
