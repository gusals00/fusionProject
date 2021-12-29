package persistence.dto;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorDTO{
    private String professorId;
    private String professorPw;
    private String professorName;
    private String ssn;
    private String eMail;
    private String professorPhoneNumber;
    private int departmentNumber;

    private DepartmentDTO departmentDTO;
}

