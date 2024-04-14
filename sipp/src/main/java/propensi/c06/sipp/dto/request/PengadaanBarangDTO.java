package propensi.c06.sipp.dto.request;

        import jakarta.validation.constraints.NotNull;
        import lombok.*;
        import propensi.c06.sipp.model.Barang;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class PengadaanBarangDTO {

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