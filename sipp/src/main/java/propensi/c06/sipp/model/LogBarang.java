package propensi.c06.sipp.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="logBarang")

public class LogBarang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLogBarang;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "barang", referencedColumnName = "kodeBarang")
    private Barang barang;

    @NotNull
    @Column(name = "changedBy", nullable = false)
    private String changedBy;

    @NotNull
    @Column(name = "action", nullable =false)
    private String action; // create, update, delete

    @Column(name = "deskripsi")
    private String deskripsi;

    @Column(name = "oldValue")
    private Integer oldValue;

    @Column(name = "newValue")
    private Integer newValue;

    @NotNull
    @Column(name = "tanggalWaktu", nullable = false)
    private LocalDateTime tanggalWaktu;

}
