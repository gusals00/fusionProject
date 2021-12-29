package persistence.mapper;

import org.apache.ibatis.jdbc.SQL;
import persistence.dto.SyllabusWeekInfoDTO;

public class SyllabusWeekInfoSql {
    public String updateSyllabusInfo(SyllabusWeekInfoDTO syllabusWeekInfoDTO){
        SQL sql=new SQL(){{
            UPDATE("syllabus_week_info");
            if(syllabusWeekInfoDTO.getSyllabusSubject()!=null){
                SET("syllabus_subject=#{syllabusSubject}");
            }
            if(syllabusWeekInfoDTO.getSyllabusContent()!=null){
                SET("syllabus_content=#{syllabusContent}");
            }
            if(syllabusWeekInfoDTO.getSyllabusAssignment()!=null){
                SET("syllabus_assignment=#{syllabusAssignment}");
            }
            if(syllabusWeekInfoDTO.getSyllabusEvaluation()!=null){
                SET("syllabus_evaluation=#{syllabusEvaluation}");
            }
            WHERE("syllabus_id= #{syllabusId} and syllabus_week=#{syllabusWeek}");
        }};
        return sql.toString();
    }
}
