package com.jaspereap.app.ws.app_ws.ui.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaspereap.app.ws.app_ws.exceptions.UserServiceException;
import com.jaspereap.app.ws.app_ws.ui.model.request.UpdateUserDetailsRequest;
import com.jaspereap.app.ws.app_ws.ui.model.request.UserDetailsRequest;
import com.jaspereap.app.ws.app_ws.ui.model.response.UserRest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

    Map<String, UserRest> users = new HashMap<>();

    @GetMapping(path="/{userId}", 
        produces = { 
                MediaType.APPLICATION_JSON_VALUE, 
                MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity<UserRest> getUser(@PathVariable String userId) {
        if (users.containsKey(userId)) {
            return new ResponseEntity<>(users.get(userId), HttpStatus.ACCEPTED);
        } else {
            throw new UserServiceException("User not found!");
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE } )
    public ResponseEntity<UserRest> createUser(@Valid @RequestBody UserDetailsRequest userDetails) {
        UserRest response = new UserRest();
        String userId = UUID.randomUUID().toString();
        response.setUserId(userId);
        response.setEmail(userDetails.getEmail());
        response.setFirstName(userDetails.getFirstName());
        response.setLastName(userDetails.getLastName());
        if (users == null) users = new HashMap<>();
        users.put(userId, response);
        return new ResponseEntity<UserRest>(response, HttpStatus.ACCEPTED);
    }

    @PutMapping(
        path="/{userId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = { 
            MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE }
    )
    public UserRest updateUser(@PathVariable String userId, @RequestBody UpdateUserDetailsRequest updateUserDetails ) {
        UserRest storedUserDetails = users.get(userId);
        storedUserDetails.setFirstName(updateUserDetails.getFirstName());
        storedUserDetails.setLastName(updateUserDetails.getLastName());
        users.put(userId, storedUserDetails);
        return storedUserDetails;
    }


    @DeleteMapping(path="/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        users.remove(userId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
