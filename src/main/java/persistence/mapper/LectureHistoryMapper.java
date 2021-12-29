package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.dto.LectureHistoryDTO;

import java.util.List;

public interface LectureHistoryMapper {
    @Select("select count(*) from lecture_history where open_lecture_id=#{openLectureId}")
    int getRegistrationStudentCnt(@Param("openLectureId")int openLectureId);//해당 강의(openLectureId)를 신청한 학생 수 리턴

    @Select("select stud.student_id from(select * from lecture_history where open_lecture_id = #{openLectureId} order by student_id) stud limit #{rowCnt} offset #{startRow}")
    List<String> findStudentIdByRegistrationOpenLectureId(@Param("openLectureId") int openLectureId, @Param("startRow") int startRow, @Param("rowCnt") int rowCnt);

    @Select("select ifnull(sum(lecture.lecture_credit),0) " +
            "from ( " +
            "select open_lecture.* from lecture_history join open_lecture on lecture_history.open_lecture_id=open_lecture.open_lecture_id where student_id=#{studentId}" +
            ") lec join lecture on lec.lecture_code=lecture.lecture_code;")
    int registeredStudentCreditSum(@Param("studentId")String studentId);//해당 학생이 지금까지 신청한 학점 수 합

    @Select("select lecture_code from lecture_history join open_lecture on lecture_history.open_lecture_id=open_lecture.open_lecture_id where student_id=#{studentId}")
    List<String> getLectureCodesByStudentId(@Param("studentId")String studentId); //해당 학생이 신청한 개설 교과목 코드들 리턴

    @Select("SELECT count(*) FROM open_lecture WHERE open_lecture_id = #{openLectureId} and cur_student_number >= max_student_number")
    boolean isOverStudentNumber(@Param("openLectureId") int openLectureId);

    @Update("update open_lecture set cur_student_number=cur_student_number+1 where open_lecture_id=#{openLectureId}")
    int addCurStudentNumber(@Param("openLectureId") int openLectureId);

    @Update("update open_lecture set cur_student_number=cur_student_number-1 where open_lecture_id=#{openLectureId}")
    int minusCurStudentNumber(@Param("openLectureId") int openLectureId);

    @Select("SELECT * FROM lecture_history WHERE student_id = #{studentId}")
    @Results(value = {
            @Result(property = "lectureRegistrationId", column = "lecture_register_id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "openLectureId", column = "open_lecture_id")
    })
    List<LectureHistoryDTO> getStudentLectureHistory(@Param("studentId") String studentId);

    @Insert("INSERT INTO lecture_history(student_id,open_lecture_id) VALUES(#{studentId}, #{openLectureId})")
    int insertLectureHistory(@Param("studentId") String studentId, @Param("openLectureId") int openLectureId);

    @Delete("DELETE FROM lecture_history WHERE student_id=#{studentId} and open_lecture_id=#{openLectureId}")
    int deleteLectureHistory(@Param("studentId") String studentId, @Param("openLectureId") int openLectureId);

    @Select("SELECT * from lecture_history join open_lecture on lecture_history.open_lecture_id = open_lecture.open_lecture_id WHERE student_id=#{studentId} order by lecture_code")
    @Results(value = {
            @Result(property = "lectureRegistrationId", column = "lecture_register_id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "openLectureId", column = "open_lecture_id"),
            @Result(property = "openLectureDTO",column = "open_lecture_id",one = @One(select = "mapper.OpenLectureMapper.getOpenLectureByOpenLectureId"))
    })
    List<LectureHistoryDTO> findHistorybyStudentId(@Param("studentId") String studentId);


    @Select("SELECT * FROM lecture_history join lecture_room_by_time on lecture_history.open_lecture_id = lecture_room_by_time.open_lecture_id WHERE student_id = #{studentId}")
    @Results(value = {
            @Result(property = "lectureRegistrationId", column = "lecture_register_id"),
            @Result(property = "studentId", column = "student_id"),
            @Result(property = "openLectureId", column = "open_lecture_id"),
            @Result(property = "lectureRoomByTimeDTO",column = "open_lecture_id",many = @Many(select = "persistence.mapper.LectureRoomByTimeMapper.selectAllLectureRoomByTime"))
    })
    List<LectureHistoryDTO> findLectureHistory(@Param("studentId") String studentId);
}
