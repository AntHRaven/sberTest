package sber.test.task.sbertesttask.service.userdetails;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sber.test.task.sbertesttask.model.User;
import sber.test.task.sbertesttask.model.UserRoles;
import sber.test.task.sbertesttask.repository.UserRepository;
import sber.test.task.sbertesttask.service.UserRolesService;

@Service
public class CustomUserDetailsService
    implements UserDetailsService {

  private final UserRolesService userRolesService;
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRolesService userRolesService, UserRepository userRepository) {
    this.userRolesService = userRolesService;
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    User user = userRepository.findUserByLogin(username);
    List<UserRoles> userRoles = userRolesService.getAllUserRoles(user.getId());
    List<GrantedAuthority> authorities = setAuthority(userRoles);

    return new CustomUserDetails(user.getId().intValue(), username, user.getPassword(), authorities);
  }

  private List<GrantedAuthority> setAuthority(List<UserRoles> userRoles) {
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    userRoles.forEach(i -> {
      switch (i.getRole().getTitle()) {
        case "user" -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        case "admin" -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
      }
    });

    return grantedAuthorities;
  }
}
