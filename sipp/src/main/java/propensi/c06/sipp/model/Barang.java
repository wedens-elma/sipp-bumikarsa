package propensi.c06.sipp.model;

import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    @Column(name = "deskripsi", nullable = false, length = 1000)
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

    @NotNull
    @Column(name = "stok_barang", nullable = false)
    private Integer stokBarang;

    @NotNull
    @Column(name = "stok_standar", nullable = false)
    private Integer standarStokBarang;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "barang", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BarangRencana> listBarangRencana;

    @OneToMany(mappedBy = "barang", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<PengadaanBarang> pengadaanBarang;

}
