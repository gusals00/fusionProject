package persistence.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OpenLectureDTO{
    private int openLectureId;
    private int seperatedNumber;
    private String lectureCode;
    private String professorId;
    private int maxStudentNumber;
    private int curStudentNumber;

    private LectureDTO lectureDTO;
    private ProfessorDTO professorDTO;

}
