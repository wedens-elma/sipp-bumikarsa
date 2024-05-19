package propensi.c06.sipp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import propensi.c06.sipp.model.Role;
import propensi.c06.sipp.model.UserModel;

@Repository
public interface UserDb extends JpaRepository<UserModel, UUID> {
    UserModel findByEmail(String email);
    List<UserModel> findByIsDeleted(boolean deleted);
    Optional<UserModel> findByNameAndRole(String name, Role role);
}
