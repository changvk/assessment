package com.cvk.CardsIT.controller;

import com.cvk.CardsIT.dto.CustomerRequest;
import com.cvk.CardsIT.dto.CustomerResponse;
import com.cvk.CardsIT.entity.CustomerEntity;
import com.cvk.CardsIT.service.CustomerService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RestTemplate restTemplate; 

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        CustomerEntity entity = customerService.getCustomerById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toResponse(entity));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerEntity> entities = customerService.getAllCustomers();
        List<CustomerResponse> responses = entities.stream().map(this::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> insertCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerEntity entity = toEntity(request);
        CustomerEntity saved = customerService.insertCustomer(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id,
                                                           @Valid @RequestBody CustomerRequest request) {
        CustomerEntity entity = toEntity(request);
        CustomerEntity updated = customerService.updateCustomer(id, entity);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CustomerResponse>> getPaged(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Page<CustomerEntity> pagedEntities = customerService.getCustomersWithPagination(page, size);
        List<CustomerResponse> responses = pagedEntities.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        Page<CustomerResponse> pagedResponses = new PageImpl<>(responses, pagedEntities.getPageable(), pagedEntities.getTotalElements());
        return ResponseEntity.ok(pagedResponses);
    }

    @SuppressWarnings("unchecked")
	@GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getCustomerWithExternalData(@PathVariable Long id) {
        CustomerEntity customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        String url = "https://jsonplaceholder.typicode.com/users/" + id;
        Map<String, Object> externalUser = restTemplate.getForObject(url, Map.class);

        return ResponseEntity.ok(Map.of(
                "customer", toResponse(customer),
                "externalUser", externalUser
        ));
    }

    private CustomerResponse toResponse(CustomerEntity entity) {
        return new CustomerResponse(entity.getId(), entity.getName(), entity.getEmail(), entity.getPhone(), entity.getAddress());
    }

    private CustomerEntity toEntity(CustomerRequest request) {
        CustomerEntity entity = new CustomerEntity();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setAddress(request.getAddress());
        return entity;
    }
}
