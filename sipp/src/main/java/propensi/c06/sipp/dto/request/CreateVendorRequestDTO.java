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

    @NotBlank(message = "Nama vendor tidak boleh kosong")
    private String namaVendor;

    @NotBlank(message = "Alamat vendor tidak boleh kosong")
    private String alamatVendor;

    private List<CreateTambahBarangRequestDTO> barangYangAkanDijual;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    @Size(max = 50, message = "Email tidak boleh lebih dari 50 karakter")
    private String email;

    @NotBlank(message = "Nomor telepon tidak boleh kosong")
    @Size(min = 10, max = 15, message = "Nomor telepon harus antara 10 sampai 15 karakter")
    private String noTelp;
}