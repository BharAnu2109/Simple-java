package com.retailbusiness.order.client;

import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {
    
    @Override
    public UserDto getUserById(Long id) {
        // Return a fallback user to prevent order processing failure
        UserDto fallbackUser = new UserDto();
        fallbackUser.setId(id);
        fallbackUser.setFirstName("Unknown");
        fallbackUser.setLastName("User");
        fallbackUser.setEmail("unknown@example.com");
        fallbackUser.setPhoneNumber("N/A");
        fallbackUser.setAddress("Address Unavailable");
        fallbackUser.setIsActive(false);
        return fallbackUser;
    }
}