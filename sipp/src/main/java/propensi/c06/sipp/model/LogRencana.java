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
@Table(name="logRencana")
public class LogRencana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLogRencana;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rencana", referencedColumnName = "idRencana")
    private Rencana rencana;

    @NotNull
    @Column(name = "changedBy", nullable = false)
    private String changedBy;

    @NotNull
    @Column(name = "status", nullable =false)
    private String status; // disetujui, dibatalkan, dibuat, direalisasikan, dihapus

    @Column(name = "feedback")
    private String feedback;

    @NotNull
    @Column(name = "tanggalWaktu", nullable = false)
    private LocalDateTime tanggalWaktu;
}
