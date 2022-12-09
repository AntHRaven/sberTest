package sber.test.task.sbertesttask.service.impl;

import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sber.test.task.sbertesttask.model.User;
import sber.test.task.sbertesttask.model.UserRoles;
import sber.test.task.sbertesttask.repository.UserRepository;
import sber.test.task.sbertesttask.repository.UserRoleRepository;
import sber.test.task.sbertesttask.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  private final UserRoleRepository userRoleRepository;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;


  public UserServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRoleRepository = userRoleRepository;
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }


  @Override
  public User create(User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }
}
