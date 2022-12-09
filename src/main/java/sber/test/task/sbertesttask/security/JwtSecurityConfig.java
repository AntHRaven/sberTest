package sber.test.task.sbertesttask.security;


import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true)
@Lazy
public class JwtSecurityConfig
    implements WebMvcConfigurer {

  private static final String[] AUTH_WHITELIST = {
      // -- Swagger UI v2
      "/v2/api-docs",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui.html",
      "/swagger-ui.html/**",
      "/webjars/**",
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/css/**", "/img/**", "/js/**", "/encode/*", "/login"
  };
  private final JwtTokenFilter jwtTokenFilter;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  public JwtSecurityConfig(JwtTokenFilter jwtTokenFilter) {
    this.jwtTokenFilter = jwtTokenFilter;
    //https://habr.com/ru/post/203318/ - все о секурити
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }


  @Bean
  public SecurityFilterChain filterChainJwt(HttpSecurity http) throws Exception {
    http
        .cors().and()
        .csrf().disable()
        .httpBasic()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        //Доступ для всех пользователей
        .antMatchers(AUTH_WHITELIST).permitAll()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint((request, response, ex) -> {
          response.sendError(
              HttpServletResponse.SC_UNAUTHORIZED,
              ex.getMessage()
          );
        })
        .and()
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

}
