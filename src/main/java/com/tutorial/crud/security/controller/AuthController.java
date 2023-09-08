package com.tutorial.crud.security.controller;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.crud.dto.Mensaje;
import com.tutorial.crud.security.dto.LoginUsuario;
import com.tutorial.crud.security.dto.NuevoUsuario;
import com.tutorial.crud.security.dto.jwtDto;
import com.tutorial.crud.security.entity.Rol;
import com.tutorial.crud.security.entity.Usuario;
import com.tutorial.crud.security.enums.RolNombre;
import com.tutorial.crud.security.jwt.JwtProvider;
import com.tutorial.crud.security.service.RolService;
import com.tutorial.crud.security.service.UsuarioService;
import com.tutorial.crud.service.ProductoService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UsuarioService usuarioService;

  @Autowired
  RolService rolService;

  @Autowired
  JwtProvider jwtProvider;

  @Autowired
  ProductoService productoService;

  @PostMapping("")
  public ResponseEntity<Mensaje> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult) {
    if (bindingResult.hasErrors())
      return new ResponseEntity<Mensaje>(new Mensaje("Verifique los datos introducidos"), HttpStatus.BAD_REQUEST);
    if (usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
      return new ResponseEntity<Mensaje>(
          new Mensaje("El nombre " + nuevoUsuario.getNombre() + " ya se encuentra registrado"), HttpStatus.BAD_REQUEST);
    if (usuarioService.existsByEmail(nuevoUsuario.getEmail()))
      return new ResponseEntity<Mensaje>(
          new Mensaje("El mail " + nuevoUsuario.getEmail() + " ya se encuentra registrado"), HttpStatus.BAD_REQUEST);
    Usuario usuario = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
        passwordEncoder.encode(nuevoUsuario.getPassword()));

    Set<Rol> roles = new HashSet<>();
    Optional<Rol> userRoleName = rolService.getByRolNombre(RolNombre.ROLE_USER);
    Optional<Rol> adminRoleName = rolService.getByRolNombre(RolNombre.ROLE_ADMIN);

    if (!userRoleName.isPresent()) {
      return new ResponseEntity<Mensaje>(new Mensaje("rolUsuario no encontrado"), HttpStatus.BAD_REQUEST);
    }
    if (!adminRoleName.isPresent()) {
      return new ResponseEntity<Mensaje>(new Mensaje("rolAdmin no encontrado"), HttpStatus.BAD_REQUEST);
    }

    roles.add(userRoleName.get());
    if (nuevoUsuario.getRoles().contains("admin"))
      roles.add(adminRoleName.get());
    usuario.setRoles(roles);
    System.out.println(usuario.toString());
    usuarioService.save(usuario);
    return new ResponseEntity<Mensaje>(new Mensaje("Usuario registrado con exito"), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
    if (bindingResult.hasErrors())
      return new ResponseEntity<Mensaje>(new Mensaje("Usuario invalido"), HttpStatus.UNAUTHORIZED);
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtProvider.generateToken(authentication);
    jwtDto jwtDto = new jwtDto(jwt);
    return new ResponseEntity<jwtDto>(jwtDto, HttpStatus.ACCEPTED);
  }

  @PostMapping("/refresh")
  public ResponseEntity<jwtDto> refresh(@RequestBody jwtDto jwtDto) throws ParseException {
    String token = jwtProvider.refreshToken(jwtDto);
    jwtDto jwt = new jwtDto(token);
    return new ResponseEntity<jwtDto>(jwt, HttpStatus.OK);
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Mensaje> delete(@PathVariable("id") int id) {
    if (!productoService.existsById(id))
      return new ResponseEntity<Mensaje>(new Mensaje("El producto a eliminar no existe"), HttpStatus.NOT_FOUND);
    productoService.delete(id);
    return new ResponseEntity<Mensaje>(new Mensaje("Producto eliminado"), HttpStatus.OK);
  }
}
