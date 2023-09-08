package com.tutorial.crud.repository;

import com.tutorial.crud.entity.Product;

/**
 * JpaRepository nos provee diferentes métodos u operaciones las cuales podemos utilizar
   para interactuar con nuestra base de datos 

 *
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Product, Integer> {
  Optional<Product> findByNombre(String nombre);

  boolean existsByNombre(String nombre);
}
