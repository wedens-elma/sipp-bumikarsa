package propensi.c06.sipp.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "barang", referencedColumnName = "idBarang")
    private Barang barang;

    // Pake faker
    @NotNull
    @Column(name = "user", nullable = false)
    // private User user;
    private String User;

    @NotNull
    @Column(name = "status", nullable =false)
    private String status;
    // approved, canceled, created, procured, deleted

    @NotNull
    @Column(name = "tanggal", nullable = false)
    private Date tanggal;
}
