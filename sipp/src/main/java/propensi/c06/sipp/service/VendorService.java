package propensi.c06.sipp.service;

import java.util.List;

import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.dto.request.UpdateVendorRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Vendor;

public interface VendorService {
    List<Vendor> getAllVendors();

    Vendor updateVendor(UpdateVendorRequestDTO dto);

    Vendor addVendor(CreateVendorRequestDTO vendorDto);
    Vendor getVendorDetail(String kodeVendor);

    void softDeleteVendor(String kodeVendor);

    boolean isVendorExistsAndNotDeleted(String namaVendor);

}
