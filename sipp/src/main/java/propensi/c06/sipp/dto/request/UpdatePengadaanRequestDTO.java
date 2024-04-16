package propensi.c06.sipp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.Vendor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePengadaanRequestDTO{
    @NotNull
    private String idPengadaan;
    @NotNull
    private String namaPengadaan;
    @NotNull
    private String tanggalPengadaan;
    @NotNull
    private Vendor vendor;

    @NotNull
    private int diskonKeseluruhan;
    //DTO diakhir dihapus
    @NotNull
    private List<PengadaanBarang> listBarang = new ArrayList<>();

    @Data
    public static class PengadaanBarangDTO {
        @NotNull
        private Long idPengadaanBarang;
        @NotNull
        private int jumlahBarang;
        @NotNull
        private int hargaBarang;
        @NotNull
        private int diskonSatuan;
        @NotNull
        private Barang barang;
    }
}
