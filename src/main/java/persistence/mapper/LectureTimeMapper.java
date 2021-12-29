package persistence.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import persistence.dto.LectureTimeDTO;

public interface LectureTimeMapper {
    @Select("SELECT lecturetime_id FROM lecture_time WHERE lecture_day = #{lectureDay} and lecture_period = #{lecturePeriod}")
    @Result(property = "lectureTimeId", column = "lecturetime_id")
    int selectLectureTimeId(@Param("lectureDay") String lectureDay, @Param("lecturePeriod") int lecturePeriod);

    @Select("SELECT * FROM lecture_time WHERE lecturetime_id = #{lectureTimeId}")
    @Results(value = {
            @Result(property = "lectureTimeId", column = "lecturetime_id"),
            @Result(property = "lectureDay", column = "lecture_day"),
            @Result(property = "lecturePeriod", column = "lecture_period")
    })
    LectureTimeDTO selectLectureTimeDTO(@Param("lectureTimeId") int lectureTimeId);
}
