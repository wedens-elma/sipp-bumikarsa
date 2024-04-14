package propensi.c06.sipp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import propensi.c06.sipp.model.Barang;

public interface BarangDb extends JpaRepository<Barang, String>{
    @Query("SELECT kodeBarang FROM Barang WHERE kodeBarang LIKE :code% ORDER BY kodeBarang DESC LIMIT 1")
    String findNextNumericIdByType(@Param("code") String code);

    boolean existsByNamaBarangAndIsDeleted(String namaBarang, Boolean isDeleted);
}
