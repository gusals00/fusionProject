package persistence.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LectureHistoryDTO {
    private int lectureRegistrationId;
    private String studentId;
    private int openLectureId;

    private OpenLectureDTO openLectureDTO;
    private List<LectureRoomByTimeDTO> lectureRoomByTimeDTO;
}
