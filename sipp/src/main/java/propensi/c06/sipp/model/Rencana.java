package propensi.c06.sipp.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="rencana")
public class Rencana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRencana;

    @NotNull
    @Column(name = "vendor", nullable = false)
    // private Vendor vendor;
    private String Vendor;

    // // Pake faker
    // @NotNull
    // @Column(name = "user", nullable = false)
    // // private User user;
    // private String User;

    @NotNull
    @Column(name="namaRencana", nullable=false)
    private String namaRencana;

    @NotNull
    @Column(name="tanggalRencana", nullable=false)
    private Date expectedDate;

    @Column(name="feedback")
    private String feedback;

    @OneToMany(mappedBy = "rencana", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BarangRencana> listBarangRencana;

    @OneToMany(mappedBy = "rencana", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LogRencana> logRencana;

    @NotNull
    @Column(name="isDeleted", nullable=false)
    private Boolean isDeleted = false;
}
