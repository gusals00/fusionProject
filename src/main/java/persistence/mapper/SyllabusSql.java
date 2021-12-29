package persistence.mapper;

import org.apache.ibatis.jdbc.SQL;
import persistence.dto.SyllabusDTO;

public class SyllabusSql {
    public String updateSyllabus(SyllabusDTO syllabusDTO){
        SQL sql=new SQL(){{
            UPDATE("syllabus");
            if(syllabusDTO.getBookName()!=null){
                SET("bookname=#{bookName}");
            }
            if(syllabusDTO.getLectureGoal()!=null){
                SET("lecture_goal=#{lectureGoal}");
            }
            WHERE("open_lecture_id= #{openLectureId}");
        }};
        return sql.toString();
    }
}
