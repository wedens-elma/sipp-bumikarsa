package propensi.c06.sipp.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBarangRequestDTO extends CreateTambahBarangRequestDTO {
    private String kodeBarang;

    @NotBlank(message = "Judul tidak boleh kosong")
    @Size(max = 100, message = "Judul maksimal 100 karakter")
    private String judul;
}