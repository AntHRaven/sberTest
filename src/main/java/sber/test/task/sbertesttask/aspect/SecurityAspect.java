package sber.test.task.sbertesttask.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sber.test.task.sbertesttask.annotation.MySecuredAnnotation;

@Aspect
@Service
public class SecurityAspect {

  @Pointcut("@annotation(sber.test.task.sbertesttask.annotation.MySecuredAnnotation)")
  public void secureAnnotation() {
  }

  @Around("secureAnnotation()")
  public Object beforeCallAtMethod1(ProceedingJoinPoint joinPoint) throws Throwable {
    Signature methodSignature = joinPoint.getSignature();
    MethodSignature signature = (MethodSignature) methodSignature;
    Method method = joinPoint
        .getTarget()
        .getClass()
        .getMethod(
            signature
                .getMethod()
                .getName(),
            signature
                .getMethod()
                .getParameterTypes()
        );
    MySecuredAnnotation mySecuredAnnotation = method.getAnnotation(MySecuredAnnotation.class);
    List<String> rolesFromAnnotation = Arrays.asList(mySecuredAnnotation.value());
    List<String> userRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    if(checkAuthority(rolesFromAnnotation, userRoles)) {
      throw new AccessDeniedException("Отказано в доступе");
    }
    return joinPoint.proceed();
  }

  private boolean checkAuthority(List<String> rolesFromAnnotation, List<String> userRoles) {
    return Collections.disjoint(rolesFromAnnotation, userRoles);
  }
}
