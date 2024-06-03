package com.ecommerce.ecommerceapi.rest;

import com.ecommerce.ecommerceapi.dto.AddressDTO;
import com.ecommerce.ecommerceapi.dto.ProductDTO;
import com.ecommerce.ecommerceapi.entity.Address;
import com.ecommerce.ecommerceapi.service.AddressService;
import com.ecommerce.ecommerceapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/addresses")
public class AddressRestController {

    private AddressService addressService;
    private UserService userService;

    public AddressRestController(AddressService addressService, UserService theUserService) {
        this.addressService = addressService;
        userService = theUserService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/createAddress")
    public ResponseEntity<?> createAddress(@RequestBody AddressDTO addressDTO) {
        try {
            Address address = addressService.createAddress(addressDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Address created with id - " + address.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An error occurred: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/updateAddress/{orderId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long orderId, @RequestBody AddressDTO addressDTO) {
        try {
            Address updatedAddress = addressService.updateAddress(orderId, addressDTO);
            return ResponseEntity.ok(updatedAddress);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error updating address: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/deleteAddress/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
        try {
            String resultMessage = addressService.deleteAddress(addressId);
            return ResponseEntity.ok(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error deleting address: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return ResponseEntity.ok(addressDTO);
    }



}
