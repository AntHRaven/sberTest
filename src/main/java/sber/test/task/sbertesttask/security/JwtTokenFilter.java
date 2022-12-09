package sber.test.task.sbertesttask.security;

import com.sun.istack.NotNull;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sber.test.task.sbertesttask.service.userdetails.CustomUserDetailsService;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter
    extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final CustomUserDetailsService authenticationService;

  @Getter
  private String token;

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest,
      @NotNull HttpServletResponse httpServletResponse,
      @NotNull FilterChain filterChain) throws ServletException, IOException {
    token = null;
    final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }

    token = header.split(" ")[1].trim();

    UserDetails userDetails;
    try {
      userDetails = authenticationService.loadUserByUsername(jwtTokenUtil.getUsernameFromToken(token));
    } catch (SignatureException exception) {
      return;
    }

    if (!jwtTokenUtil.validateToken(token, userDetails)) {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
        null,
        userDetails.getAuthorities());
    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
