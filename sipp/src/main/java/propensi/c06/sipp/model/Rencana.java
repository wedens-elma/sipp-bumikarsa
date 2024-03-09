package propensi.c06.sipp.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="rencana")
@JsonIgnoreProperties(value={"id_vendor", "vendor", "createdBy", "feedback", "listBarangRencana", "logRencana", "isDeleted"}, allowSetters = true)
public class Rencana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRencana;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vendor", referencedColumnName = "kodeVendor")
    private Vendor vendor;

    // Sesuaikan sama Wedens
    @NotNull
    @Column(name = "createdBy", nullable = false)
    private String createdBy; 

    @NotNull
    @Column(name="namaRencana", nullable=false)
    private String namaRencana; 

    @NotNull
    @Column(name="tanggalRencana", nullable=false)
    private LocalDate expectedDate; 

    @Column(name="feedback")
    private String feedback;

    @OneToMany(mappedBy = "rencana", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BarangRencana> listBarangRencana; 

    @OneToMany(mappedBy = "rencana", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LogRencana> logRencana;

    @NotNull
    @Column(name="isDeleted", nullable=false)
    private Boolean isDeleted = false;
}
