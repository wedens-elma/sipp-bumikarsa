package propensi.c06.sipp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatePasswordRequestDTO {
    String passwordLama;
    String passwordBaru;
    String konfirmasi;
}
