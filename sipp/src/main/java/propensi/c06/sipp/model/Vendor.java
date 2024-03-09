package propensi.c06.sipp.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.mapping.Set;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendor")
public class Vendor {

    @Id
    @NotNull
    @Size(max = 20)
    private String kodeVendor;

    @NotNull
    @Column(name = "nama_vendor", nullable = false)
    private String namaVendor;

    @NotNull
    @Email
    @Size(max = 50)
    @Column(name = "email_vendor", nullable = false)
    private String emailVendor;

    @NotNull
    @Size(min = 10, max = 15)
    @Column(name = "nomor_handphone_vendor", nullable = false)
    private String nomorHandphoneVendor;

    @ManyToMany
    @JoinTable(
            name = "vendor_barang",
            joinColumns = @JoinColumn(name = "kode_vendor", referencedColumnName = "kodeVendor"),
            inverseJoinColumns = @JoinColumn(name = "kode_barang", referencedColumnName = "kodeBarang") // Perbarui ini sesuai dengan kunci primer dari Barang
    )
    private List<Barang> barangYangDimiliki;

    @Column(name = "is_deleted")
    private boolean is_deleted = Boolean.FALSE;
}

