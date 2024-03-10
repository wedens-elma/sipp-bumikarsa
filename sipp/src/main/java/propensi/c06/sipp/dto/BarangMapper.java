package propensi.c06.sipp.dto;

import org.mapstruct.Mapper;

import propensi.c06.sipp.dto.request.CreateTambahBarangRequestDTO;
import propensi.c06.sipp.dto.response.ReadBarangResponseDTO;
import propensi.c06.sipp.model.Barang;

@Mapper(componentModel = "spring")
public interface BarangMapper {
    Barang createTambahBarangRequestDTO(CreateTambahBarangRequestDTO createTambahBarangRequestDTO);

    // ReadBarangResponseDTO barangToReadBarangResponseDTO(Barang barang);

}
