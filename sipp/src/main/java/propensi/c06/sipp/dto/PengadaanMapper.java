package propensi.c06.sipp.dto;

import org.mapstruct.Mapper;
import propensi.c06.sipp.dto.request.UpdatePengadaanRequestDTO;
import propensi.c06.sipp.model.Pengadaan;
import propensi.c06.sipp.dto.PengadaanRequestDTO;

@Mapper(componentModel = "spring")
public interface PengadaanMapper {
    Pengadaan createPengadaanRequestDTOToPengadaan(PengadaanRequestDTO pengadaanRequestDTO);
    Pengadaan updatePengadaanRequestDTOToPengadaan(UpdatePengadaanRequestDTO updatePengadaanRequestDTO);
    //PengadaanRequestDTO pengadaanToPengadaanRequestDTO(Pengadaan pengadaan);
    default PengadaanRequestDTO pengadaanToPengadaanRequestDTO(Pengadaan pengadaan) {
        PengadaanRequestDTO dto = new PengadaanRequestDTO();
        dto.setIdPengadaan(pengadaan.getIdPengadaan());
        dto.setNamaPengadaan(pengadaan.getNamaPengadaan());
        dto.setTanggalPengadaan(pengadaan.getTanggalPengadaan().toString());
        dto.setVendor(pengadaan.getVendor());
        dto.setDiskonKeseluruhan(pengadaan.getDiskonKeseluruhan());
        dto.setShipmentStatus(pengadaan.getShipmentStatus());
        dto.setPaymentStatus(pengadaan.getPaymentStatus());
        dto.setListBarang(pengadaan.getListPengadaanBarang());
        return dto;
    }

    UpdatePengadaanRequestDTO pengadaanToUpdatePengadaanRequestDTO(Pengadaan pengadaan);
}
