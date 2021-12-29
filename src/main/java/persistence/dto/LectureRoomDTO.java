package persistence.dto;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LectureRoomDTO {
    private int lectureRoomId;
    private String buildingName;
    private int lectureRoomNumber;
}
