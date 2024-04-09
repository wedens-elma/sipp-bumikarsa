package propensi.c06.sipp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.PengadaanBarang;
import propensi.c06.sipp.model.VendorBarang;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVendorRequestDTO {
    private String namaVendor;
    private String alamatVendor;
    private String emailVendor;
    private String nomorHandphoneVendor;
    private List<VendorBarang> listBarang;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class VendorBarangDTO {
        private String namaBarang;
        private Barang barang;
    }
}