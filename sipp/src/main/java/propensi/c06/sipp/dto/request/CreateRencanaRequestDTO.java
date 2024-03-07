package propensi.c06.sipp.dto.request;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import propensi.c06.sipp.model.BarangRencana;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateRencanaRequestDTO {
    private String vendor;
    private String user;
    private String namaRencana;
    private LocalDate expectedDate;
    private List<BarangRencana> listBarangRencana;
}
