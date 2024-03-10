package propensi.c06.sipp.service;

import java.util.List;

import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.model.Vendor;

public interface VendorService {
    List<Vendor> getAllVendors();
    Vendor addVendor(CreateVendorRequestDTO vendorDto);
    Vendor getVendorDetail(String kodeVendor);
}
