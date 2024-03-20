package propensi.c06.sipp.dto;

import org.mapstruct.Mapper;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.dto.PengadaanRequestDTO;

@Mapper(componentModel = "spring")
public interface PengadaanMapper {
    Pengadaan createPengadaanRequestDTOToPengadaan(PengadaanRequestDTO pengadaanRequestDTO);
}
