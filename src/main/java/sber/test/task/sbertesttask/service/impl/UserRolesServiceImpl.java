package sber.test.task.sbertesttask.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import sber.test.task.sbertesttask.model.UserRoles;
import sber.test.task.sbertesttask.repository.UserRoleRepository;
import sber.test.task.sbertesttask.service.UserRolesService;

@Service
public class UserRolesServiceImpl implements UserRolesService {

  private final UserRoleRepository userRoleRepository;

  public UserRolesServiceImpl(UserRoleRepository userRoleRepository) {
    this.userRoleRepository = userRoleRepository;
  }

  @Override
  public List<UserRoles> getAllUserRoles(Long userId) {
    return userRoleRepository.findAllByUserId(userId);
  }
}
