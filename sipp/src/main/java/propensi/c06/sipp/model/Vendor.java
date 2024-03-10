package propensi.c06.sipp.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "vendor")
public class Vendor {
    @Id
    @NotNull
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
    @Column(name = "nomor_handphone_vendor", nullable = false)
    private String nomorHandphoneVendor;

    @Column(name = "is_deleted")
    private boolean is_deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pengadaan> listPengadaan;

}
