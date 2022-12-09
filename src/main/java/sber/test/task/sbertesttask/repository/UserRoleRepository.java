package sber.test.task.sbertesttask.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sber.test.task.sbertesttask.model.UserRoles;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoles, Long> {

  List<UserRoles> findAllByUserId(Long id);

}
