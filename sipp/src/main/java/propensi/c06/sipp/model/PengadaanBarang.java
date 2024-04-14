package propensi.c06.sipp.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "pengadaanBarang")
public class PengadaanBarang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPengadaanBarang;

    @NotNull
    @Column(name = "jumlah_barang")
    private int jumlahBarang;

    @NotNull
    @Column(name = "harga_barang")
    private int hargaBarang;

    @NotNull
    @Column(name = "diskon_satuan")
    private int diskonSatuan;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barang", referencedColumnName = "kodeBarang")
    private Barang barang;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengadaan", referencedColumnName = "idPengadaan")
    private Pengadaan pengadaan;

    private String namaBarang;

}
