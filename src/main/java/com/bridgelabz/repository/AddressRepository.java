package com.bridgelabz.repository;
import com.bridgelabz.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository
        extends JpaRepository<Address, Long> {
}
