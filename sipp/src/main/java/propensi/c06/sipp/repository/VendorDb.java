package propensi.c06.sipp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import propensi.c06.sipp.model.Vendor;

public interface VendorDb extends JpaRepository<Vendor, Long> {
}
