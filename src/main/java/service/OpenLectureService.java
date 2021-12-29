package service;

import persistence.dao.OpenLectureDAO;
import persistence.dto.*;

import java.util.List;

public class OpenLectureService {
    private final OpenLectureDAO openLectureDAO;

    public OpenLectureService(OpenLectureDAO openLectureDAO){
        this.openLectureDAO = openLectureDAO;
    }

    public synchronized int insertOpenLecture(OpenLectureDTO openLectureDTO, LectureTimeDTO lectureTimeDTO, LectureRoomDTO lectureRoomDTO) {// 개설 교과목 삽입
        return openLectureDAO.insertOpenLecture(openLectureDTO,lectureTimeDTO,lectureRoomDTO);
        // 해당 개설 교과목이 존재하는지 확인 & 교시별 강의실 사용 가능할 경우만 insert
    }

    public List<OpenLectureDTO> readAllOpenLecture(){// 개설 교과목 전체 조회
        List<OpenLectureDTO> allOpenLecture = openLectureDAO.findAllOpenLecture();

        return allOpenLecture;
    }

    public List<OpenLectureDTO> readOpenLectureByCondition(LectureDTO lectureDTO, ProfessorDTO professorDTO) {// 학년별, 담당 교수별 개설 교과목 조회
        String professorId = professorDTO.getProfessorId();
        int level = lectureDTO.getLectureLevel();
        List<OpenLectureDTO> openLectureByCondition = openLectureDAO.findOpenLectureByCondition(professorId, level);

        return openLectureByCondition;
    }

    public synchronized int updateMaxStudentNumber(int maxStudentNumber, int openLectureId){//최대 수강 인원 변경
        return openLectureDAO.updateMaxStudentNumber(maxStudentNumber, openLectureId);
    }

    public int getOpenLectureIdByLectureCodeAndSeperatedNumber(String lectureCode,int seperatedNumber){// 교과목 코드와 분반으로 개설교과목 번호 찾기
        int openLectureId = openLectureDAO.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, seperatedNumber);
        return openLectureId;
    }

    public boolean isExistopenLectureId(String lectureCode,int seperatedNumber){// 해당 개설 교과목 존재 여부
        return openLectureDAO.isExistopenLectureId(lectureCode,seperatedNumber);
    }

    public OpenLectureDTO getOpenLectureByOpenLectureId(int openLectureId){//개설 교과목 id에 해당하는 개설 교과목 정보 찾기
        OpenLectureDTO openLectureDTO = openLectureDAO.getOpenLectureByOpenLectureId(openLectureId);
        return openLectureDTO;
    }

    public OpenLectureDTO findOpenLectureJoinById(int openLectureId){//개설 교과목 id에 해당하는 개설 교과목 정보,시간 및 강의실 찾기
        OpenLectureDTO openLectureDTO=openLectureDAO.findOpenLectureJoinById(openLectureId);
        return openLectureDTO;
    }

    public synchronized int insertManyOpenLecture(OpenLectureDTO openLectureDTO,LectureService lectureService, List<LectureTimeDTO> lectureTimeDTOS, List<LectureRoomDTO> lectureRoomDTOS,List<Integer> cntList){
        // 한번에 여러개의 개설 교과목,강의실 및 시간 추가
        if(lectureService.findLectureByLectureCode(openLectureDTO.getLectureCode())==null){
            System.out.println("강의 코드가 존재하지 않음.");
            return 0;
        }

        return openLectureDAO.insertManyOpenLecture(openLectureDTO,lectureTimeDTOS,lectureRoomDTOS,cntList);
    }
    public synchronized int deleteOpenLecture(int seperatedNumber, String lectureCode){//개설 고과목 삭제
        return openLectureDAO.deleteOpenLecture(seperatedNumber, lectureCode);
    }

    public synchronized int updateManyOpenLecture(OpenLectureDTO openLectureDTO, List<LectureTimeDTO> lectureTimeDTOS, List<LectureRoomDTO> lectureRoomDTOS, List<LectureTimeDTO> changeLectureTimeDTOS, List<LectureRoomDTO> changeLectureRoomDTOS ,List<Integer> cntList){
        //개설 교과목, 강의실 및 시간 여러개 변경
        return openLectureDAO.updateManyOpenLecture(openLectureDTO, lectureTimeDTOS, lectureRoomDTOS, changeLectureTimeDTOS, changeLectureRoomDTOS, cntList);
    }

}
