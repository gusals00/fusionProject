package persistence.mapper;

import org.apache.ibatis.annotations.*;
import persistence.dto.LectureRoomDTO;

public interface LectureRoomMapper {
    @Select("SELECT lecture_room_id FROM lecture_room WHERE building_name = #{buildingName} and lectureroom_number = #{lectureRoomNumber}")
    @Result(property = "lectureRoomId", column = "lecture_room_id")
    int selectLectureRoomId(@Param("buildingName") String buildingName, @Param("lectureRoomNumber") int lectureRoomNumber);

    @Select("SELECT * FROM lecture_room WHERE lecture_room_id = #{lectureRoomId}")
    @Results(value = {
            @Result(property = "lectureRoomId", column = "lecture_room_id"),
            @Result(property = "buildingName", column = "building_name"),
            @Result(property = "lectureRoomNumber", column = "lectureroom_number")
    })
    LectureRoomDTO selectLectureRoomDTO(@Param("lectureRoomId") int lectureRoomId);

    @Insert("insert into lecture_room(building_name,lectureroom_number) values (#{name},#{number})")
    int insertRoom(@Param("name")String name,@Param("number")int number);
}
