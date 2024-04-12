package propensi.c06.sipp.repository;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import propensi.c06.sipp.model.Vendor;

@Repository
public interface VendorDb extends JpaRepository<Vendor, String> {
    Optional<Vendor> findByKodeVendor(String kodeVendor);

    @Query("SELECT v.kodeVendor FROM Vendor v WHERE v.kodeVendor LIKE CONCAT(:code, '%') ORDER BY v.kodeVendor DESC")
    List<String> findLastKodeVendorByCode(@Param("code") String code);

    @Query("SELECT v FROM Vendor v WHERE v.namaVendor = :namaVendor OR v.emailVendor = :emailVendor")
    Optional<Vendor> findByNamaVendorOrEmailVendor(@Param("namaVendor") String namaVendor, @Param("emailVendor") String emailVendor);

    boolean existsByNamaVendorAndIsDeleted(String namaVendor, Boolean isDeleted);

    @Query("SELECT v FROM Vendor v WHERE v.isDeleted = false")
    List<Vendor> findAllActiveVendors();
}

