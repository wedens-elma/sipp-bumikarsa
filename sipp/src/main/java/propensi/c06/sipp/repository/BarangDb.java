package propensi.c06.sipp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import propensi.c06.sipp.model.Barang;

import java.util.List;
import java.util.Optional;

public interface BarangDb extends JpaRepository<Barang, String>{
    @Query("SELECT kodeBarang FROM Barang WHERE kodeBarang LIKE :code% ORDER BY kodeBarang DESC LIMIT 1")
    String findNextNumericIdByType(@Param("code") String code);

    boolean existsByNamaBarangAndIsDeleted(String namaBarang, Boolean isDeleted);

    List<Barang> findByTipeBarang(Integer tipeBarang);
    List<Barang> findByNamaBarangContainingIgnoreCase(String namaBarang);

    @Query("SELECT b FROM Barang b WHERE b.kodeBarang = :kodeBarang AND b.isDeleted = false")
    Optional<Barang> findByIdAndIsDeletedFalse(@Param("kodeBarang") String kodeBarang);

    @Query("SELECT b FROM Barang b WHERE b.isDeleted = false AND b.kodeBarang = :kodeBarang")
    Optional<Barang> findByIdAndIsNotDeleted(@Param("kodeBarang") String kodeBarang);

    @Query("SELECT b.kodeBarang as kodeBarang, b.namaBarang as namaBarang FROM Barang b WHERE b.isDeleted = false")
    List<BarangInfo> findAllActiveBarangInfo();
}
