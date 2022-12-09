package sber.test.task.sbertesttask.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class TestService {

  @Secured("ROLE_USER")
  public String test1() {
    return "test1";
  }

  @Secured("ROLE_ADMIN")
  public String test2() {
    return "test2";
  }


  @Secured({ "ROLE_USER", "ROLE_ADMIN" })
  public String test3() {
    return "test3";
  }
}
