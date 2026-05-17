package com.vaccine.controller;

import com.vaccine.common.Result;
import com.vaccine.entity.Reservation;
import com.vaccine.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
@CrossOrigin
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create")
    public Result<Reservation> create(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            Long siteId = Long.valueOf(params.get("siteId").toString());
            Long vaccineId = Long.valueOf(params.get("vaccineId").toString());
            LocalDate date = LocalDate.parse(params.get("date").toString());
            String timeSlot = params.get("timeSlot").toString();
            Reservation reservation = reservationService.createReservation(userId, siteId, vaccineId, date, timeSlot);
            return Result.success(reservation);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/confirm/{id}")
    public Result<Void> confirm(@PathVariable Long id) {
        try {
            reservationService.confirmVaccination(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/missed/{id}")
    public Result<Void> missed(@PathVariable Long id) {
        try {
            reservationService.markMissed(id);
            return Result.success();
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<List<Reservation>> list() {
        return Result.success(reservationService.getAllReservations());
    }

    @GetMapping("/user/{userId}")
    public Result<List<Reservation>> userReservations(@PathVariable Long userId) {
        return Result.success(reservationService.getUserReservations(userId));
    }
}
