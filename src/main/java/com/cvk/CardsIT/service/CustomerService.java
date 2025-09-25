package com.cvk.CardsIT.service;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.cvk.CardsIT.entity.CustomerEntity;
import com.cvk.CardsIT.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {

	@Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public CustomerEntity insertCustomer(CustomerEntity customerEntity) {
        return customerRepository.save(customerEntity);
    }

    @Transactional
    public CustomerEntity updateCustomer(Long id, CustomerEntity customerEntity) {
        return customerRepository.findById(id)
                .map(existing -> {
                    existing.setName(customerEntity.getName());
                    existing.setEmail(customerEntity.getEmail());
                    existing.setPhone(customerEntity.getPhone());
                    existing.setAddress(customerEntity.getAddress());
                    return customerRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    @Transactional
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Transactional
    public CustomerEntity getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    @Transactional
    public List<CustomerEntity> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public Page<CustomerEntity> getCustomersWithPagination(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size));
    }
    
}
