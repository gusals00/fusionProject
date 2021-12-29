package persistence.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LectureTimeDTO {
    private int lectureTimeId;
    private String lectureDay;
    private int lecturePeriod;
}
