package persistence.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class LectureRoomByTimeSql {
    public String insertLectureRoomByTimeMapper(@Param("lectureRoomId") int lectureRoomId, @Param("lectureTimeId") int lectureTimeId, @Param("openLectureId") int openLectureId) {
        SQL sql = new SQL() {{
            INSERT_INTO("lecture_room_by_time");
            VALUES("lecture_room_id, lecturetime_id, open_lecture_id", "#{lectureRoomId}, #{lectureTimeId}, #{openLectureId}");
        }};

        return sql.toString();
    }
}
