package propensi.c06.sipp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import propensi.c06.sipp.dto.VendorMapper;
import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.dto.request.UpdateVendorRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.repository.VendorDb;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {
    private static final Logger logger = LoggerFactory.getLogger(VendorServiceImpl.class);

    @Autowired
    private  VendorDb vendorDb;
    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private BarangService barangService;
    @Override
    public List<Vendor> getAllVendors() {
        return vendorDb.findAll();
    }

    @Override
    public Vendor updateVendor(Vendor vendorFromDto) {
        Vendor existingVendor = vendorDb.findByKodeVendor(vendorFromDto.getKodeVendor()).orElse(null);
        if (existingVendor == null) {
            logger.warn("Vendor with kodeVendor: {} not found", vendorFromDto.getKodeVendor());
            return null;
        }

        existingVendor.setEmailVendor(vendorFromDto.getEmailVendor());
        existingVendor.setNomorHandphoneVendor(vendorFromDto.getNomorHandphoneVendor());
        List<Barang> updatedBarangList = new ArrayList<>();
        if (vendorFromDto.getBarangList() != null) {
            for (Barang barang : vendorFromDto.getBarangList()) {
                Barang existingBarang = barangService.getBarangById(barang.getKodeBarang());
                if (existingBarang != null) {
                    updatedBarangList.add(existingBarang);
                } else {
                    logger.warn("Barang with ID: {} not found", barang.getKodeBarang());
                }
            }
            existingVendor.setBarangList(updatedBarangList);
        }
        Vendor updatedVendor = vendorDb.save(existingVendor);
        if (updatedVendor == null) {
            logger.error("Failed to update vendor with kodeVendor: {}", existingVendor.getKodeVendor());
        } else {
            logger.info("Successfully updated vendor: {}", updatedVendor.getKodeVendor());
        }
        return updatedVendor;
    }

    @Override
    public Vendor getVendorDetail(String kodeVendor) {
        Vendor vendor = vendorDb.findByKodeVendor(kodeVendor)
                .orElse(null);

        if (vendor != null) {
            logger.info("Fetched vendor: {}", vendor.getNamaVendor());
            logger.info("Barang list size: {}", vendor.getBarangList().size());
        } else {
            logger.warn("Vendor not found for kodeVendor: {}", kodeVendor);
        }

        return vendor;
    }



    @Override
    public Vendor addVendor(CreateVendorRequestDTO vendorDto) {
        // Check if a vendor with the same name or email already exists
        Optional<Vendor> existingVendor = vendorDb.findByNamaVendorOrEmailVendor(vendorDto.getNamaVendor(), vendorDto.getEmailVendor());
        if (existingVendor.isPresent()) {
            throw new IllegalArgumentException("A vendor with the same name or email already exists.");
        }

        Vendor vendor = new Vendor();
        String kodeVendorPrefix = "VEND-";
        List<String> lastKodeVendors = vendorDb.findLastKodeVendorByCode(kodeVendorPrefix + "%");
        String lastKodeVendor = (lastKodeVendors.isEmpty()) ? null : lastKodeVendors.get(0);
        String newKodeVendor = generateKodeVendor(kodeVendorPrefix, lastKodeVendor);

        vendor.setKodeVendor(newKodeVendor);
        vendor.setNamaVendor(vendorDto.getNamaVendor());
        vendor.setEmailVendor(vendorDto.getEmailVendor());
        vendor.setNomorHandphoneVendor(vendorDto.getNomorHandphoneVendor());

        if (vendorDto.getVendorBarang() != null) {
            List<Barang> barangList = new ArrayList<>();
            for (String barangId : vendorDto.getVendorBarang()) {
                Barang existingBarang = barangService.getBarangById(barangId);
                if (existingBarang != null) {
                    barangList.add(existingBarang);
                }
            }
            vendor.setBarangList(barangList);
        }

        return vendorDb.save(vendor);
    }


    private String generateKodeVendor(String prefix, String lastKodeVendor) {
        int nextId = 1;
        if (lastKodeVendor != null && !lastKodeVendor.isEmpty()) {
            String numericPart = lastKodeVendor.substring(prefix.length());
            nextId = Integer.parseInt(numericPart) + 1;
        }
        return prefix + String.format("%03d", nextId);
    }

}
