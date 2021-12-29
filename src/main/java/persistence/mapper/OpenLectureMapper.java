package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.dto.OpenLectureDTO;
import persistence.dto.ProfessorDTO;

import java.util.List;

public interface OpenLectureMapper {

    @Select("select * from open_lecture join lecture on open_lecture.lecture_code=lecture.lecture_code order by lecture_level,open_lecture.lecture_code,seperated_number")
    @Results(id="OpenLectureJoinResult",value={
            @Result(property = "openLectureId", column = "open_lecture_id"),
            @Result(property = "seperatedNumber", column = "seperated_number"),
            @Result(property = "lectureCode", column = "lecture_code"),
            @Result(property = "professorId", column = "professor_id"),
            @Result(property = "maxStudentNumber", column = "max_student_number"),
            @Result(property = "curStudentNumber", column = "cur_student_number"),
            @Result(property = "lectureDTO",column = "lecture_code",one = @One(select = "mapper.LectureMapper.selectLectureByLectureCode")),
            @Result(property = "professorDTO",column = "professor_id",one = @One(select = "findProfessorById"))
    })
    List<OpenLectureDTO> findAllOpenLecutreJoin();//모든 개설 강좌 찾기(교과목과 교수 테이블에 조인한 결과)

    @Select("select * from professor where professor_id=#{professorId}")
    @Results(id="professorResultSet",value={
            @Result(property = "professorId", column = "professor_id"),
            @Result(property = "professorPw", column = "professor_pw"),
            @Result(property = "professorName", column = "professor_name"),
            @Result(property = "ssn", column = "SSN"),
            @Result(property = "eMail", column = "e_mail"),
            @Result(property = "professorPhoneNumber", column = "professor_phone_number"),
            @Result(property = "departmentNumber", column = "department_number")
    })
    ProfessorDTO findProfessorById(@Param("professorId") String professorId);

    @SelectProvider(type = persistence.mapper.OpenLectureSql.class, method = "selectOpenLectureByCondition")
    @ResultMap("OpenLectureJoinResult")
    List<OpenLectureDTO> findOpenLecutreJoinByCondition(@Param("professorId")String professorId,@Param("level") int level);

    @Select("select * from open_lecture join lecture on open_lecture.lecture_code=lecture.lecture_code where open_lecture_id=#{openLectureId}")
    @ResultMap("OpenLectureJoinResult")
    OpenLectureDTO findOpenLectureById(@Param("openLectureId")int openLectureId);

    @Update("UPDATE open_lecture set max_student_number=#{maxStudentNumber} where open_lecture_id=#{openLectureId}")
    int updateMaxStudentNumber(@Param("maxStudentNumber") int maxStudentNumber, @Param("openLectureId") int openLectureId); // 최대 수강 인원 변경(maxStudentNumber,openLectureId 가지는 객체)

    @InsertProvider(type = persistence.mapper.OpenLectureSql.class, method = "insertOpenLecture")
    int insertOpenLecture(OpenLectureDTO openLectureDTO);

    @Select("SELECT open_lecture_id FROM open_lecture WHERE seperated_number = #{seperatedNumber} and lecture_code = #{lectureCode}")
    @Result(property = "openLectureId", column = "open_lecture_id")
    int selectopenLectureId(@Param("seperatedNumber") int seperatedNumber, @Param("lectureCode") String lectureCode);

    @Select("SELECT count(*) FROM open_lecture WHERE seperated_number = #{seperatedNumber} and lecture_code = #{lectureCode}")
    boolean isExistopenLectureId(@Param("seperatedNumber") int seperatedNumber, @Param("lectureCode") String lectureCode);

    @Select("SELECT count(*) FROM open_lecture WHERE seperated_number = #{seperatedNumber} and lecture_code = #{lectureCode}")
    boolean checkopenLecture(@Param("seperatedNumber") int seperatedNumber, @Param("lectureCode") String lectureCode);


    @Select("select lecture_credit from open_lecture join lecture on open_lecture.lecture_code=lecture.lecture_code where open_lecture_id=#{openLectureId}")
    int getCredit(@Param("openLectureId")int openLectureId); // 선택된 개설 교과목 학점 가져오기


    @Select("select lecture_code from open_lecture where open_lecture_id=#{openLectureId}")
    String getLectureCode(@Param("openLectureId") int openLectureId ); //해당 open_lecture의 교과목 코드 리턴

    @Select("select * from open_lecture where open_lecture_id=#{openLectureId}")
    @Results(id="OpenLectureResult",value= {
            @Result(property = "openLectureId", column = "open_lecture_id"),
            @Result(property = "seperatedNumber", column = "seperated_number"),
            @Result(property = "lectureCode", column = "lecture_code"),
            @Result(property = "professorId", column = "professor_id"),
            @Result(property = "maxStudentNumber", column = "max_student_number"),
            @Result(property = "curStudentNumber", column = "cur_student_number"),
    })
    OpenLectureDTO getOpenLectureByOpenLectureId(@Param("openLectureId") int openLectureId);

    @UpdateProvider(type = persistence.mapper.OpenLectureSql.class, method = "updateOpenLectureByCondition")
    int updateOpenLectureByCondition(OpenLectureDTO openLectureDTO);

    @Delete("delete from open_lecture where seperated_number = #{seperatedNumber} and lecture_code = #{lectureCode}")
    int deleteOpenLecture(@Param("seperatedNumber") int seperatedNumber, @Param("lectureCode") String lectureCode);

    @UpdateProvider(type = persistence.mapper.OpenLectureSql.class, method = "updateOpenLecture")
    int updateOpenLecture(OpenLectureDTO openLectureDTO);
}
