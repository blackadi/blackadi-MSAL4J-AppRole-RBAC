package com.approle.demo.controller;

import com.approle.demo.utils.OboAuthProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    @Autowired
    OboAuthProvider oboAuthProvider;

        @RequestMapping("/admin")
    public ResponseEntity<String> admin() {
        if (oboAuthProvider.hasAdminRole())
        {
            return ResponseEntity.status(200).body("You can access as Admin Only.");
        }
        return ResponseEntity.status(403).body(String.format("Forbidden"));
    }

    @RequestMapping("/user")
    public ResponseEntity<String> user() {
        if (oboAuthProvider.hasUserRole())
        {
            return ResponseEntity.status(200).body("You can access as User or Admin.");
        }
        return ResponseEntity.status(403).body(String.format("Forbidden"));
    }

    @RequestMapping("/graphMeApi")
    public ResponseEntity<String> graphMeApi() {
        try {
            GraphServiceClient<Request> graphClient = GraphServiceClient
                    .builder()
                    .authenticationProvider(oboAuthProvider)
                    .buildClient();

            User user = graphClient.me().buildRequest().get();
            ObjectMapper objectMapper = new ObjectMapper();
            return ResponseEntity.status(200).body(objectMapper.writeValueAsString(user));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(String.format("%s: %s", ex.getCause(), ex.getMessage()));
        }
    }
}
