package propensi.c06.sipp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.repository.VendorDb;
import propensi.c06.sipp.dto.VendorMapper;

import java.util.List;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {

    @Autowired
    private  VendorDb vendorDb;
    @Autowired
    private VendorMapper vendorMapper;

    @Override
    public List<Vendor> getAllVendors() {
        return vendorDb.findAll();
    }

    @Override
    public Vendor getVendorDetail(String kodeVendor) {
        for (Vendor vendor : getAllVendors()) {
            if (vendor.getKodeVendor().equals(kodeVendor)) {
                return vendor;
            }
        }
        return null;
    }

    @Override
    public Vendor addVendor(CreateVendorRequestDTO vendorDto) {
        Vendor vendor = new Vendor();

        String kodeVendorPrefix = "VEND-";
        List<String> lastKodeVendors = vendorDb.findLastKodeVendorByCode(kodeVendorPrefix + "%");

        String lastKodeVendor = (lastKodeVendors.isEmpty()) ? null : lastKodeVendors.get(0);

        String newKodeVendor = generateKodeVendor(kodeVendorPrefix, lastKodeVendor);
        vendor.setKodeVendor(newKodeVendor);

        vendor.setNamaVendor(vendorDto.getNamaVendor());
        vendor.setEmailVendor(vendorDto.getEmailVendor());
        vendor.setNomorHandphoneVendor(vendorDto.getNomorHandphoneVendor());

        vendorDb.save(vendor);

        return vendor;
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
