package propensi.c06.sipp.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import propensi.c06.sipp.dto.request.CreateVendorRequestDTO;
import propensi.c06.sipp.dto.request.UpdateVendorRequestDTO;
import propensi.c06.sipp.model.Vendor;
import propensi.c06.sipp.model.Barang;
import propensi.c06.sipp.repository.BarangDb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface VendorMapper {
    Vendor createVendorRequestDTOToVendor(CreateVendorRequestDTO createVendorRequestDTO);

    @Mapping(target = "barangList", expression = "java(mapBarangListToStringList(vendor.getBarangList()))")
    UpdateVendorRequestDTO vendorToUpdateVendorRequestDTO(Vendor vendor);
    @Mappings({
            @Mapping(target="emailVendor", source="dto.emailVendor"),
            @Mapping(target="nomorHandphoneVendor", source="dto.nomorHandphoneVendor"),
            @Mapping(target="barangList", ignore=true)
    })    Vendor updateVendorRequestDTOToVendor(UpdateVendorRequestDTO dto);

    default List<String> mapBarangListToStringList(List<Barang> barangList) {
        if (barangList != null) {
            return barangList.stream()
                    .map(Barang::getKodeBarang)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}

