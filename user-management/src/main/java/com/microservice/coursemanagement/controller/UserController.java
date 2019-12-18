package com.microservice.coursemanagement.controller;

import com.microservice.coursemanagement.entity.Role;
import com.microservice.coursemanagement.entity.User;
import com.microservice.coursemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.List;

public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    Environment environment;
    @Value("${spring.application.name}")
    private String serviceId;

    @PostMapping("/service/registration")
    public ResponseEntity<?> saveUser(@RequestBody User user){
        if(userService.findByUsername(user.getUsername())!=null){
            //status code 409
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setRole(Role.USER);
        return new ResponseEntity<>(userService.save(user),HttpStatus.CREATED);
    }

    @GetMapping("/service/login")
    public ResponseEntity<?> getUser(Principal principal){
        //Principal principal = request.getUserPrincipal();
        if(principal==null||principal.getName()==null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return ResponseEntity.ok(userService.findByUsername(principal.getName()));
    }

    @PostMapping("/service/names")
    public ResponseEntity<?> getNamesOfUsers(@RequestBody List<Long> idList){
        return ResponseEntity.ok(userService.findUsers(idList));
    }
    @GetMapping("/service/services")
    public ResponseEntity<?> getServices(){
        return new ResponseEntity<>(discoveryClient.getServices(),HttpStatus.OK);
    }
    @GetMapping("/service/port")
    public String getPort(){
        return "Service port number :".concat(environment.getProperty("local.server.port"));
    }
    @GetMapping("/service/instances")
    public ResponseEntity<?> getInstances(){
        return new ResponseEntity<>(discoveryClient.getInstances(serviceId),HttpStatus.OK);
    }

}
