package propensi.c06.sipp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import propensi.c06.sipp.repository.BarangDb;
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

    @Autowired
    private BarangDb barangDb;
    @Override
    public List<Vendor> getAllVendors() {
        return vendorDb.findAllActiveVendors();
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

//    // yang pertama
//        public Vendor updateVendor(UpdateVendorRequestDTO dto) throws IllegalArgumentException {
//            Vendor existingVendor = vendorDb.findByKodeVendor(dto.getKodeVendor())
//                    .orElseThrow(() -> new IllegalArgumentException("Vendor not found with kodeVendor: " + dto.getKodeVendor()));
//
//            boolean exists = vendorDb.existsByEmailOrPhoneExcludingVendor(dto.getEmailVendor(), dto.getNomorHandphoneVendor(), dto.getKodeVendor());
//            if (exists) {
//                throw new IllegalArgumentException("Another active vendor with the same email or phone number already exists.");
//            }
//
//            existingVendor.setEmailVendor(dto.getEmailVendor());
//            existingVendor.setNomorHandphoneVendor(dto.getNomorHandphoneVendor());
//            updateVendorBarangList(existingVendor, dto.getBarangList());
//
//            return vendorDb.save(existingVendor);
//        }
//
//        private void updateVendorBarangList(Vendor vendor, List<String> newBarangListCodes) {
//            List<Barang> updatedBarangList = newBarangListCodes.stream()
//                    .map(barangDb::findById)
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .collect(Collectors.toList());
//            vendor.getBarangList().clear();
//            vendor.getBarangList().addAll(updatedBarangList);
//        }
//
//

    @Override
    public Vendor updateVendor(UpdateVendorRequestDTO dto) throws IllegalArgumentException {
        Vendor existingVendor = vendorDb.findByKodeVendor(dto.getKodeVendor())
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found with kodeVendor: " + dto.getKodeVendor()));

        boolean exists = vendorDb.existsByEmailOrPhoneExcludingVendor(dto.getEmailVendor(), dto.getNomorHandphoneVendor(), dto.getKodeVendor());
        if (exists) {
            throw new IllegalArgumentException("Another active vendor with the same email or phone number already exists.");
        }

        existingVendor.setEmailVendor(dto.getEmailVendor());
        existingVendor.setNomorHandphoneVendor(dto.getNomorHandphoneVendor());
        if (dto.getBarangList() != null && !dto.getBarangList().isEmpty()) {
            updateVendorBarangList(existingVendor, dto.getBarangList());
        }

        return vendorDb.save(existingVendor);
    }

    private void updateVendorBarangList(Vendor vendor, List<String> newBarangListCodes) {
        List<Barang> updatedBarangList = newBarangListCodes.stream()
                .map(barangDb::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        vendor.getBarangList().clear();
        vendor.getBarangList().addAll(updatedBarangList);
    }




    @Override
    public Vendor addVendor(CreateVendorRequestDTO vendorDto) {
        Optional<Vendor> existingVendor = vendorDb.findByNamaVendorOrEmailVendor(vendorDto.getNamaVendor(), vendorDto.getEmailVendor());
        if (existingVendor.isPresent()) {
            throw new IllegalArgumentException("Nama atau email vendor sudah ada.");
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

    @Override
    public void softDeleteVendor(String kodeVendor) {
        Vendor vendor = vendorDb.findByKodeVendor(kodeVendor).orElse(null);
        if (vendor != null){
            vendor.setIsDeleted(true);
            vendorDb.save(vendor);
        }
    }

    @Override
    public boolean isVendorExistsAndNotDeleted(String namaVendor) {
        return vendorDb.existsByNamaVendorAndIsDeleted(namaVendor, false);
    }
}
