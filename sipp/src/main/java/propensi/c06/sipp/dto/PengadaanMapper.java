package propensi.c06.sipp.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.dto.PengadaanRequestDTO;
import propensi.c06.sipp.model.PengadaanBarang;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PengadaanMapper {
    Pengadaan createPengadaanRequestDTOToPengadaan(PengadaanRequestDTO pengadaanRequestDTO);

   // @Mapping(source="idPengadaan", target = "idPengadaan")
    Pengadaan updatePengadaanRequestDTOToPengadaan(UpdatePengadaanRequestDTO updatePengadaanRequestDTO);

    UpdatePengadaanRequestDTO pengadaanToUpdatePengadaanRequestDTO(Pengadaan pengadaan);



}

