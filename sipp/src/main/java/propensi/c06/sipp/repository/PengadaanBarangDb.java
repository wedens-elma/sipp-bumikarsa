package propensi.c06.sipp.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.model.PengadaanBarang;


@Repository
public interface PengadaanBarangDb extends JpaRepository<PengadaanBarang, Long>  {
    PengadaanBarang findByPengadaanAndBarang(Pengadaan pengadaan, Barang barang);

}
