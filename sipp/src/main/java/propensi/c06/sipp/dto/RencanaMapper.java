package propensi.c06.sipp.dto;

import org.mapstruct.Mapper;
import propensi.c06.sipp.dto.request.CreateRencanaRequestDTO;
import propensi.c06.sipp.model.Rencana;

@Mapper(componentModel = "spring")
public interface RencanaMapper {
    Rencana createRencanaRequestDTOToRencana(CreateRencanaRequestDTO createRencanaRequestDTO);
}
