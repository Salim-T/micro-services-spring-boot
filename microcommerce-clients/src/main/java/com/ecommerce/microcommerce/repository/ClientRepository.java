package com.ecommerce.microcommerce.repository;

import com.ecommerce.microcommerce.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    List<Client> findByFirstnameContainingIgnoreCase(String firstname);

    List<Client> findByLastnameContainingIgnoreCase(String lastname);

    List<Client> findByEmailContainingIgnoreCase(String email);
}
