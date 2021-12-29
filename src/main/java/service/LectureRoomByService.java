package service;

import persistence.MyBatisConnectionFactory;
import persistence.dao.LectureHistoryDAO;
import persistence.dao.LectureRoomByTimeDAO;
import persistence.dao.OpenLectureDAO;
import persistence.dto.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LectureRoomByService {
    private final LectureRoomByTimeDAO lectureRoomByTimeDAO;

    public LectureRoomByService(LectureRoomByTimeDAO lectureRoomByTimeDAO){ this.lectureRoomByTimeDAO = lectureRoomByTimeDAO; }

    public List<LectureRoomByTimeDTO> selectAllLectureRoomByTime(int openLectureId){//개설교과목 번호로 해당 교과목 강의실,강의 시간 모두 찾기
        List<LectureRoomByTimeDTO> list = lectureRoomByTimeDAO.selectAllLectureRoomByTime(openLectureId);

        return list;
    }

    public String getProfessorSchedule(OpenLectureService openLectureService,String professorId){//교수 시간표 리턴
        ArrayList<String> monday=new ArrayList<String>();
        ArrayList<String> tuesday=new ArrayList<String>();
        ArrayList<String> wednesday=new ArrayList<String>();
        ArrayList<String> thursday=new ArrayList<String>();
        ArrayList<String> friday=new ArrayList<String>();

        String result="";



        ProfessorDTO professorDTO=new ProfessorDTO();
        professorDTO.setProfessorId(professorId);
        LectureDTO lectureDTO=new LectureDTO();
        List<OpenLectureDTO> openLectureDTOS = openLectureService.readOpenLectureByCondition(lectureDTO, professorDTO);
        for(OpenLectureDTO openLectureDTO:openLectureDTOS){
            String lecture_name = openLectureDTO.getLectureDTO().getLectureName();
            List<LectureRoomByTimeDTO> lectureRoomByTimeDTOs=selectAllLectureRoomByTime(openLectureDTO.getOpenLectureId());

            for(LectureRoomByTimeDTO lectureRoomByTimeDTO:lectureRoomByTimeDTOs){
                if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureDay().equals("월")){
                    monday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+" "+lecture_name);
                }
                else if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureDay().equals("화")){
                    tuesday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+" "+lecture_name);
                }
                else if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureDay().equals("수")){
                    wednesday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+" "+lecture_name);
                }
                else if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureDay().equals("목")) {
                    thursday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+" "+lecture_name);
                }
                else{
                    friday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+lecture_name);
                }
            }
        }

        Collections.sort(monday);
        Collections.sort(tuesday);
        Collections.sort(wednesday);
        Collections.sort(thursday);
        Collections.sort(friday);

        result+="\n월요일\n";
        for(String schedule:monday){
            result+=schedule+"\n";
        }
        result+="\n화요일\n";
        for(String schedule:tuesday){
            result+=schedule+"\n";
        }
        result+="\n수요일\n";
        for(String schedule:wednesday){
            result+=schedule+"\n";
        }
        result+="\n목요일\n";
        for(String schedule:thursday){
            result+=schedule+"\n";
        }
        result+="\n금요일\n";
        for(String schedule:friday){
            result+=schedule+"\n";
        }
        return result;
    }

    public String getStudentSchedule(OpenLectureService openLectureService,LectureHistoryService lectureHistoryService,String studentId){// 학생 시간표 리턴
        ArrayList<String> monday=new ArrayList<String>();
        ArrayList<String> tuesday=new ArrayList<String>();
        ArrayList<String> wednesday=new ArrayList<String>();
        ArrayList<String> thursday=new ArrayList<String>();
        ArrayList<String> friday=new ArrayList<String>();
        String result="";


        StudentDTO studentDTO=new StudentDTO();
        studentDTO.setStudentId(studentId);
        List<LectureHistoryDTO> lectureHistoryDTOS = lectureHistoryService.readLectureHistoryByStudentId(studentDTO);

        for(LectureHistoryDTO lectureHistoryDTO:lectureHistoryDTOS){
            int OpenLecutureId=lectureHistoryDTO.getOpenLectureId();
            OpenLectureDTO openLectureJoinById = openLectureService.findOpenLectureJoinById(OpenLecutureId);
            String lecture_name = openLectureJoinById.getLectureDTO().getLectureName();

            List<LectureRoomByTimeDTO> lectureRoomByTimeDTOs=selectAllLectureRoomByTime(openLectureJoinById.getOpenLectureId());
            for(LectureRoomByTimeDTO lectureRoomByTimeDTO:lectureRoomByTimeDTOs){
                if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureDay().equals("월")){
                    monday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+" "+lecture_name);
                }
                else if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureDay().equals("화")){
                    tuesday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+" "+lecture_name);
                }
                else if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureDay().equals("수")){
                    wednesday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+" "+lecture_name);
                }
                else if(lectureRoomByTimeDTO.getLectureTimeDTO().getLectureDay().equals("목")) {
                    thursday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+" "+lecture_name);
                }
                else{
                    friday.add(lectureRoomByTimeDTO.getLectureTimeDTO().getLecturePeriod()+"교시 "+lectureRoomByTimeDTO.getLectureRoomDTO().getBuildingName()+lectureRoomByTimeDTO.getLectureRoomDTO().getLectureRoomNumber()+lecture_name);
                }
            }
        }
        Collections.sort(monday);
        Collections.sort(tuesday);
        Collections.sort(wednesday);
        Collections.sort(thursday);
        Collections.sort(friday);

        result+="\n월요일\n";
        for(String schedule:monday){
            result+=schedule+"\n";
        }
        result+="\n화요일\n";
        for(String schedule:tuesday){
            result+=schedule+"\n";
        }
        result+="\n수요일\n";
        for(String schedule:wednesday){
            result+=schedule+"\n";
        }
        result+="\n목요일\n";
        for(String schedule:thursday){
            result+=schedule+"\n";
        }
        result+="\n금요일\n";
        for(String schedule:friday){
            result+=schedule+"\n";
        }

        return result;
    }
}
