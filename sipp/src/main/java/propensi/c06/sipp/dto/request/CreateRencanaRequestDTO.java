package propensi.c06.sipp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Vendor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateRencanaRequestDTO {
    private Vendor vendor;
    private String createdBy = "User1";
    private String namaRencana;
    private String expectedDate;
    private List<BarangRencanaDTO> listBarangRencana;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class BarangRencanaDTO {
        private Integer kuantitas;
        private Barang barang;
    }
}
