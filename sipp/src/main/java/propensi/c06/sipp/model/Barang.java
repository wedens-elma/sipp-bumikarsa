package propensi.c06.sipp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "barang")
public class Barang {
    
    @Id
    @NotNull
    @Size(max = 20)
    private String kodeBarang;

    @NotNull
    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @NotNull
    @Column(name = "deskripsi", nullable = false)
    private String deskripsiBarang;

    @NotNull
    @Column(name = "tipe_barang", nullable = false)
    private Integer tipeBarang;

    @Column(name = "dimensi_barang")
    private String dimensiBarang;

    @Column(name = "berat_barang")
    private String beratBarang;

    //Belum tau caranya
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image64")
    private String imageBase64;

    @NotNull
    @Column(name = "stok_barang", nullable = false)
    private Integer stokBarang;

    @NotNull
    @Column(name = "stok_standar", nullable = false)
    private Integer standarStokBarang;

}
