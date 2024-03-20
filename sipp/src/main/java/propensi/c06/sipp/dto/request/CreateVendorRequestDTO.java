package propensi.c06.sipp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateVendorRequestDTO {
    private String namaVendor;
    private String alamatVendor;
//    private List<CreateTambahBarangRequestDTO> barangYangAkanDijual;
    private String emailVendor;
    private String nomorHandphoneVendor;
}