package com.christian.nimbo.controller;

import com.christian.nimbo.service.GoogleSheetsService;
import com.christian.nimbo.service.NimboService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class NimboController {

    private final NimboService nimboService;
    private final GoogleSheetsService googleSheetsService;

    public NimboController(
            NimboService nimboService,
            GoogleSheetsService googleSheetsService) {

        this.nimboService = nimboService;
        this.googleSheetsService = googleSheetsService;
    }

    //region Google Sheets test
    @GetMapping("/api/sheets/test")
    @ResponseBody
    public String testGoogleSheets() {

        try {
            return googleSheetsService.readSheet();

        } catch (Exception e) {
            e.printStackTrace();

            return "Google Sheets error: " +
                    e.getMessage();
        }
    }
    //endregion


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
            @RequestBody Map<String, Object> data) {

        return nimboService.createDiagnostic(data);
    }


    @GetMapping("/api/diagnostics/{id}")
    @ResponseBody
    public Map<String, Object> getDiagnostic(
            @PathVariable String id) {

        return nimboService.getDiagnostic(id);
    }

    //endregion
}