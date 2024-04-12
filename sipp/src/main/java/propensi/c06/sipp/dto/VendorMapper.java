package propensi.c06.sipp.dto;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.dto.request.UpdateBarangRequestDTO;
import propensi.c06.sipp.dto.request.UpdateVendorRequestDTO;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.model.Vendor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    Vendor createVendorRequestDTOToVendor(CreateVendorRequestDTO createVendorRequestDTO);

    UpdateVendorRequestDTO vendorToUpdateVendorRequestDTO(Vendor vendor);

    Vendor updateVendorRequestDTOToVendor(UpdateVendorRequestDTO updateVendorRequestDTO);

}

