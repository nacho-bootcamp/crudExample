package com.tutorial.crud.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.crud.dto.Mensaje;
import com.tutorial.crud.dto.ProductoDto;
import com.tutorial.crud.entity.Product;
import com.tutorial.crud.service.ProductoService;

//marca a la clase como un controlador 
@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductoController {

  @Autowired
  ProductoService productoService;

  @GetMapping("")
  public ResponseEntity<List<Product>> findAll() {
    List<Product> list = productoService.list();
    return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable("id") int id) {
    if (!productoService.existsById(id))
      return new ResponseEntity<>(new Mensaje("El producto solicitado no existe"), HttpStatus.NOT_FOUND);
    Product producto = productoService.getOne(id).get();
    return new ResponseEntity<>(producto, HttpStatus.OK);
  }

  @GetMapping("/detail-name/{name}")
  public ResponseEntity<?> getByNombre(@PathVariable("nombre") String nombre) {
    if (!productoService.existsByNombre(nombre))
      return new ResponseEntity<Mensaje>(new Mensaje("El producto con nombre " + nombre + " no existe"),
          HttpStatus.NOT_FOUND);
    Product producto = productoService.getByNombre(nombre).get();
    return new ResponseEntity<Product>(producto, HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<Mensaje> create(@RequestBody ProductoDto productoDto) {
    if (StringUtils.isBlank(productoDto.getNombre()))
      return new ResponseEntity<Mensaje>(new Mensaje("El nombre del producto es obligatorio"), HttpStatus.BAD_REQUEST);
    if (productoDto.getPrecio() == null || productoDto.getPrecio() < 0)
      return new ResponseEntity<Mensaje>(new Mensaje("El precio debe sert mayor que 0.0"), HttpStatus.BAD_REQUEST);
    if (productoService.existsByNombre(productoDto.getNombre()))
      return new ResponseEntity<Mensaje>(
          new Mensaje("El nombre " + productoDto.getNombre() + " ya se encuentra registrado"), HttpStatus.BAD_REQUEST);
    Product producto = new Product(productoDto.getNombre(), productoDto.getPrecio());
    productoService.save(producto);
    return new ResponseEntity<Mensaje>(new Mensaje("Producto creado con exito"), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Mensaje> update(@PathVariable("id") int id, @RequestBody ProductoDto productoDto) {
    if (!productoService.existsById(id))
      return new ResponseEntity<Mensaje>(new Mensaje("El producto no existe"), HttpStatus.NOT_FOUND);
    if (productoService.existsByNombre(productoDto.getNombre())
        && productoService.getByNombre(productoDto.getNombre()).get().getId() != id)
      return new ResponseEntity<Mensaje>(
          new Mensaje("El nomnbre " + productoDto.getNombre() + " ya se encuentra registrado"), HttpStatus.BAD_REQUEST);
    if (StringUtils.isBlank(productoDto.getNombre()))
      return new ResponseEntity<Mensaje>(new Mensaje("El nombre del producto es obligatorio"), HttpStatus.BAD_REQUEST);
    if (productoDto.getPrecio() == null || productoDto.getPrecio() < 0)
      return new ResponseEntity<Mensaje>(new Mensaje("El precio debe ser mayor a 0.0"), HttpStatus.BAD_REQUEST);
    Product producto = productoService.getOne(id).get();
    producto.setNombre(productoDto.getNombre());
    producto.setPrecio(productoDto.getPrecio());
    productoService.save(producto);
    return new ResponseEntity<Mensaje>(new Mensaje("Producto actualizado con exito"), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Mensaje> delete(@PathVariable("id") int id) {
    if (!productoService.existsById(id))
      return new ResponseEntity<Mensaje>(new Mensaje("El producto a eliminar no existe"), HttpStatus.NOT_FOUND);
    productoService.delete(id);
    return new ResponseEntity<Mensaje>(new Mensaje("Producto eliminado"), HttpStatus.OK);
  }
}
