package propensi.c06.sipp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import propensi.c06.sipp.dto.request.CreateUserRequestDTO;
import propensi.c06.sipp.dto.request.LoginJwtRequestDTO;
import propensi.c06.sipp.model.UserModel;
import propensi.c06.sipp.repository.UserDb;
import propensi.c06.sipp.security.jwt.JwtUtils;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDb userDb;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserModel addUser(UserModel user, CreateUserRequestDTO createUserRequestDTO){
        user.setRole(roleService.getRoleByRoleName(createUserRequestDTO.getRole()));
        String hashedPass = encrypt(user.getPassword());
        user.setPassword(hashedPass);
        return userDb.save(user);
    }

    @Override
    public String encrypt(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public String loginJwtAdmin(LoginJwtRequestDTO loginJwtRequestDTO) {
        String email = loginJwtRequestDTO.getEmail();
        String name = loginJwtRequestDTO.getName();

        UserModel user = userDb.findByEmail(email);

        if(user == null){
            user = new UserModel();
            user.setName(name);
            user.setPassword("password");
            user.setEmail(email);
            user.setRole(roleService.getRoleByRoleName("Admin"));
            userDb.save(user);
        }


        return jwtUtils.generateJwtToken(loginJwtRequestDTO.getEmail());
    }

    @Override
    public byte[] processFile(MultipartFile file) throws IOException {
        // check if the file is not empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        // check if the file is an image (you can customize this check based on your
        // requirements)
        if (!isImage(file)) {
            throw new IllegalArgumentException("File is not an image");
        }
        // convert MultipartFile to byte[]
        return file.getBytes();
    }

    private boolean isImage(MultipartFile file) {
        // Implement the Logic to check if the file is an image
        // You can use Libraries Like Apache Tika or simply check the file extension
        // For simplicity, let's assume any file with ".jpg", "jpeg", or ".png"
        // extension is considered an image
        String fileName = file.getOriginalFilename();
        return fileName != null
                && (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png"));
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userDb.findAll();
    }

    @Override
    public UserModel getLoggedInUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel user = null;
        if (principal instanceof UserDetails) {
            var userDetails = (UserDetails) principal;
            user = getUserByEmail(userDetails.getUsername());
        }
        return user;
    }

    @Override
    public UserModel getUserByEmail(String email) {
        return userDb.findByEmail(email);
    }

    @Override
    public String getCurrentUserRole(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            UserModel userModel = getUserByEmail(user.getUsername());

            System.out.println(userModel.getRole().getRole());

            String role = userModel.getRole().getRole();

            return role;
        }
        return null;
    }

    public List<UserModel> getActiveUsers() {
        return userDb.findByIsDeleted(false);
    }

    @Override
    public void softDeleteUser(String email) {
        UserModel user = userDb.findByEmail(email);
        if (user != null) {
            user.setDeleted(true);
            userDb.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    // @Transactional
    // public byte[] getImageData(String email) {
    //     UserModel user = userDb.findByEmail(email);
    //     Blob imageBlob = user.getImage();

    //     try (InputStream inputStream = imageBlob.getBinaryStream()) {
    //         return toByteArray(inputStream);
    //     } catch (IOException | SQLException e) {
    //         // Handle exceptions accordingly
    //         e.printStackTrace();
    //         return null;
    //     }
    // }

    @Override
    public String getCurrentUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) principal;
            UserModel userModel = getUserByEmail(user.getUsername());
            String name = userModel.getName();
            return name;
        }
        return null;
    }

    @Override
    public Boolean getPassChecker(String raw, String encoded) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        return passwordEncoder.matches(raw, encoded);
    }

    @Override
    public void updatePass(UserModel user, String passBaruEncode) {
        user.setPassword(passBaruEncode);
        userDb.save(user);
    }

    @Override
    public void updateProfile(UserModel user, String email) {
        user.setEmail(email);
        String newToken = jwtUtils.generateJwtToken(email);
        userDb.save(user);
    }
}
