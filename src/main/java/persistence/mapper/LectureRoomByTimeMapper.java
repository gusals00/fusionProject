package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.dto.LectureRoomByTimeDTO;

import java.util.List;

public interface LectureRoomByTimeMapper {
    @InsertProvider(type = persistence.mapper.LectureRoomByTimeSql.class, method = "insertLectureRoomByTimeMapper")
    int insertLectureRoomByTimeMapper(@Param("lectureRoomId") int lectureRoomId, @Param("lectureTimeId") int lectureTimeId, @Param("openLectureId") int openLectureId);

    @Select("SELECT count(*) FROM lecture_room_by_time WHERE lecture_room_id=#{lectureRoomId} and lecturetime_id = #{lectureTimeId}")
    boolean checkUsingLectureRoomId(@Param("lectureTimeId") int lectureTimeId, @Param("lectureRoomId") int lectureRoomId);

    @Update("update lecture_room_by_time set lecture_room_id=#{changeRoomId} where lecture_room_id=#{lectureRoomId} and lecturetime_id=#{lectureTimeId}")
    int updateLectureRoom(@Param("lectureTimeId") int lectureTimeId, @Param("lectureRoomId") int lectureRoomId, @Param("changeRoomId") int changeRoomId);

    @Update("update lecture_room_by_time set lecturetime_id = #{changeTimeId} where lecture_room_id=#{lectureRoomId} and lecturetime_id=#{lectureTimeId}")
    int updateLectureTime(@Param("lectureRoomId") int lectureRoomId, @Param("lectureTimeId") int lectureTimeId, @Param("changeTimeId") int changeTimeId);


    @Select("SELECT * FROM lecture_room_by_time WHERE open_lecture_id = #{openLectureId}")
    @Results(id="LectureRoomByTimeJoinResult",value={
            @Result(property = "usingLectureRoomId", column = "using_lecture_room_id"),
            @Result(property = "lectureRoomId", column = "lecture_room_id"),
            @Result(property = "lectureTimeId", column = "lecturetime_id"),
            @Result(property = "openLectureId", column = "open_lecture_id"),
            @Result(property = "lectureRoomDTO",column = "lecture_room_id",one = @One(select = "persistence.mapper.LectureRoomMapper.selectLectureRoomDTO")),
            @Result(property = "lectureTimeDTO",column = "lecturetime_id",one = @One(select = "persistence.mapper.LectureTimeMapper.selectLectureTimeDTO"))
    })
    List<LectureRoomByTimeDTO> selectAllLectureRoomByTime(@Param("openLectureId") int openLectureId);

    @Select("SELECT * FROM lecture_room_by_time WHERE open_lecture_id = #{openLectureId} and lecture_room_id = #{lectureRoomId} and lecturetime_id = #{lectureTimeId}")
    @ResultMap("LectureRoomByTimeJoinResult")
    LectureRoomByTimeDTO selectlectureRoombyTimeDTO(@Param("openLectureId") int openLectureId, @Param("lectureRoomId") int lectureRoomId, @Param("lectureTimeId") int lectureTimeId);

}
