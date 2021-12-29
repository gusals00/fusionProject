package persistence.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LectureRoomByTimeDTO {
    private int usingLectureRoomId;
    private int lectureRoomId;
    private int lectureTimeId;
    private int openLectureId;

    private LectureRoomDTO lectureRoomDTO;
    private LectureTimeDTO lectureTimeDTO;
    private OpenLectureDTO openLectureDTO;
}
