package com.example.cantine.controller;

import com.example.cantine.dto.DashboardDto;
import com.example.cantine.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller @RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        DashboardDto dto = dashboardService.getDashboard();
        model.addAttribute("dashboard", dto);
        return "dashboard/index";
    }
}
