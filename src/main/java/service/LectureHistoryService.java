package service;


import persistence.dao.LectureHistoryDAO;
import persistence.dto.LectureDTO;
import persistence.dto.LectureHistoryDTO;
import persistence.dto.OpenLectureDTO;
import persistence.dto.StudentDTO;

import java.util.ArrayList;
import java.util.List;

public class LectureHistoryService {
    private final LectureHistoryDAO lectureHistoryDAO;

    public LectureHistoryService(LectureHistoryDAO lectureHistoryDAO){
        this.lectureHistoryDAO = lectureHistoryDAO;
    }

    public List<LectureHistoryDTO> readStudentLectureHistory(String studentId){// 학생의 수강신청 현황 조회
        List<LectureHistoryDTO> studentLectureHistory = lectureHistoryDAO.getStudentLectureHistory(studentId);
        return studentLectureHistory;
    }



    //수강신청+수강신청 예외
    public synchronized String registerOpenLecture(OpenLectureDTO openLectureDTO, StudentDTO studentDTO,OpenLectureService openLectureService,LectureService lectureService,PeriodService periodService){
        String lectureCode = openLectureDTO.getLectureCode();//과목 코드
        int seperatedNumber = openLectureDTO.getSeperatedNumber();//분반번호

        int openLectureId = openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode,seperatedNumber);//개설 교과목 번호


        String studentId=studentDTO.getStudentId();//학생 id
        int studentLevel= studentDTO.getStudentLevel();//학생 학년

        LectureDTO lectureByLectureCode = lectureService.findLectureByLectureCode(lectureCode);

        boolean isAvailableRegisiter = periodService.isAvailableRegister(lectureByLectureCode.getLectureLevel());

        boolean isRightStudentLevel = periodService.isAvailableRegister(studentLevel);//해당 학년에 맞는 학생이 수강신청했는지 여부

        if(!isRightStudentLevel){//해당 학생의 학년에 맞는 수강 신청 기간인지 확인
            isRightStudentLevel = periodService.isAvailableRegister(5);// 전체 수강신청 기간 확인
            if(!isRightStudentLevel){
                return "수강신청 기간이 아닙니다.";
            }
        }



        if(!isAvailableRegisiter){//해당 과목 신청 기간 확인
            isAvailableRegisiter = periodService.isAvailableRegister(5);// 전체 수강신청 기간 확인
            if(!isAvailableRegisiter){
                return "다른 학년의 강의를 신청했습니다.";// 신청하는 학생의 학년에 맞는 lecture_level이 아닌 경우
            }
        }

        boolean isOverMaxStudentCredit = lectureHistoryDAO.isOverMaxStudentCredit(openLectureId,studentId);
        if(isOverMaxStudentCredit){
            return "신청하는 학생이 최대 학점을 넘겼습니다.";// 신청하는 학생이 최대 학점을 넘겼을경우
        }


        boolean isSameLectureExist = lectureHistoryDAO.isRegisterSameLectureCode(openLectureId,studentId);
        if(isSameLectureExist){
            return "같은 강의코드를 가진 강의를 신청했습니다."; // 같은 코드의 강의를 신청했을경우
        }

        boolean isDuplicateTime = lectureHistoryDAO.isDuplicateTime(openLectureId,studentId);
        if(isDuplicateTime){
            return "강의 시간이 중복됩니다."; // 시간이 중복되는 경우
        }

        boolean isOverStudentNumber = lectureHistoryDAO.isOverStudentNumber(openLectureId);
        if(isOverStudentNumber){
            return "최대 수강 인원을 넘었습니다."; // 최대 수강 인원을 넘은 과목을 신청하는 경우
        }


        lectureHistoryDAO.addCurStudentNumber(openLectureId); // 현재 수강 신청 인원 + 1
        lectureHistoryDAO.insertLectureHistory(studentId,openLectureId);

        return "";
    }

    public int deleteLectureHistory(OpenLectureDTO openLectureDTO, StudentDTO studentDTO, OpenLectureService openLectureService){// 수강신청 취소
        String lectureCode = openLectureDTO.getLectureCode();
        int seperatedNumber = openLectureDTO.getSeperatedNumber();

        int openLectureId = openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode,seperatedNumber);
        String studentId=studentDTO.getStudentId();

        int result=0;
        result = lectureHistoryDAO.deleteLectureHistory(studentId, openLectureId);

        return result;
    }

    public List<LectureHistoryDTO> readLectureHistoryByStudentId(StudentDTO studentDTO){ // 해당 학생이 신청한 개설 교과목 조회
        String studentId = studentDTO.getStudentId();
        List<LectureHistoryDTO> studentLectureHistory = lectureHistoryDAO.getStudentLectureHistory(studentId);
        return studentLectureHistory;
    }

    public List<StudentDTO> readStudentList(OpenLectureDTO openLectureDTO, StudentService studentService,int curPage,int pageSize){//교수 관점 페이징
        //curPage-> 현재 페이지
        //pageSize -> 페이지 크기
        List<StudentDTO> studentDTOList = new ArrayList<StudentDTO>();
        int openLectureId = openLectureDTO.getOpenLectureId();
        List<String> studentIdsForPage = lectureHistoryDAO.findStudentIdsForPage(openLectureId, curPage, pageSize);
        for(String studentId:studentIdsForPage){
            StudentDTO studentDTO=studentService.findStudentByStudentId(studentId);
            studentDTOList.add(studentDTO);
        }

        return studentDTOList;
    }

    public int getTotalPage(int openLectureId,int pageSize){// 총 페이지 크기 리턴
        int totalPage = lectureHistoryDAO.getTotalPage( openLectureId, pageSize);
        return totalPage;
    }
}
