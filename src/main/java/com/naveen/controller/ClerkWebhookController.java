package com.naveen.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.naveen.entity.User;
import com.naveen.service.InvoiceService;
import com.naveen.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.svix.Webhook;
import com.svix.exceptions.WebhookVerificationException;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhooks")
public class ClerkWebhookController {

    @Value("${clerk.webhook.secret}")
    private String webhookSecret;

    private final UserService userService;
    private final InvoiceService invoiceService;

    @PostMapping("/clerk")
    public ResponseEntity<?> handleClerkWebhook(@RequestHeader("svix-id") String svixId,
                                                @RequestHeader("svix-timestamp") String svixTimestamp,
                                                @RequestHeader("svix-signature") String svixSignature,
                                                @RequestBody String payload) {
        try {
            verifyWebhookSignature(svixId,svixTimestamp, svixSignature, payload);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);

            String eventType = rootNode.path("type").asText();
            switch (eventType) {
                case "user.created":
                    handleUserCreated(rootNode.path("data"));
                    break;
                case "user.updated":
                    handleUserUpdated(rootNode.path("data"));
                    break;
                case "user.deleted":
                    handleUserDeleted(rootNode.path("data"));
                    break;
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // log the error for now
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,e.getMessage());
        }
    }

    private void handleUserDeleted(JsonNode data) {
        String clerkId = data.path("id").asText();
        // Delete user
        userService.deleteAccount(clerkId);
        // Delete all invoices associated with the user
        invoiceService.deleteInvoicesByClerkId(clerkId);
    }

    private void handleUserUpdated(JsonNode data) {
        String clerkId = data.path("id").asText();
        User existingUser = userService.getAccountByClerkId(clerkId);

        existingUser.setEmail(data.path("email_addresses").path(0).path("email_address").asText());
        existingUser.setFirstName(data.path("first_name").asText());
        existingUser.setLastName(data.path("last_name").asText());
        existingUser.setPhotoUrl(data.path("image_url").asText());

        userService.saveOrUpdateUser(existingUser);

    }

    private void handleUserCreated(JsonNode data) {
        User newUser = User.builder()
                .clerkId(data.path("id").asText())
                .email(data.path("email_addresses").path(0).path("email_address").asText())
                .firstName(data.path("first_name").asText())
                .lastName(data.path("last_name").asText())
                .build();
        userService.saveOrUpdateUser(newUser);
    }

    private void verifyWebhookSignature(String svixId, String svixTimestamp, String svixSignature, String payload) {
        try {
            // Build map of headers
            Map<String, List<String>> headerMap = new HashMap<>();
            headerMap.put("svix-id", List.of(svixId));
            headerMap.put("svix-timestamp", List.of(svixTimestamp));
            headerMap.put("svix-signature", List.of(svixSignature));

            // Create HttpHeaders compatible with Svix SDK
            HttpHeaders headers = HttpHeaders.of(headerMap, (k, v) -> true);

            // Verify using official Svix webhook verifier
            Webhook webhook = new Webhook(webhookSecret);
            webhook.verify(payload, headers); // throws on failure

        } catch (WebhookVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Signature verification failed: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Webhook error: " + e.getMessage());
        }
    }


}
