package propensi.c06.sipp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "vendorBarang")
public class VendorBarang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVendorBarang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor", referencedColumnName = "kodeVendor")
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barang", referencedColumnName = "kodeBarang")
    private Barang barang;

    private String namaBarang;
}
