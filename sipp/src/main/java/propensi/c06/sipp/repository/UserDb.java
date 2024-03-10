package propensi.c06.sipp.repository;

import propensi.c06.sipp.model.Role;
import propensi.c06.sipp.model.UserModel;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDb extends JpaRepository<UserModel, String> {
    UserModel findByEmail(String email);
    List<UserModel> findByIsDeleted(boolean deleted);
    Optional<UserModel> findByNameAndRole(String name, Role role);
}