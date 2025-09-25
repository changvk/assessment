package com.cvk.CardsIT.dto;

import java.util.Map;
import com.cvk.CardsIT.entity.CustomerEntity;

public class CustomerDetailsResponse {
	
    private CustomerEntity customerEntity;
    private Map<String, Object> externalUser;
    
    public CustomerDetailsResponse() {}

    public CustomerDetailsResponse(CustomerEntity customerEntity, Map<String, Object> externalUser) {
        this.customerEntity = customerEntity;
        this.externalUser = externalUser;
    }
    
	public CustomerEntity getCustomerEntity() {
		return customerEntity;
	}
	public void setCustomerEntity(CustomerEntity customerEntity) {
		this.customerEntity = customerEntity;
	}
	public Map<String, Object> getExternalUser() {
		return externalUser;
	}
	public void setExternalUser(Map<String, Object> externalUser) {
		this.externalUser = externalUser;
	}
    
    
}
