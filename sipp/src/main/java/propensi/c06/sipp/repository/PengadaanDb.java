package propensi.c06.sipp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import propensi.c06.sipp.model.Pengadaan;


@Repository
public interface PengadaanDb extends JpaRepository<Pengadaan, String>  {
    @Query("SELECT idPengadaan FROM Pengadaan WHERE idPengadaan LIKE :code% ORDER BY idPengadaan DESC LIMIT 1")
    String findCodePengadaan(@Param("code") String code);
}
