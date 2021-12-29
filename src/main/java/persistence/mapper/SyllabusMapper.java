package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.dto.SyllabusDTO;

import java.util.List;

public interface SyllabusMapper {

    @Select("select * from syllabus")
    @Results(id="syllabusResultSet",value={
            @Result(property = "syllabusId",column = "syllabus_id"),
            @Result(property = "bookName",column = "bookname"),
            @Result(property = "lectureGoal",column = "lecture_goal"),
            @Result(property = "openLectureId",column = "open_lecture_id")
    })
    List<SyllabusDTO> findAllSyllabus();//모든 강의 계획서 리턴

    @Select("select * from syllabus where open_lecture_id=#{openLectureId}")
    @ResultMap("syllabusResultSet")
    SyllabusDTO findSyllabusByOpenLectureId(@Param("openLectureId") int openLectureId); //개설 강좌에 해당하는 강의 계획서 리턴

    @Select("select * from syllabus where open_lecture_id=#{openLectureId}")
    @Results(id="syllabusJoinResultSet",value={
            @Result(property = "syllabusId",column = "syllabus_id"),
            @Result(property = "bookName",column = "bookname"),
            @Result(property = "lectureGoal",column = "lecture_goal"),
            @Result(property = "openLectureId",column = "open_lecture_id"),
            @Result(property = "syllabusWeekInfoDTOList",column = "syllabus_id",many = @Many(select = "persistence.mapper.SyllabusWeekInfoMapper.findSyllabusWeekInfoBySyllabusId"))
    })
    SyllabusDTO findSyllabusJoinWithWeekInfoByOpenLectureId(@Param("openLectureId") int openLectureId);//개설 강좌에 해당하는 강의계획서와 주별 강의 내용 리턴

    @Select("select count(*) from syllabus where open_lecture_id=#{openLectureid}")
    boolean isSyllabusExist(@Param("openLectureid") int openLectureid); //해당 개설 교과목에 대한 강의계획서 존재 여부 리턴

    @Insert("insert into syllabus(bookname,lecture_goal,open_lecture_id) values(#{bookName},#{lectureGoal},#{openLectureId})")
    int insertSyllabus(SyllabusDTO syllabusDTO); // 강의 계획서 삽입

    @UpdateProvider(type = persistence.mapper.SyllabusSql.class, method = "updateSyllabus")
    int updateSyllabus(SyllabusDTO syllabusDTO); // 깅의 계획서 내용 변경(syllabusId를 가지고 강의계획서 찾아서 변경)

}
