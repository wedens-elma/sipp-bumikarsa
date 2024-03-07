package propensi.c06.sipp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="barang")
public class Barang {
    // Sesuaikan sama Rangga
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBarangRencana;

    @OneToOne(mappedBy = "barang", cascade = CascadeType.ALL)
    private BarangRencana barangRencana;
}
