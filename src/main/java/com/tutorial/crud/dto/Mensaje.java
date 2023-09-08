package com.tutorial.crud.dto;

/**
 * Este DTO nos sirve para el envío de mensajes a través de las respuestas HTTP a
  los clientes.
 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Mensaje {
  private String mensaje;
}
