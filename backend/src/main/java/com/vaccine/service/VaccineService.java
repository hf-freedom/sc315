package com.vaccine.service;

import com.vaccine.dao.DataStore;
import com.vaccine.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VaccineService {

    @Autowired
    private DataStore dataStore;

    public Vaccine createVaccine(Vaccine vaccine) {
        vaccine.setId(dataStore.vaccineIdGen.getAndIncrement());
        vaccine.setCreateTime(LocalDateTime.now());
        vaccine.setUpdateTime(LocalDateTime.now());
        dataStore.vaccines.put(vaccine.getId(), vaccine);
        return vaccine;
    }

    public List<Vaccine> getAllVaccines() {
        return new ArrayList<>(dataStore.vaccines.values());
    }

    public VaccineBatch createBatch(VaccineBatch batch) {
        batch.setId(dataStore.batchIdGen.getAndIncrement());
        batch.setAvailableQuantity(batch.getTotalQuantity());
        batch.setStatus(1);
        batch.setCreateTime(LocalDateTime.now());
        batch.setUpdateTime(LocalDateTime.now());
        dataStore.batches.put(batch.getId(), batch);
        return batch;
    }

    public List<VaccineBatch> getAllBatches() {
        return new ArrayList<>(dataStore.batches.values());
    }

    public VaccinationSite createSite(VaccinationSite site) {
        site.setId(dataStore.siteIdGen.getAndIncrement());
        site.setCreateTime(LocalDateTime.now());
        site.setUpdateTime(LocalDateTime.now());
        dataStore.sites.put(site.getId(), site);
        return site;
    }

    public List<VaccinationSite> getAllSites() {
        return new ArrayList<>(dataStore.sites.values());
    }

    public Inventory addInventory(Long siteId, Long vaccineId, Long batchId, int quantity) {
        Inventory exist = dataStore.inventories.values().stream()
                .filter(inv -> inv.getSiteId().equals(siteId) && inv.getBatchId().equals(batchId))
                .findFirst().orElse(null);

        if (exist != null) {
            exist.setTotalQuantity(exist.getTotalQuantity() + quantity);
            exist.setAvailableQuantity(exist.getAvailableQuantity() + quantity);
            exist.setUpdateTime(LocalDateTime.now());
            return exist;
        }

        Inventory inventory = new Inventory();
        inventory.setId(dataStore.inventoryIdGen.getAndIncrement());
        inventory.setSiteId(siteId);
        inventory.setVaccineId(vaccineId);
        inventory.setBatchId(batchId);
        inventory.setTotalQuantity(quantity);
        inventory.setReservedQuantity(0);
        inventory.setAvailableQuantity(quantity);
        inventory.setUpdateTime(LocalDateTime.now());
        dataStore.inventories.put(inventory.getId(), inventory);
        return inventory;
    }

    public List<Inventory> getAllInventories() {
        return new ArrayList<>(dataStore.inventories.values());
    }

    public List<Inventory> getSiteInventories(Long siteId) {
        return dataStore.inventories.values().stream()
                .filter(inv -> inv.getSiteId().equals(siteId))
                .collect(Collectors.toList());
    }

    public void expireBatch(Long batchId) {
        VaccineBatch batch = dataStore.batches.get(batchId);
        if (batch != null) {
            batch.setStatus(0);
            batch.setUpdateTime(LocalDateTime.now());
        }
    }
}
