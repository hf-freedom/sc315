package com.vaccine.service;

import com.vaccine.dao.DataStore;
import com.vaccine.entity.Reservation;
import com.vaccine.entity.VaccineBatch;
import com.vaccine.entity.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTaskService {

    @Autowired
    private DataStore dataStore;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private VaccineService vaccineService;

    private final List<String> scanResults = new ArrayList<>();

    @Scheduled(cron = "0 0 8 * * ?")
    public void scanExpiringBatches() {
        LocalDate today = LocalDate.now();
        LocalDate warningDate = today.plusDays(30);
        scanResults.add("[" + LocalDateTime.now() + "] 开始扫描临期批次...");

        for (VaccineBatch batch : dataStore.batches.values()) {
            if (batch.getStatus() == 1 && !batch.getExpireDate().isAfter(warningDate)) {
                scanResults.add("批次 " + batch.getBatchNo() + " 将在 " + batch.getExpireDate() + " 过期");
            }
            if (batch.getStatus() == 1 && !batch.getExpireDate().isAfter(today)) {
                vaccineService.expireBatch(batch.getId());
                scanResults.add("批次 " + batch.getBatchNo() + " 已过期并自动下架");
            }
        }
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void scanUpcomingReservations() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        scanResults.add("[" + LocalDateTime.now() + "] 扫描明日接种预约...");

        for (Reservation r : dataStore.reservations.values()) {
            if (r.getStatus() == 1 && r.getReservationDate().equals(tomorrow)) {
                scanResults.add("提醒: 用户 " + r.getUserId() + " 明日有接种预约");
            }
        }
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void scanMissedReservations() {
        LocalDate today = LocalDate.now();
        scanResults.add("[" + LocalDateTime.now() + "] 扫描今日爽约记录...");

        for (Reservation r : dataStore.reservations.values()) {
            if (r.getStatus() == 1 && r.getReservationDate().isBefore(today)) {
                reservationService.markMissed(r.getId());
                scanResults.add("用户 " + r.getUserId() + " 预约 " + r.getId() + " 已爽约");
            }
        }
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void scanLowInventory() {
        scanResults.add("[" + LocalDateTime.now() + "] 扫描库存不足...");

        for (Inventory inv : dataStore.inventories.values()) {
            if (inv.getAvailableQuantity() < 10) {
                scanResults.add("接种点 " + inv.getSiteId() + " 批次 " + inv.getBatchId() + " 库存不足: " + inv.getAvailableQuantity());
            }
        }
    }

    public List<String> getScanResults() {
        return new ArrayList<>(scanResults);
    }

    public void triggerScan() {
        scanExpiringBatches();
        scanUpcomingReservations();
        scanMissedReservations();
        scanLowInventory();
    }
}
