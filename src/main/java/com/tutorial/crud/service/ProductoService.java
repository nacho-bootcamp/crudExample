package com.tutorial.crud.service;

import com.tutorial.crud.entity.Product;
import com.tutorial.crud.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

  @Autowired
  ProductoRepository productoRepository;

  public List<Product> list() {
    return productoRepository.findAll();
  }

  public Optional<Product> getOne(int id) {
    return productoRepository.findById(id);
  }

  public Optional<Product> getByNombre(String nombre) {
    return productoRepository.findByNombre(nombre);
  }

  public void save(Product producto) {
    productoRepository.save(producto);
  }

  public void delete(int id) {
    productoRepository.deleteById(id);
  }

  public boolean existsById(int id) {
    return productoRepository.existsById(id);
  }

  public boolean existsByNombre(String nombre) {
    return productoRepository.existsByNombre(nombre);
  }
}
