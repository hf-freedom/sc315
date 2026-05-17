package com.vaccine.service;

import com.vaccine.dao.DataStore;
import com.vaccine.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private DataStore dataStore;

    public Reservation createReservation(Long userId, Long siteId, Long vaccineId, LocalDate date, String timeSlot) {
        User user = dataStore.users.get(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        Vaccine vaccine = dataStore.vaccines.get(vaccineId);
        if (vaccine == null) {
            throw new RuntimeException("疫苗不存在");
        }

        List<Reservation> userReservations = dataStore.reservations.values().stream()
                .filter(r -> r.getUserId().equals(userId) && r.getVaccineId().equals(vaccineId))
                .collect(Collectors.toList());

        int nextDose = userReservations.size() + 1;
        if (nextDose > vaccine.getDosesRequired()) {
            throw new RuntimeException("该疫苗已完成全部接种");
        }

        if (nextDose > 1) {
            Reservation lastReservation = userReservations.stream()
                    .filter(r -> r.getStatus() == 2)
                    .max(Comparator.comparing(Reservation::getReservationDate))
                    .orElse(null);
            if (lastReservation == null) {
                throw new RuntimeException("上一针未完成接种");
            }
            LocalDate minNextDate = lastReservation.getReservationDate().plusDays(vaccine.getIntervalDays());
            if (date.isBefore(minNextDate)) {
                throw new RuntimeException("接种间隔不足，最早可预约日期: " + minNextDate);
            }
        }

        Inventory targetInventory = null;
        for (Inventory inv : dataStore.inventories.values()) {
            if (inv.getSiteId().equals(siteId) && inv.getVaccineId().equals(vaccineId)
                    && inv.getAvailableQuantity() > 0) {
                VaccineBatch batch = dataStore.batches.get(inv.getBatchId());
                if (batch != null && batch.getStatus() == 1 && !batch.getExpireDate().isBefore(date)) {
                    targetInventory = inv;
                    break;
                }
            }
        }

        if (targetInventory == null) {
            throw new RuntimeException("该接种点当前疫苗库存不足");
        }

        if (user.getRiskLevel() == null || user.getRiskLevel() < 2) {
            long highRiskCount = dataStore.reservations.values().stream()
                    .filter(r -> r.getSiteId().equals(siteId) && r.getReservationDate().equals(date)
                            && r.getStatus() == 1)
                    .count();
            if (highRiskCount >= 20) {
                throw new RuntimeException("今日预约名额已满，请高风险人群优先预约");
            }
        }

        targetInventory.setAvailableQuantity(targetInventory.getAvailableQuantity() - 1);
        targetInventory.setReservedQuantity(targetInventory.getReservedQuantity() + 1);
        targetInventory.setUpdateTime(LocalDateTime.now());

        Reservation reservation = new Reservation();
        reservation.setId(dataStore.reservationIdGen.getAndIncrement());
        reservation.setUserId(userId);
        reservation.setSiteId(siteId);
        reservation.setVaccineId(vaccineId);
        reservation.setBatchId(targetInventory.getBatchId());
        reservation.setReservationDate(date);
        reservation.setTimeSlot(timeSlot);
        reservation.setStatus(1);
        reservation.setDoseNumber(nextDose);
        reservation.setCreateTime(LocalDateTime.now());
        reservation.setUpdateTime(LocalDateTime.now());
        dataStore.reservations.put(reservation.getId(), reservation);

        return reservation;
    }

    public void cancelReservation(Long reservationId) {
        Reservation reservation = dataStore.reservations.get(reservationId);
        if (reservation == null) {
            throw new RuntimeException("预约不存在");
        }
        if (reservation.getStatus() != 1) {
            throw new RuntimeException("该预约状态不可取消");
        }
        reservation.setStatus(0);
        reservation.setUpdateTime(LocalDateTime.now());

        Inventory inventory = dataStore.inventories.values().stream()
                .filter(inv -> inv.getSiteId().equals(reservation.getSiteId())
                        && inv.getBatchId().equals(reservation.getBatchId()))
                .findFirst().orElse(null);
        if (inventory != null) {
            inventory.setReservedQuantity(inventory.getReservedQuantity() - 1);
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + 1);
            inventory.setUpdateTime(LocalDateTime.now());
        }
    }

    public void confirmVaccination(Long reservationId) {
        Reservation reservation = dataStore.reservations.get(reservationId);
        if (reservation == null) {
            throw new RuntimeException("预约不存在");
        }
        if (reservation.getStatus() != 1) {
            throw new RuntimeException("该预约状态不可确认接种");
        }

        reservation.setStatus(2);
        reservation.setUpdateTime(LocalDateTime.now());

        Inventory inventory = dataStore.inventories.values().stream()
                .filter(inv -> inv.getSiteId().equals(reservation.getSiteId())
                        && inv.getBatchId().equals(reservation.getBatchId()))
                .findFirst().orElse(null);
        if (inventory != null) {
            inventory.setReservedQuantity(inventory.getReservedQuantity() - 1);
            inventory.setTotalQuantity(inventory.getTotalQuantity() - 1);
            inventory.setUpdateTime(LocalDateTime.now());
        }

        Vaccine vaccine = dataStore.vaccines.get(reservation.getVaccineId());
        if (vaccine != null && reservation.getDoseNumber() < vaccine.getDosesRequired()) {
            reservation.setNextSuggestDate(reservation.getReservationDate().plusDays(vaccine.getIntervalDays()));
        }
    }

    public void markMissed(Long reservationId) {
        Reservation reservation = dataStore.reservations.get(reservationId);
        if (reservation == null) {
            throw new RuntimeException("预约不存在");
        }
        if (reservation.getStatus() != 1) {
            throw new RuntimeException("该预约状态不可标记爽约");
        }

        reservation.setStatus(3);
        reservation.setUpdateTime(LocalDateTime.now());

        Inventory inventory = dataStore.inventories.values().stream()
                .filter(inv -> inv.getSiteId().equals(reservation.getSiteId())
                        && inv.getBatchId().equals(reservation.getBatchId()))
                .findFirst().orElse(null);
        if (inventory != null) {
            inventory.setReservedQuantity(inventory.getReservedQuantity() - 1);
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + 1);
            inventory.setUpdateTime(LocalDateTime.now());
        }
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(dataStore.reservations.values());
    }

    public List<Reservation> getUserReservations(Long userId) {
        return dataStore.reservations.values().stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
