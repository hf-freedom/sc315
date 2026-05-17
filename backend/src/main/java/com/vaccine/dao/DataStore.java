package com.vaccine.dao;

import com.vaccine.entity.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class DataStore {
    public final Map<Long, Vaccine> vaccines = new ConcurrentHashMap<>();
    public final Map<Long, VaccinationSite> sites = new ConcurrentHashMap<>();
    public final Map<Long, VaccineBatch> batches = new ConcurrentHashMap<>();
    public final Map<Long, User> users = new ConcurrentHashMap<>();
    public final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    public final Map<Long, Inventory> inventories = new ConcurrentHashMap<>();
    public final Map<Long, Transfer> transfers = new ConcurrentHashMap<>();

    public final AtomicLong vaccineIdGen = new AtomicLong(1);
    public final AtomicLong siteIdGen = new AtomicLong(1);
    public final AtomicLong batchIdGen = new AtomicLong(1);
    public final AtomicLong userIdGen = new AtomicLong(1);
    public final AtomicLong reservationIdGen = new AtomicLong(1);
    public final AtomicLong inventoryIdGen = new AtomicLong(1);
    public final AtomicLong transferIdGen = new AtomicLong(1);
}
