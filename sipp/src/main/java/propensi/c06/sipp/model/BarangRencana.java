package propensi.c06.sipp.model;

import jakarta.validation.constraints.NotNull;
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
@Table(name="barangRencana")
public class BarangRencana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBarangRencana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rencana", referencedColumnName = "idRencana")
    private Rencana rencana;

    @NotNull
    @Column(name="kuantitas", nullable=false)
    private Integer kuantitas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barang", referencedColumnName = "kodeBarang")
    private Barang barang;

    private String namaBarang;
}
