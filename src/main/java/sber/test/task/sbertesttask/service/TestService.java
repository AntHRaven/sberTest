package sber.test.task.sbertesttask.service;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sber.test.task.sbertesttask.annotation.MySecuredAnnotation;

@Service
public class TestService {

  @MySecuredAnnotation("ROLE_USER")
  public String test1() {
    return "test1";
  }

  @MySecuredAnnotation("ROLE_ADMIN")
  public String test2() {
    return "test2";
  }


  @MySecuredAnnotation({ "ROLE_USER", "ROLE_ADMIN" })
  public String test3() {
    return "test3";
  }
}
