package persistence.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private String adminId;
    private String adminPw;
}
