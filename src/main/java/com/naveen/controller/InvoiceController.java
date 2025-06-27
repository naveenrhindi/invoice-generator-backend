package com.naveen.controller;

import com.naveen.entity.Invoice;
import com.naveen.service.EmailService;
import com.naveen.service.InvoiceService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Invoice> saveInvoice(@RequestBody Invoice invoice){
        return ResponseEntity.ok(invoiceService.saveInvoice(invoice));
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> fetchInvoices(Authentication authentication) {
        return ResponseEntity.ok(invoiceService.fetchInvoices(authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeInvoice(@PathVariable("id") String id, Authentication authentication) {
        if(authentication.getName() != null) {
            System.out.println(authentication.getName()+"\n"+id);
            invoiceService.removeInvoice(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"User does not have permission to access this resource");
        }
    }

    @PostMapping("/send-invoice")
    public ResponseEntity<?> sendInvoice(@RequestPart("file")MultipartFile file,
                                         @RequestPart("email") String customerEmail) throws MessagingException, IOException {
        try{
            emailService.sendInvoiceEmail(customerEmail, file);
            return ResponseEntity.ok("Invoice sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send invoice.");
        }
    }

}
