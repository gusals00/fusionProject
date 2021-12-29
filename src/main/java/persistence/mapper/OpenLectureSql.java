package persistence.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import persistence.dto.OpenLectureDTO;

public class OpenLectureSql {
    public String selectOpenLectureByCondition(@Param("professorId")String professorId,@Param("level") int level){
        SQL sql=new SQL(){{
            SELECT("*");
            FROM("open_lecture");
            JOIN("lecture on open_lecture.lecture_code=lecture.lecture_code");
            if(professorId!=null){
                WHERE("professor_id=#{professorId}");
            }

            if(level!=0){
                AND();
                WHERE("lecture_level=#{level}");
            }
            ORDER_BY("lecture_level, open_lecture.lecture_code ,seperated_number");
        }};
        return sql.toString();
    }

    public String insertOpenLecture(OpenLectureDTO openLectureDTO) {
        SQL sql = new SQL() {{
            INSERT_INTO("open_lecture");
            VALUES("seperated_number, lecture_code, professor_id, max_student_number, cur_student_number", "#{seperatedNumber}, #{lectureCode}, #{professorId}, #{maxStudentNumber}, #{curStudentNumber}");
        }};

        return sql.toString();
    }

    public String updateOpenLectureByCondition(OpenLectureDTO openLectureDTO){
        SQL sql=new SQL(){{
            UPDATE("open_lecture");
            if(openLectureDTO.getProfessorId()!=null){
                SET("professor_id=#{professor_id}");
            }
            if(openLectureDTO.getMaxStudentNumber()!=0){
                SET("max_student_number=#{maxStudentNumber}");
            }
            WHERE("seperated_number= #{seperatedNumber} AND lecture_code= #{lectureCode}");
        }};
        return sql.toString();
    }

    public String updateOpenLecture(OpenLectureDTO openLectureDTO){
        SQL sql = new SQL() {{
            UPDATE("open_lecture");
            if(openLectureDTO.getProfessorId() != null){
                SET("professor_id = #{professorId}");
            }
            if(openLectureDTO.getMaxStudentNumber() > 0){
                SET("max_student_number = #{maxStudentNumber}");
            }
            if(openLectureDTO.getCurStudentNumber() >= 0){
                SET("cur_student_number = #{curStudentNumber}");
            }
            WHERE("seperated_number = #{seperatedNumber} and lecture_code = #{lectureCode}");
        }};

        return sql.toString();
    }
}


