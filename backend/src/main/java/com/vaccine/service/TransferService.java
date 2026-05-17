package com.vaccine.service;

import com.vaccine.dao.DataStore;
import com.vaccine.entity.Inventory;
import com.vaccine.entity.Transfer;
import com.vaccine.entity.VaccineBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    @Autowired
    private DataStore dataStore;

    public Transfer createTransfer(Long fromSiteId, Long toSiteId, Long vaccineId, Long batchId, int quantity, String remark) {
        Inventory fromInventory = dataStore.inventories.values().stream()
                .filter(inv -> inv.getSiteId().equals(fromSiteId) && inv.getBatchId().equals(batchId))
                .findFirst().orElse(null);

        if (fromInventory == null || fromInventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException("调出点库存不足");
        }

        VaccineBatch batch = dataStore.batches.get(batchId);
        if (batch == null || batch.getStatus() != 1) {
            throw new RuntimeException("疫苗批次不可用");
        }

        Transfer transfer = new Transfer();
        transfer.setId(dataStore.transferIdGen.getAndIncrement());
        transfer.setFromSiteId(fromSiteId);
        transfer.setToSiteId(toSiteId);
        transfer.setVaccineId(vaccineId);
        transfer.setBatchId(batchId);
        transfer.setQuantity(quantity);
        transfer.setStatus(1);
        transfer.setRemark(remark);
        transfer.setCreateTime(LocalDateTime.now());
        transfer.setUpdateTime(LocalDateTime.now());
        dataStore.transfers.put(transfer.getId(), transfer);

        fromInventory.setAvailableQuantity(fromInventory.getAvailableQuantity() - quantity);
        fromInventory.setTotalQuantity(fromInventory.getTotalQuantity() - quantity);
        fromInventory.setUpdateTime(LocalDateTime.now());

        return transfer;
    }

    public void confirmTransfer(Long transferId) {
        Transfer transfer = dataStore.transfers.get(transferId);
        if (transfer == null || transfer.getStatus() != 1) {
            throw new RuntimeException("调拨单不存在或状态错误");
        }

        Inventory toInventory = dataStore.inventories.values().stream()
                .filter(inv -> inv.getSiteId().equals(transfer.getToSiteId()) && inv.getBatchId().equals(transfer.getBatchId()))
                .findFirst().orElse(null);

        if (toInventory != null) {
            toInventory.setAvailableQuantity(toInventory.getAvailableQuantity() + transfer.getQuantity());
            toInventory.setTotalQuantity(toInventory.getTotalQuantity() + transfer.getQuantity());
            toInventory.setUpdateTime(LocalDateTime.now());
        } else {
            Inventory newInventory = new Inventory();
            newInventory.setId(dataStore.inventoryIdGen.getAndIncrement());
            newInventory.setSiteId(transfer.getToSiteId());
            newInventory.setVaccineId(transfer.getVaccineId());
            newInventory.setBatchId(transfer.getBatchId());
            newInventory.setTotalQuantity(transfer.getQuantity());
            newInventory.setReservedQuantity(0);
            newInventory.setAvailableQuantity(transfer.getQuantity());
            newInventory.setUpdateTime(LocalDateTime.now());
            dataStore.inventories.put(newInventory.getId(), newInventory);
        }

        transfer.setStatus(2);
        transfer.setUpdateTime(LocalDateTime.now());
    }

    public List<Transfer> getAllTransfers() {
        return new ArrayList<>(dataStore.transfers.values());
    }
}
