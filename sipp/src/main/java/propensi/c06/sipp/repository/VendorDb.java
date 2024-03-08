package propensi.c06.sipp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import propensi.c06.sipp.model.Vendor;

import java.util.List;

@Repository
public interface VendorDb extends JpaRepository<Vendor, String> {

    @Query("SELECT v FROM Vendor v WHERE v.kodeVendor LIKE %:code%")
    List<Vendor> findByKodeVendorContaining(@Param("code") String code);

    @Query("SELECT v FROM Vendor v WHERE v.namaVendor LIKE %:name%")
    List<Vendor> findByNamaVendorContaining(@Param("name") String name);

    @Query("SELECT v.kodeVendor FROM Vendor v WHERE v.kodeVendor LIKE :code% ORDER BY v.kodeVendor DESC")
    String findLastVendorCodeByPrefix(@Param("code") String code);
}
