package propensi.c06.sipp.service;

import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.model.Vendor;

import java.util.List;

public interface VendorService {
    List<Vendor> getAllVendors();
    Vendor addVendor(CreateVendorRequestDTO vendorDto);
    Vendor getVendorDetail(String kodeVendor);
}
