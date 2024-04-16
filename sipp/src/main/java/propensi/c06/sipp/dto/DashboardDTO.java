package propensi.c06.sipp.dto;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardDTO {
    private Map<String, Double> pengeluaranBulanan; // Pengeluaran per bulan dalam satu tahun
    private Map<String, Double> pengeluaranTahunan; // Pengeluaran per tahun
    private int totalPengadaan; // Total pengadaan yang telah dilakukan
}
