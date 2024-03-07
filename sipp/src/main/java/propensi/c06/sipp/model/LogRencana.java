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

    // Pake faker
    @NotNull
    @Column(name = "user", nullable = false)
    // private User user;
    private String User;

    @NotNull
    @Column(name = "status", nullable =false)
    private String status; // approved, canceled, created, procured, deleted

    @NotNull
    @Column(name = "tanggalWaktu", nullable = false)
    private LocalDateTime tanggalWaktu;
}
