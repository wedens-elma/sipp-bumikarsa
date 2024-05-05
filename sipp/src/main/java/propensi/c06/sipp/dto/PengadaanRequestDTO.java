package propensi.c06.sipp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.Vendor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class PengadaanRequestDTO {

    private String idPengadaan;

    private String namaPengadaan;

    private String tanggalPengadaan;

    private Vendor vendor;

    private int diskonKeseluruhan;

    private int shipmentStatus = 0;

    private int paymentStatus = 0;

    private List<PengadaanBarang> listPengadaanBarang;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class PengadaanBarangDTO{

        private Long idPengadaanBarang;
        private int jumlahBarang;

        private int hargaBarang;

        private int diskonSatuan;

        private Barang barang;
    }

}
