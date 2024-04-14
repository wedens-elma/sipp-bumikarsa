package propensi.c06.sipp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import propensi.c06.sipp.model.Barang;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVendorRequestDTO {
    private String namaVendor;
    private String alamatVendor;
    @Email
    private String emailVendor;
    private String nomorHandphoneVendor;
    private List<String> vendorBarang;
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class VendorBarangDTO {
        private String namaBarang;
        private Barang barang;
    }
}