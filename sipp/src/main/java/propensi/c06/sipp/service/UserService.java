package propensi.c06.sipp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import propensi.c06.sipp.dto.request.CreateUserRequestDTO;
import propensi.c06.sipp.dto.request.LoginJwtRequestDTO;
import propensi.c06.sipp.model.UserModel;

public interface UserService {
    UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO);
    
    String encrypt(String password);

    String loginJwtAdmin(LoginJwtRequestDTO loginJwtRequestDTO);

    byte[] processFile(MultipartFile image) throws IOException;

    List<UserModel> getAllUsers();

    UserModel getUserByEmail(String email);

    String getCurrentUserRole();

    List<UserModel> getActiveUsers();

    void softDeleteUser(String email);

    UserModel getLoggedInUser();
    
    String getCurrentUserName();
}