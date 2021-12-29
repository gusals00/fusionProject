package persistence.dto;

import lombok.*;
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusWeekInfoDTO {

    private int syllabusInfoId;
    private int syllabusWeek;
    private String syllabusSubject;
    private String syllabusContent;
    private String syllabusAssignment;
    private String syllabusEvaluation;
    private int syllabusId;
}
