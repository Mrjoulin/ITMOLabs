package com.joulin.Lab4.controllers;

import com.joulin.Lab4.dto.Coordinates;
import com.joulin.Lab4.dto.HitResult;

import com.joulin.Lab4.security.jwt.AuthTokenFilter;
import com.joulin.Lab4.security.sevices.AuthUsersDetails;
import com.joulin.Lab4.services.HitService;
import com.joulin.Lab4.utils.CoordinatesValidation;
import com.joulin.Lab4.utils.OutOfCoordinatesBoundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/hits")
public class HitController {
    private final HitService hitService;

    private final AuthTokenFilter authTokenFilter;

    @Autowired
    public HitController(HitService hitService, AuthTokenFilter authTokenFilter) {
        this.hitService = hitService;
        this.authTokenFilter = authTokenFilter;
    }

    @GetMapping()
    public ResponseEntity<List<HitResult>> getHits(HttpServletRequest request) {
        List<HitResult> hits = hitService.findAllByOwnerId(getUserIdFromRequest(request));

        return new ResponseEntity<>(hits, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> addHit(@RequestBody Coordinates pointRequest, HttpServletRequest request) {
        try {
            CoordinatesValidation.validate(pointRequest);

            HitResult hitResult = hitService.save(pointRequest, getUserIdFromRequest(request));

            return new ResponseEntity<>(hitResult, HttpStatus.CREATED);
        } catch (OutOfCoordinatesBoundsException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping()
    public void deleteAll(HttpServletRequest request) {
        hitService.deleteAll(getUserIdFromRequest(request));
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        AuthUsersDetails userDetails = (AuthUsersDetails) authTokenFilter.getUserDetails(request);
        return userDetails.getId();
    }
}
