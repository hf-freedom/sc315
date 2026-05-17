package com.vaccine.controller;

import com.vaccine.common.Result;
import com.vaccine.entity.Transfer;
import com.vaccine.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transfer")
@CrossOrigin
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/create")
    public Result<Transfer> create(@RequestBody Map<String, Object> params) {
        try {
            Long fromSiteId = Long.valueOf(params.get("fromSiteId").toString());
            Long toSiteId = Long.valueOf(params.get("toSiteId").toString());
            Long vaccineId = Long.valueOf(params.get("vaccineId").toString());
            Long batchId = Long.valueOf(params.get("batchId").toString());
            int quantity = Integer.parseInt(params.get("quantity").toString());
            String remark = params.getOrDefault("remark", "").toString();
            Transfer transfer = transferService.createTransfer(fromSiteId, toSiteId, vaccineId, batchId, quantity, remark);
            return Result.success(transfer);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/confirm/{id}")
    public Result<Void> confirm(@PathVariable Long id) {
        try {
            transferService.confirmTransfer(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<List<Transfer>> list() {
        return Result.success(transferService.getAllTransfers());
    }
}
