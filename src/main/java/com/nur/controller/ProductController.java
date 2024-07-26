package com.nur.controller;

import com.nur.config.UserInfoUserDetails;
import com.nur.dto.Product;
import com.nur.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;


    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome, this endpoint is not secure";
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Product> getAllTheProducts() {
        String username = getUserPrinciple().getUsername();
        LOGGER.info("username extracted from UserPrinciple is: {}", username);
        return service.getProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product getProductById(@PathVariable int id) {
        return service.getProduct(id);
    }

    private UserInfoUserDetails getUserPrinciple(){
        UserInfoUserDetails userPrinciple = (UserInfoUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userPrinciple.getUsername() == null) {
            LOGGER.error("An error occurred during retrieving username from SecurityContextHolder.");
            throw new RuntimeException("Error while retrieving userinfo");
        }
        return userPrinciple;
    }

}

