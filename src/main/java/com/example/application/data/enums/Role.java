package com.example.application.data.enums;

import com.example.application.data.entity.User;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum Role {
  LANDLORD("landlord"),
  STUDENT("student"),
  ADMIN("admin");

  private final String roleName;

  Role(String roleName) {
    this.roleName = roleName;
  }

}
