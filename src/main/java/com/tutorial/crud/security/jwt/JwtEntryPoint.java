package com.tutorial.crud.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.crud.dto.Mensaje;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
  private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

  @Override
  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException e)
      throws IOException, ServletException {
    logger.error("Error en el metodo commence");
    res.setHeader("Content-Type", "application/json");
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    ObjectMapper objectMapper = new ObjectMapper();
    System.out.println("paso por aqui");
    res.getWriter().write(objectMapper.writeValueAsString(new Mensaje("No autorizado")));
  }
}
