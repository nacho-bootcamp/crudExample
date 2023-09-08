package com.tutorial.crud.security.service;

/**
 * @Autowired permite inyectar unas dependencias con otras dentro de Spring
 */

import com.tutorial.crud.security.entity.Rol;
import com.tutorial.crud.security.enums.RolNombre;
import com.tutorial.crud.security.repository.RolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RolService {

  @Autowired
  RolRepository rolRepository;

  public Optional<Rol> getByRolNombre(RolNombre rolNombre) {
    System.out.println(rolNombre);
    return rolRepository.findByRolNombre(rolNombre);
  }

  public void save(Rol rol) {
    rolRepository.save(rol);
  }
}
