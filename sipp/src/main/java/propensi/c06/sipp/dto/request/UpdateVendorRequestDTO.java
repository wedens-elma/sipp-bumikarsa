package propensi.c06.sipp.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.PengadaanBarang;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateVendorRequestDTO extends CreateVendorRequestDTO {
    private String kodeVendor;
    @Email
    private String emailVendor;
    private String nomorHandphoneVendor;
    private List<String> barangList;

}
