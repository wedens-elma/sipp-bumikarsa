package propensi.c06.sipp.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name="logPengadaan")

public class LogPengadaan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLogPengadaan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pengadaan", referencedColumnName = "idPengadaan")
    private Pengadaan pengadaan;

    @NotNull
    @Column(name = "changedBy", nullable = false)
    private String changedBy;

    @NotNull
    @Column(name = "status", nullable =false)
    private String status; // status yg diubah, shipment atau payment

    @Column(name = "feedback")
    private String feedback;

    @NotNull
    @Column(name = "tanggalWaktu", nullable = false)
    private LocalDateTime tanggalWaktu;


}
