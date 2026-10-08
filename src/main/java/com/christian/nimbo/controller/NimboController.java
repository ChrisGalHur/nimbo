package com.christian.nimbo.controller;

import com.christian.nimbo.service.NimboService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@Controller
public class NimboController {

    private final NimboService nimboService;

    public NimboController(NimboService nimboService) {

        this.nimboService = nimboService;
    }

    //region Health
    @GetMapping("/api/health")
    @ResponseBody
    public String health() {
        return "Nimbo funcionando";
    }
    //endregion


    //region Diagnostics

    @PostMapping("/api/diagnostics")
    @ResponseBody
    public Map<String, Object> createDiagnostic(
            @RequestBody Map<String, Object> data,
            HttpServletRequest request) {

        String userId =
                (String) request.getAttribute(
                        "userId"
                );

        return nimboService.createDiagnostic(
                userId,
                data
        );
    }


    @GetMapping("/api/diagnostics/{id}")
    @ResponseBody
    public Map<String, Object> getDiagnostic(
            @PathVariable String id,
            HttpServletRequest request) {

        String userId =
                (String) request.getAttribute(
                        "userId"
                );

        return nimboService.getDiagnostic(
                userId,
                id
        );
    }

    //endregion
}