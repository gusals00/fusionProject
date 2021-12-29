package persistence.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO {
    private String studentId;
    private String studentPw;
    private String studentName;
    private String ssn;
    private int studentLevel;
    private int departmentNumber;
    private DepartmentDTO departmentDTO;
}
