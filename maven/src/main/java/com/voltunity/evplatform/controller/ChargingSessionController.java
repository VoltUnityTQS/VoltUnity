package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.service.ChargingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chargingSessions")
@CrossOrigin(origins = "*")
public class ChargingSessionController {

    @Autowired
    private ChargingSessionService chargingSessionService;

    // Os endpoints POST/PUT/GET vêm no próximo commit!
}