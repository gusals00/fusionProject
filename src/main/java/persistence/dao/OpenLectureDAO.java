package persistence.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import persistence.dto.LectureRoomByTimeDTO;
import persistence.dto.LectureRoomDTO;
import persistence.dto.LectureTimeDTO;
import persistence.dto.OpenLectureDTO;
import persistence.mapper.LectureRoomByTimeMapper;
import persistence.mapper.LectureRoomMapper;
import persistence.mapper.LectureTimeMapper;
import persistence.mapper.OpenLectureMapper;

import java.util.List;

public class OpenLectureDAO {
    private SqlSessionFactory sqlSessionFactory = null;

    public OpenLectureDAO(SqlSessionFactory sqlSessionFactory){
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public List<OpenLectureDTO> findAllOpenLecture(){
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);
        List<OpenLectureDTO> list = null;

        try{
            list = mapper.findAllOpenLecutreJoin();
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    public List<OpenLectureDTO> findOpenLectureByCondition(String professorId,int level){
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);
        List<OpenLectureDTO> list = null;

        try{
            list = mapper.findOpenLecutreJoinByCondition(professorId,level);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return list;
    }

    public int updateMaxStudentNumber(int maxStudentNumber, int openLectureId){//최대 수강 인원 변경
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);

        try{
            result = mapper.updateMaxStudentNumber(maxStudentNumber, openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return result;
    }

    public int insertOpenLecture(OpenLectureDTO openLectureDTO, LectureTimeDTO lectureTimeDTO, LectureRoomDTO lectureRoomDTO){
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper openLectureMapper = session.getMapper(OpenLectureMapper.class);
        LectureTimeMapper lectureTimeMapper = session.getMapper(LectureTimeMapper.class);
        LectureRoomMapper lectureRoomMapper = session.getMapper(LectureRoomMapper.class);
        LectureRoomByTimeMapper lectureRoomByTimeMapper = session.getMapper(LectureRoomByTimeMapper.class);
        int result=0;

        try{
            if(openLectureMapper.checkopenLecture(openLectureDTO.getSeperatedNumber(), openLectureDTO.getLectureCode()) == false){
                openLectureMapper.insertOpenLecture(openLectureDTO);
            }
            int openLectureId = openLectureMapper.selectopenLectureId(openLectureDTO.getSeperatedNumber(), openLectureDTO.getLectureCode());
            int lectureTimeId = lectureTimeMapper.selectLectureTimeId(lectureTimeDTO.getLectureDay(), lectureTimeDTO.getLecturePeriod());
            int lectureRoomId = lectureRoomMapper.selectLectureRoomId(lectureRoomDTO.getBuildingName(), lectureRoomDTO.getLectureRoomNumber());
            if(lectureRoomByTimeMapper.checkUsingLectureRoomId(lectureTimeId, lectureRoomId) == false) {
                result = lectureRoomByTimeMapper.insertLectureRoomByTimeMapper(lectureRoomId, lectureTimeId, openLectureId);
            }
            session.commit();

        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
            return 0;
        } finally{
            session.close();
        }

        return result;
    }

    public int changeLectureRoom(LectureTimeDTO lectureTimeDTO, LectureRoomDTO lectureRoomDTO, LectureRoomDTO changeRoomDTO){
        SqlSession session = sqlSessionFactory.openSession();
        LectureTimeMapper lectureTimeMapper = session.getMapper(LectureTimeMapper.class);
        LectureRoomMapper lectureRoomMapper = session.getMapper(LectureRoomMapper.class);
        LectureRoomByTimeMapper lectureRoomByTimeMapper = session.getMapper(LectureRoomByTimeMapper.class);

        int result=0;

        try{
            int lectureTimeId = lectureTimeMapper.selectLectureTimeId(lectureTimeDTO.getLectureDay(), lectureTimeDTO.getLecturePeriod());
            int lectureRoomId = lectureRoomMapper.selectLectureRoomId(lectureRoomDTO.getBuildingName(), lectureRoomDTO.getLectureRoomNumber());
            int changeRoomId = lectureRoomMapper.selectLectureRoomId(changeRoomDTO.getBuildingName(), changeRoomDTO.getLectureRoomNumber());
            if(lectureRoomByTimeMapper.checkUsingLectureRoomId(lectureTimeId, lectureRoomId) == true) {
                result = lectureRoomByTimeMapper.updateLectureRoom(lectureTimeId, lectureRoomId, changeRoomId);
            }
            session.commit();

        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally{
            session.close();
        }

        return result;
    }

    public List<LectureRoomByTimeDTO> selectAllLectureRoomByTime(int openLectureId){
        SqlSession session = sqlSessionFactory.openSession();
        LectureTimeMapper lectureTimeMapper = session.getMapper(LectureTimeMapper.class);
        LectureRoomMapper lectureRoomMapper = session.getMapper(LectureRoomMapper.class);
        LectureRoomByTimeMapper lectureRoomByTimeMapper = session.getMapper(LectureRoomByTimeMapper.class);

        List<LectureRoomByTimeDTO> lectureRoomByTimeDTOS = null;

        try{
            lectureRoomByTimeDTOS = lectureRoomByTimeMapper.selectAllLectureRoomByTime(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return lectureRoomByTimeDTOS;
    }

    public String getLectureCodeByOpenLectureId(int openLectureId){
        String openLectureCode=null;
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);

        try{
            openLectureCode = mapper.getLectureCode(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return openLectureCode;

    }

    public int getOpenLectureIdByLectureCodeAndSeperatedNumber(String lectureCode,int seperatedNumber){ // 강의코드랑 분반으로 개설강의코드 받아오기
        int openLectureId=0;
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);

        try{
            openLectureId = mapper.selectopenLectureId(seperatedNumber,lectureCode);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return openLectureId;
    }

    public boolean isExistopenLectureId(String lectureCode,int seperatedNumber){
        boolean result=false;
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);

        try{
            result = mapper.isExistopenLectureId(seperatedNumber,lectureCode);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public OpenLectureDTO getOpenLectureByOpenLectureId(int openLectureId){
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);
        OpenLectureDTO openLectureDTO = null;

        try{
            openLectureDTO = mapper.getOpenLectureByOpenLectureId(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return openLectureDTO;
    }

    public OpenLectureDTO findOpenLectureJoinById(int openLectureId){
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);
        OpenLectureDTO openLectureDTO = null;

        try{
            openLectureDTO = mapper.findOpenLectureById(openLectureId);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return openLectureDTO;
    }

    public int updateOpenLectureByCondition(OpenLectureDTO openLectureDTO){
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);


        try{
            result = mapper.updateOpenLectureByCondition(openLectureDTO);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return result;
    }

    public int insertManyOpenLecture(OpenLectureDTO openLectureDTO, List<LectureTimeDTO> lectureTimeDTOS, List<LectureRoomDTO> lectureRoomDTOS,List<Integer> cntList){
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper openLectureMapper = session.getMapper(OpenLectureMapper.class);
        LectureTimeMapper lectureTimeMapper = session.getMapper(LectureTimeMapper.class);
        LectureRoomMapper lectureRoomMapper = session.getMapper(LectureRoomMapper.class);
        LectureRoomByTimeMapper lectureRoomByTimeMapper = session.getMapper(LectureRoomByTimeMapper.class);



        int result=0;

        try{
            if(openLectureMapper.checkopenLecture(openLectureDTO.getSeperatedNumber(), openLectureDTO.getLectureCode()) == false){
                result=openLectureMapper.insertOpenLecture(openLectureDTO);

            }
            else{
                System.out.println("중복된 개설 강좌 존재");
                session.rollback();
                return 0;
            }
            int timeIdx=0;
            List<OpenLectureDTO> openLecutreJoinByCondition = findOpenLectureByCondition(openLectureDTO.getProfessorId(), 0);

            for(int i=0;i<lectureRoomDTOS.size();i++){
                for(int j=0;j<cntList.get(i);j++){
                    int openLectureId = openLectureMapper.selectopenLectureId(openLectureDTO.getSeperatedNumber(), openLectureDTO.getLectureCode());


                    System.out.println(lectureTimeDTOS.get(timeIdx).getLectureDay()+" "+lectureTimeDTOS.get(timeIdx).getLecturePeriod());
                    int lectureTimeId = lectureTimeMapper.selectLectureTimeId(lectureTimeDTOS.get(timeIdx).getLectureDay(), lectureTimeDTOS.get(timeIdx).getLecturePeriod());

                    for(OpenLectureDTO lectureDTO : openLecutreJoinByCondition){
                        List<LectureRoomByTimeDTO> lectureRoomByTimeDTOS = lectureRoomByTimeMapper.selectAllLectureRoomByTime(lectureDTO.getOpenLectureId());
                        for(LectureRoomByTimeDTO lectureRoomByTimeDTO : lectureRoomByTimeDTOS){
                            LectureTimeDTO lectureTimeDTO1 = lectureRoomByTimeDTO.getLectureTimeDTO();
                            if(lectureTimeId == lectureTimeDTO1.getLectureTimeId()){
                                System.out.println("같은 교수의 강의시간이 중첩됨");
                                session.rollback();
                                return 0;
                            }
                        }
                    }

                    int lectureRoomId = lectureRoomMapper.selectLectureRoomId(lectureRoomDTOS.get(i).getBuildingName(), lectureRoomDTOS.get(i).getLectureRoomNumber());
                    if(lectureRoomByTimeMapper.checkUsingLectureRoomId(lectureTimeId, lectureRoomId) == false) {
                        result = lectureRoomByTimeMapper.insertLectureRoomByTimeMapper(lectureRoomId, lectureTimeId, openLectureId);
                    }
                    else{
                        System.out.println("중복된 강의실별 시간 존재");
                        session.rollback();
                        return 0;
                    }

                    timeIdx++;
                }
            }
            session.commit();

        } catch(Exception e){
            //e.printStackTrace();
            session.rollback();
            result=0;
        } finally{
            session.close();
        }

        return result;
    }

    public int updateOpenLecture(OpenLectureDTO openLectureDTO){
        int result=0;
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);

        try{
            result = mapper.updateOpenLecture(openLectureDTO);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

        return result;
    }
    public int updateManyOpenLecture(OpenLectureDTO openLectureDTO, List<LectureTimeDTO> lectureTimeDTOS, List<LectureRoomDTO> lectureRoomDTOS, List<LectureTimeDTO> changeLectureTimeDTOS, List<LectureRoomDTO> changeLectureRoomDTOS ,List<Integer> cntList){
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper openLectureMapper = session.getMapper(OpenLectureMapper.class);
        LectureTimeMapper lectureTimeMapper = session.getMapper(LectureTimeMapper.class);
        LectureRoomMapper lectureRoomMapper = session.getMapper(LectureRoomMapper.class);
        LectureRoomByTimeMapper lectureRoomByTimeMapper = session.getMapper(LectureRoomByTimeMapper.class);

        int result = 0;

        try{
            if((openLectureMapper.checkopenLecture(openLectureDTO.getSeperatedNumber(), openLectureDTO.getLectureCode()) == true) && ((openLectureDTO.getCurStudentNumber()!=-1 || openLectureDTO.getMaxStudentNumber()!=-1) || openLectureDTO.getProfessorId()!=null)){
                result=openLectureMapper.updateOpenLecture(openLectureDTO);
            }

            OpenLectureDTO openLectureDTO1 = openLectureMapper.findOpenLectureById(openLectureMapper.selectopenLectureId(openLectureDTO.getSeperatedNumber(), openLectureDTO.getLectureCode()));
            List<OpenLectureDTO> openLecutreJoinByCondition = findOpenLectureByCondition(openLectureDTO1.getProfessorId(), 0);
            for(int i=0; i<lectureRoomDTOS.size(); i++){
                int lectureRoomId = lectureRoomMapper.selectLectureRoomId(lectureRoomDTOS.get(i).getBuildingName(), lectureRoomDTOS.get(i).getLectureRoomNumber());

                if(!lectureRoomDTOS.toString().equals(changeLectureRoomDTOS.toString())) {
                    int changeLectureRoomId = lectureRoomMapper.selectLectureRoomId(changeLectureRoomDTOS.get(i).getBuildingName(), changeLectureRoomDTOS.get(i).getLectureRoomNumber());
                    for (int j = 0; j < cntList.get(i); j++) {
                        int lectureTimeId = lectureTimeMapper.selectLectureTimeId(lectureTimeDTOS.get(j).getLectureDay(), lectureTimeDTOS.get(j).getLecturePeriod());
                        result = lectureRoomByTimeMapper.updateLectureRoom(lectureTimeId, lectureRoomId, changeLectureRoomId);
                    }
                }

                lectureRoomId = lectureRoomMapper.selectLectureRoomId(changeLectureRoomDTOS.get(i).getBuildingName(), changeLectureRoomDTOS.get(i).getLectureRoomNumber());

                for(int j=0; j<cntList.get(i); j++){
                    int lectureTimeId = lectureTimeMapper.selectLectureTimeId(lectureTimeDTOS.get(j).getLectureDay(), lectureTimeDTOS.get(j).getLecturePeriod());
                    int changeLectureTimeId = lectureTimeMapper.selectLectureTimeId(changeLectureTimeDTOS.get(j).getLectureDay(), changeLectureTimeDTOS.get(j).getLecturePeriod());
                    for(OpenLectureDTO lectureDTO : openLecutreJoinByCondition){
                        List<LectureRoomByTimeDTO> lectureRoomByTimeDTOS = lectureRoomByTimeMapper.selectAllLectureRoomByTime(lectureDTO.getOpenLectureId());
                        for(LectureRoomByTimeDTO lectureRoomByTimeDTO : lectureRoomByTimeDTOS){
                            LectureTimeDTO lectureTimeDTO1 = lectureRoomByTimeDTO.getLectureTimeDTO();
                            if(changeLectureTimeId == lectureTimeDTO1.getLectureTimeId()){
                                System.out.println("같은 교수의 강의시간이 중첩됨");
                                session.rollback();
                                return 0;
                            }
                        }
                    }
                    result = lectureRoomByTimeMapper.updateLectureTime(lectureRoomId, lectureTimeId, changeLectureTimeId);
                }
            }
            session.commit();

        } catch(Exception e){
            //e.printStackTrace();
            session.rollback();
            result=0;
        } finally{
            session.close();
        }

        return result;
    }

    public int deleteOpenLecture(int seperatedNumber ,String lectureCode){ // 강의코드랑 분반으로 개설강의코드 받아오기
        SqlSession session = sqlSessionFactory.openSession();
        OpenLectureMapper mapper = session.getMapper(OpenLectureMapper.class);
        int result =0;

        try{
            result = mapper.deleteOpenLecture(seperatedNumber,lectureCode);
            session.commit();
        } catch(Exception e){
            e.printStackTrace();
            session.rollback();
            return result;
        } finally {
            session.close();
        }

        return result;
    }

}
