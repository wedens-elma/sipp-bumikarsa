package propensi.c06.sipp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserResponseDTO {
    private Long id;
    private String email;
    private String name;
    private String role;
}