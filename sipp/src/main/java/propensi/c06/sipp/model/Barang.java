package propensi.c06.sipp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "barang")
public class Barang {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private String idBarang;

    @NotNull
    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @OneToMany(mappedBy = "barang2", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<PengadaanBarang> pengadaanBarang;

}
