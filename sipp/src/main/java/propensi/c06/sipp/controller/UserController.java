package propensi.c06.sipp.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import propensi.c06.sipp.dto.UserMapper;
import propensi.c06.sipp.dto.request.CreateUserRequestDTO;
import propensi.c06.sipp.dto.request.UpdatePasswordRequestDTO;
import propensi.c06.sipp.dto.request.UpdateProfileRequestDTO;
import propensi.c06.sipp.model.UserModel;
import propensi.c06.sipp.repository.UserDb;
import propensi.c06.sipp.service.RoleService;
import propensi.c06.sipp.service.UserService;



@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserDb userDb;

    @GetMapping("/user/add")
    private String addUserForm(Model model){
        UserModel user = new UserModel();
        CreateUserRequestDTO createUserRequestDTO = new CreateUserRequestDTO();
        model.addAttribute("userDTO", createUserRequestDTO);
        model.addAttribute("listRole", roleService.getAllList());
        model.addAttribute("user", user);
        UserModel loggedInUser = userService.getLoggedInUser();
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("username", loggedInUser.getName());
        return "form-create-user";
    }

    @PostMapping("/user/add")
    private String addUserSubmit(@ModelAttribute CreateUserRequestDTO createUserRequestDTO, Model model){
        UserModel userModel = new UserModel();
        // userMapper.createUserRequestDTOToUserModel(createUserRequestDTO);
        var role = roleService.getRoleByRoleName(createUserRequestDTO.getRole());

        if (userDb.findByNameAndRole(createUserRequestDTO.getName(), role).isPresent()) {
            return "double-account";
        }

        // userModel.setDeleted(false);
        userModel.setEmail(createUserRequestDTO.getEmail());
        userModel.setName(createUserRequestDTO.getName());
        userModel.setPassword(createUserRequestDTO.getPassword());
        userModel.setRole(role);

        // if (!file.isEmpty()) {
        //     byte[] imageContent;
        //     try {
        //             imageContent = userService.processFile(file);
        //             userModel.setImage(imageContent);
        //     } catch (IOException e) {
        //         model.addAttribute("error", "Error processing the file");
        //         model.addAttribute("userDTO", createUserRequestDTO);
        //         model.addAttribute("listRole", roleService.getAllList());
        //         model.addAttribute("user", userModel);
        //         return "form-create-user";
        //     }
        // }
        // try {
        //     user.setImage(createUserRequestDTO.getFile().getBytes());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        userService.addUser(userModel, createUserRequestDTO);
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String viewAllUsers(Model model) {
        List<UserModel> users = userService.getActiveUsers();
        model.addAttribute("users", users);
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());
        return "viewall-user";
    }

    @GetMapping("/user/soft-delete/{email}")
    private String softDeleteUser(@PathVariable String email, Model model) {
        UserModel userModel = userService.getUserByEmail(email);
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());
        model.addAttribute("email", userModel.getEmail());
        if (userModel.getRole().getRole().equals("Admin")){
            return "failed-delete-admin";
        }
        else{
            return "confirm-delete-user";
        }
    }

    @GetMapping("/user/soft-delete/{email}/confirm")
    private String confirmDeleteUser(@PathVariable String email, Model model) {
        userService.softDeleteUser(email);
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());
        return "redirect:/user";
    }

    @GetMapping("/logout-confirm")
    public String showLogoutConfirmation(Model model) {
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());
        return "logout-confirm";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Principal principal) {
        UserModel loggedInUser = userService.getLoggedInUser();
        model.addAttribute("user", loggedInUser);
        model.addAttribute("username", loggedInUser.getName());

        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            return "profile-admin.html";
        }
        return "profile.html";
    }

    @GetMapping("/profile/update-password")
    public String getFormUpdatePassword(Model model) {
        UpdatePasswordRequestDTO dto = new UpdatePasswordRequestDTO();
        UserModel user = userService.getLoggedInUser();
        model.addAttribute("user", user);
        model.addAttribute("dto", dto);
        model.addAttribute("username", user.getName());
        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            return "form-update-password-admin.html";
        }
        return "form-update-password";
    }

    @PostMapping("/profile/update-password")
    public String updatePassword(UpdatePasswordRequestDTO dto,
                                Model model, Principal principal) {

        UserModel user = userService.getLoggedInUser();

        String passwordLama = dto.getPasswordLama();
        String passwordBaru = dto.getPasswordBaru();
        String konfirmasi = dto.getKonfirmasi();

        int stat = 0;

        Boolean isPassLamaTrue = userService.getPassChecker(passwordLama, user.getPassword());
        model.addAttribute("stat", stat);
        model.addAttribute("dto", dto);
        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());

        if(isPassLamaTrue){
            if(passwordBaru.equals(konfirmasi)){
                String passBaruEncode = userService.encrypt(passwordBaru);
                userService.updatePass(user, passBaruEncode);
                if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
                    return "success-update-password-admin.html";
                }
                return "success-update-password";
            }
            else {
                model.addAttribute("error", "Konfirmasi password belum sesuai");
                if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
                    return "failed-update-password-admin.html";
                }
                return "failed-update-password";
            }
        }
        else {
            model.addAttribute("error", "Password Lama belum tepat");
            if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
                return "failed-update-password-admin.html";
            }
            return "failed-update-password";
        }
        
        // if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
        //     return "form-update-password-admin.html";
        // }
        // return "form-update-password";
    }

    @GetMapping("/profile/update-profile")
    public String getFormUpdateProfile(Model model) {

        UserModel user = userService.getLoggedInUser();

        UpdateProfileRequestDTO profileDTO = new UpdateProfileRequestDTO();
        profileDTO.setEmail(user.getEmail());
        profileDTO.setName(user.getName());
        profileDTO.setRole(user.getRole().getRole());

        model.addAttribute("user", user);
        model.addAttribute("username", user.getName());

        model.addAttribute("user", user);
        model.addAttribute("dto", profileDTO);
        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            return "form-update-profile-admin.html";
        }
        return "form-update-profile";
    }

    @PostMapping("/profile/update-profile")
    public String updateProfile(Model model, UpdateProfileRequestDTO profileDTO) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!MASUK CONTROLLER");
        UserModel user = userService.getLoggedInUser();

        userService.updateProfile(user, profileDTO.getEmail());
        System.out.println("!!!!!!!!!!!!!!!!!!!UDAH SAVED");
        System.out.println(user.getEmail());

        model.addAttribute("username", user.getName());
        model.addAttribute("user", user);

        if (userService.getCurrentUserRole().equalsIgnoreCase("admin")) {
            return "success-update-email-admin.html";
        }
        
        return "success-update-email";
    }

    // @GetMapping("/logout-successful")
    // public String logoutSuccessful() {
    //     return "logout-successful";
    // }

}
