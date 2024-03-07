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

    // Log Rencana? (rencana 1 : N log rencana)
    @NotNull
    @Column(name="logRencana", nullable=false)
    private LogRencana logRencana;

    // Vendor? -> vendor

    // Created By? -> user

    @NotNull
    @Column(name="namaRencana", nullable=false)
    private String namaRencana;

    @NotNull
    @Column(name="namaPembuat", nullable=false)
    private String namaPembuat;

    // @NotNull
    // @Column(name="status", nullable=false)
    // private String status;

    // @NotNull
    // @Column(name="tanggalDibuat", nullable=false)
    // private String tanggalDibuat;

    @NotNull
    @Column(name="tanggalRencana", nullable=false)
    private Date tanggalRencana;

    // @Column(name="tanggalRealisasi")
    // private Date tanggalRealisasi; 

    // @Column(name="tanggalUbahStatus")
    // private Date tanggalUbahStatus;

    @OneToMany(mappedBy = "rencana", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BarangRencana> listBarangRencana;

    @Column(name="feedback")
    private String feedback;

    @NotNull
    @Column(name="isDeleted", nullable=false)
    private Boolean isDeleted = false;
}
