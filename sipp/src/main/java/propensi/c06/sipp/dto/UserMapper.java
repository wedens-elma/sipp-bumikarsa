// package propensi.c06.sipp.dto;

// import propensi.c06.sipp.dto.request.CreateUserRequestDTO;
// import propensi.c06.sipp.dto.response.CreateUserResponseDTO;
// import propensi.c06.sipp.model.UserModel;
// import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;

// @Mapper(componentModel = "spring")
// public interface UserMapper {
//     @Mapping(target = "role", ignore = true)
//     UserModel createUserRequestDTOToUserModel(CreateUserRequestDTO createUserRequestDTO);

//     @Mapping(target = "role", ignore = true)
//     CreateUserResponseDTO createUserResponseDTOToUserModel(UserModel userModel);
// }