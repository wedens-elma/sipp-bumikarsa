package propensi.c06.sipp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import propensi.c06.sipp.model.Rencana;
import propensi.c06.sipp.model.Vendor;

import java.util.List;

@Repository
public interface RencanaDb extends JpaRepository<Rencana, Long> {
    List<Rencana> findAllByVendor(Vendor vendor);

}
