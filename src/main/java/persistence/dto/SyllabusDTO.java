package persistence.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusDTO {
    private int syllabusId;
    private String bookName;
    private String lectureGoal;
    private int openLectureId;

    private List<SyllabusWeekInfoDTO> syllabusWeekInfoDTOList;
    // 주별 강의 내용 객체 추가해놓고 mapper를 주별 강의 내용 조인해서 가져오는 걸로 변경하기.
}
