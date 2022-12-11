package sber.test.task.sbertesttask.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sber.test.task.sbertesttask.dto.LoginDTO;
import sber.test.task.sbertesttask.model.User;
import sber.test.task.sbertesttask.security.JwtTokenUtil;
import sber.test.task.sbertesttask.service.TestService;
import sber.test.task.sbertesttask.service.UserService;
import sber.test.task.sbertesttask.service.userdetails.CustomUserDetailsService;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final CustomUserDetailsService authenticationService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final TestService testService;

    public UserController(CustomUserDetailsService authenticationService,
        JwtTokenUtil jwtTokenUtil, UserService userService, TestService testService) {
        this.authenticationService = authenticationService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.testService = testService;
    }

    @RequestMapping(value = "/auth", method = POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> auth(@RequestBody LoginDTO loginDTO) throws JsonProcessingException {
        HashMap<String, Object> response = new HashMap<>();
        UserDetails foundUser = authenticationService.loadUserByUsername(loginDTO.getLogin());
        String token = jwtTokenUtil.generateToken(foundUser);
        response.put("token", token);
        response.put("permissions", foundUser.getAuthorities());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/registration")
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @GetMapping("/test1")
    public ResponseEntity<String> test1() {
        try {
            return ResponseEntity.ok().body(testService.test1());
        } catch (AccessDeniedException e) {
            return forbidden(e);
        }
    }

    @GetMapping("/test2")
    public ResponseEntity<String> test2() {
        try {
            return ResponseEntity.ok().body(testService.test2());
        } catch (AccessDeniedException e) {
            return forbidden(e);
        }
    }

    @GetMapping("/test3")
    public ResponseEntity<String> test3() {
        try {
            return ResponseEntity.ok().body(testService.test3());
        } catch (AccessDeniedException e) {
            return forbidden(e);
        }
    }

    private ResponseEntity<String> forbidden(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
