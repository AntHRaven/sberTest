package sber.test.task.sbertesttask.service;

import java.util.List;
import sber.test.task.sbertesttask.model.UserRoles;

public interface UserRolesService {
  List<UserRoles> getAllUserRoles(Long userId);

}
