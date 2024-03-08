package propensi.c06.sipp.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vendor", referencedColumnName = "idVendor")
    private Vendor vendor;

    // // Pake faker
    @NotNull
    @Column(name = "createdBy", nullable = false)
    // // private User user;
    private String createdBy; // INI

    @NotNull
    @Column(name="namaRencana", nullable=false)
    private String namaRencana; // INI

    @NotNull
    @Column(name="tanggalRencana", nullable=false)
    private LocalDate expectedDate; // INI

    @Column(name="feedback")
    private String feedback;

    @OneToMany(mappedBy = "rencana", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BarangRencana> listBarangRencana; // INI

    @OneToMany(mappedBy = "rencana", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LogRencana> logRencana;

    @NotNull
    @Column(name="isDeleted", nullable=false)
    private Boolean isDeleted = false;
}
