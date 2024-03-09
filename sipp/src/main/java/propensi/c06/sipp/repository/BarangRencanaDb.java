package propensi.c06.sipp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.BarangRencana;
import propensi.c06.sipp.model.Rencana;

@Repository
public interface BarangRencanaDb extends JpaRepository<BarangRencana, Long> {
    BarangRencana findByRencanaAndBarang(Rencana rencana, Barang barang);
}
