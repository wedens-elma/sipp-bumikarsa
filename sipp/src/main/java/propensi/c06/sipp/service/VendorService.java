package propensi.c06.sipp.service;

import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.model.Vendor;



import java.util.List;

public interface VendorService {
    void createVendor(CreateVendorRequestDTO createVendorRequestDTO);
    List<Vendor> getAllVendors();
    Vendor getVendorByCode(String kodeVendor);
    List<Vendor> getVendorByName(String namaVendor);
}