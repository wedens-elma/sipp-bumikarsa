package propensi.c06.sipp.service;

import propensi.c06.sipp.dto.request.CreateUserRequestDTO;
import propensi.c06.sipp.dto.request.LoginJwtRequestDTO;
import propensi.c06.sipp.model.UserModel;

public interface UserService {
    UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO);
    
    String encrypt(String password);

    String loginJwtAdmin(LoginJwtRequestDTO loginJwtRequestDTO);
    
    String getCurrentUserRole();
}