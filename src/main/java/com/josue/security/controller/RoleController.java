package com.josue.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

  @GetMapping("/accessAdmin")
  public String accessAdmin() {
    return "Hola, has accedido con rol de ADMIN";
  }

  @GetMapping("/accessUser")
  public String accessUser() {
    return "Hola, has accedido con rol de USER";
  }

  @GetMapping("/accessInvited")
  public String accessInvited() {
    return "Hola, has accedido con rol de INVITED";
  }
  
}
