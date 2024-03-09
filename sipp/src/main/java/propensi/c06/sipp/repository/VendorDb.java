package propensi.c06.sipp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import propensi.c06.sipp.model.Vendor;

import java.util.List;

@Repository
public interface VendorDb extends JpaRepository<Vendor, String> {

    @Query("SELECT v.kodeVendor FROM Vendor v WHERE v.kodeVendor LIKE CONCAT(:code, '%') ORDER BY v.kodeVendor DESC")
    List<String> findLastKodeVendorByCode(@Param("code") String code);
}

