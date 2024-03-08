package propensi.c06.sipp.model;


import jakarta.persistence.*;
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

}

