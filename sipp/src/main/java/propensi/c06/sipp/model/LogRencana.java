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

    /* nama procurement, -> rencana
    vendor, -> rencana
    items (jlh barang), -> rencana
    status, v 
    expected date, -> rencana
    last updated (nama, tanggal, jam) 
        -> approved v, canceled v, created v, procured v, deleted v */

    @NotNull
    @Column(name="status", nullable=false)
    private String status;

    // Pengubah? / Created By?

    @NotNull
    @Column(name="tanggalDibuat", nullable=false)
    private String tanggalDibuat; // tanggal created

    @Column(name="tanggalUbahStatus")
    private Date tanggalUbahStatus; // tanggal canceled/approved

    @Column(name="tanggalRealisasi")
    private Date tanggalRealisasi; // tanggal procured

    @Column(name="tanggalDeleted")
    private Date tanggalDihapus; // tanggal deleted
}
