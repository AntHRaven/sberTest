package sber.test.task.sbertesttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sber.test.task.sbertesttask.model.User;

@Repository
public interface UserRepository
      extends JpaRepository<User, Long> {

    User findUserByLogin(String login);

}
