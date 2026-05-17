package com.vaccine.controller;

import com.vaccine.common.Result;
import com.vaccine.entity.*;
import com.vaccine.service.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vaccine")
@CrossOrigin
public class VaccineController {

    @Autowired
    private VaccineService vaccineService;

    @PostMapping("/create")
    public Result<Vaccine> createVaccine(@RequestBody Vaccine vaccine) {
        return Result.success(vaccineService.createVaccine(vaccine));
    }

    @GetMapping("/list")
    public Result<List<Vaccine>> listVaccines() {
        return Result.success(vaccineService.getAllVaccines());
    }

    @PostMapping("/batch/create")
    public Result<VaccineBatch> createBatch(@RequestBody VaccineBatch batch) {
        return Result.success(vaccineService.createBatch(batch));
    }

    @GetMapping("/batch/list")
    public Result<List<VaccineBatch>> listBatches() {
        return Result.success(vaccineService.getAllBatches());
    }

    @PostMapping("/site/create")
    public Result<VaccinationSite> createSite(@RequestBody VaccinationSite site) {
        return Result.success(vaccineService.createSite(site));
    }

    @GetMapping("/site/list")
    public Result<List<VaccinationSite>> listSites() {
        return Result.success(vaccineService.getAllSites());
    }

    @PostMapping("/inventory/add")
    public Result<Inventory> addInventory(@RequestBody Map<String, Object> params) {
        Long siteId = Long.valueOf(params.get("siteId").toString());
        Long vaccineId = Long.valueOf(params.get("vaccineId").toString());
        Long batchId = Long.valueOf(params.get("batchId").toString());
        int quantity = Integer.parseInt(params.get("quantity").toString());
        return Result.success(vaccineService.addInventory(siteId, vaccineId, batchId, quantity));
    }

    @GetMapping("/inventory/list")
    public Result<List<Inventory>> listInventories() {
        return Result.success(vaccineService.getAllInventories());
    }

    @GetMapping("/inventory/site/{siteId}")
    public Result<List<Inventory>> listSiteInventories(@PathVariable Long siteId) {
        return Result.success(vaccineService.getSiteInventories(siteId));
    }
}
