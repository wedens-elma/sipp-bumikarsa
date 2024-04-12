package propensi.c06.sipp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import propensi.c06.sipp.model.Barang;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVendorRequestDTO extends CreateVendorRequestDTO {
    // yang bisa diganti cuma email, nomor  handphone, dan nama barangnya
    private String kodeVendor;
    private String emailVendor;
    private String nomorHandphoneVendor;
    private List<String> addBarang;
    private List<String> removeBarang;
    private List<Barang> currentBarang;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VendorBarangDTO {
        private String namaBarang;
        private Barang barang;
    }
}
