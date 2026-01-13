package com.AyushToCode.JobPortalApplication.Controller;

import com.AyushToCode.JobPortalApplication.services.HiringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bids")
public class HiringController {

    private final HiringService hiringService;

    public HiringController(HiringService hiringService) {
        this.hiringService = hiringService;
    }

    // Endpoint: PATCH /api/bids/:bidId/hire
    @PatchMapping("/{bidId}/hire")
    public ResponseEntity<?> hireFreelancer(@PathVariable int bidId) {
        try {
            hiringService.hireCandidate(bidId);
            return ResponseEntity.ok("Freelancer hired and other bids rejected successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Conflict
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error during hiring process: " + e.getMessage());
        }
    }
}