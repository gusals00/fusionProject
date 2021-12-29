package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.dto.SyllabusWeekInfoDTO;

import java.util.List;

public interface SyllabusWeekInfoMapper {

    @Select("select * from syllabus_week_info")
    @Results(id="syllabusWeekInfoResultSet",value={
            @Result(property = "syllabusInfoId",column = "syllabus_info_id"),
            @Result(property = "syllabusWeek",column = "syllabus_week"),
            @Result(property = "syllabusSubject",column = "syllabus_subject"),
            @Result(property = "syllabusContent",column = "syllabus_content"),
            @Result(property = "syllabusAssignment",column = "syllabus_assignment"),
            @Result(property = "syllabusEvaluation",column = "syllabus_evaluation"),
            @Result(property = "syllabusId",column = "syllabus_id")
    })
    List<SyllabusWeekInfoDTO> findAllSyllabusWeekInfo();//모든 주별 강의 내용 리턴

    @Select("select * from syllabus_week_info where syllabus_id=#{syllabusId} order by syllabus_week")
    @ResultMap("syllabusWeekInfoResultSet")
    List<SyllabusWeekInfoDTO> findSyllabusWeekInfoBySyllabusId(@Param("syllabusId")int syllabusId); // 강의 계획서 id에 해당하는 주별 강의 내용이 주차 오름차순으로 정렬한 후 리턴

    @Insert("insert into syllabus_week_info(syllabus_week,syllabus_subject,syllabus_content,syllabus_assignment,syllabus_evaluation,syllabus_id) values(#{syllabusWeek},#{syllabusSubject},#{syllabusContent},#{syllabusAssignment},#{syllabusEvaluation},#{syllabusId})")
    int insertSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO);


    @Select("select count(*) from syllabus_week_info where syllabus_id=#{syllabusId} and syllabus_week=#{syllabusWeek}")
    boolean isExistSyllabusWeekInfo(@Param("syllabusId")int syllabusId,@Param("syllabusWeek")int syllabusWeek);

    @UpdateProvider(type = persistence.mapper.SyllabusWeekInfoSql.class, method = "updateSyllabusInfo")
    int updateSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO);


    @Delete("delete from syllabus_week_info where syllabus_id = #{syllabusId} and syllabus_week = #{syllabusWeek}")
    int deleteSyllabusWeekInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO);
}
