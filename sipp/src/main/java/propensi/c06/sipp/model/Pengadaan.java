package propensi.c06.sipp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pengadaan")

public class Pengadaan {

    @Id
    @NotNull
    private String idPengadaan;


    @NotNull
    @Column(name="nama_pengadaan", nullable=false)
    private String namaPengadaan;

    @NotNull
    @Column(name="tanggal_pengadaan", nullable=false)
    private LocalDate tanggalPengadaan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kode_vendor", referencedColumnName = "kodeVendor")
    private Vendor vendor;

    @NotNull
    @Column(name = "diskon_keseluruhan")
    private int diskonKeseluruhan;

    @NotNull
    @Column(name = "shipment_status", nullable = false)
    private int shipmentStatus = 0;
    //shipment status 0 = in progress
    //shipment status 1 = arrived

    @NotNull
    @Column(name = "payment_status", nullable = false)
    private int paymentStatus = 0;
    //payment status 0 = not paid
    //payment status 1 = in progress
    //payment status 2 = paid


    @OneToMany(mappedBy = "pengadaan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PengadaanBarang> listPengadaanBarang;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "pengadaan", referencedColumnName = "idPengadaan")
//    private List<PengadaanBarang> listPengadaanBarang;

    @NotNull
    @Column(name="isDeleted", nullable=false)
    private Boolean isDeleted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rencana_id")
    private Rencana rencana;
}
