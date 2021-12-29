package persistence.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PeriodDTO {
    private int periodId;
    private String periodName;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
}
