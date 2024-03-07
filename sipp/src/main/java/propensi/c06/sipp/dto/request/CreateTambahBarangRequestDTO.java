package propensi.c06.sipp.dto.request;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CreateTambahBarangRequestDTO {
    private String namaBarang;
    private String deskripsiBarang;
    private Integer tipeBarang;
    private String dimensiBarang;
    private String beratBarang;
    private String fotoBarang;
    private Integer stokAwalBarang;
    private Integer standarStokBarang;
}