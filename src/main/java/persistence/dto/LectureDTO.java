package persistence.dto;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LectureDTO {
    private String lectureCode;
    private String lectureName;
    private int lectureLevel;
    private int lectureCredit;
}
