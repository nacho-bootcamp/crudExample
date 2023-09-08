package com.tutorial.crud.security.entity;

import com.tutorial.crud.security.enums.RolNombre;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "rol")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rol implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @NotNull
  @Enumerated(EnumType.STRING)
  private RolNombre rolNombre;

  public Rol(@NotNull RolNombre rolNombre) {
    this.rolNombre = rolNombre;
  }
}
