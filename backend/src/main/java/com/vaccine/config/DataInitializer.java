package com.vaccine.config;

import com.vaccine.entity.*;
import com.vaccine.service.VaccineService;
import com.vaccine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private VaccineService vaccineService;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) {
        Vaccine vaccine1 = new Vaccine();
        vaccine1.setName("新冠疫苗");
        vaccine1.setManufacturer("国药集团");
        vaccine1.setDosesRequired(3);
        vaccine1.setIntervalDays(21);
        vaccine1.setDescription("新冠灭活疫苗");
        vaccineService.createVaccine(vaccine1);

        Vaccine vaccine2 = new Vaccine();
        vaccine2.setName("HPV疫苗");
        vaccine2.setManufacturer("默沙东");
        vaccine2.setDosesRequired(3);
        vaccine2.setIntervalDays(60);
        vaccine2.setDescription("九价HPV疫苗");
        vaccineService.createVaccine(vaccine2);

        Vaccine vaccine3 = new Vaccine();
        vaccine3.setName("流感疫苗");
        vaccine3.setManufacturer("赛诺菲");
        vaccine3.setDosesRequired(1);
        vaccine3.setIntervalDays(0);
        vaccine3.setDescription("季节性流感疫苗");
        vaccineService.createVaccine(vaccine3);

        VaccinationSite site1 = new VaccinationSite();
        site1.setName("朝阳区社区卫生服务中心");
        site1.setAddress("北京市朝阳区XX路100号");
        site1.setContact("张医生");
        site1.setPhone("010-12345678");
        site1.setWorkHours("周一至周五 8:00-17:00");
        vaccineService.createSite(site1);

        VaccinationSite site2 = new VaccinationSite();
        site2.setName("海淀区医院");
        site2.setAddress("北京市海淀区XX路200号");
        site2.setContact("李医生");
        site2.setPhone("010-87654321");
        site2.setWorkHours("周一至周日 8:00-18:00");
        vaccineService.createSite(site2);

        VaccinationSite site3 = new VaccinationSite();
        site3.setName("西城区妇幼保健院");
        site3.setAddress("北京市西城区XX路50号");
        site3.setContact("王医生");
        site3.setPhone("010-11112222");
        site3.setWorkHours("周一至周五 8:30-17:30");
        vaccineService.createSite(site3);

        VaccineBatch batch1 = new VaccineBatch();
        batch1.setVaccineId(1L);
        batch1.setBatchNo("CV202601001");
        batch1.setProductionDate(LocalDate.now().minusMonths(1));
        batch1.setExpireDate(LocalDate.now().plusMonths(11));
        batch1.setTotalQuantity(100);
        vaccineService.createBatch(batch1);

        VaccineBatch batch2 = new VaccineBatch();
        batch2.setVaccineId(1L);
        batch2.setBatchNo("CV202601002");
        batch2.setProductionDate(LocalDate.now().minusMonths(2));
        batch2.setExpireDate(LocalDate.now().plusDays(20));
        batch2.setTotalQuantity(50);
        vaccineService.createBatch(batch2);

        VaccineBatch batch3 = new VaccineBatch();
        batch3.setVaccineId(2L);
        batch3.setBatchNo("HPV2026001");
        batch3.setProductionDate(LocalDate.now().minusMonths(3));
        batch3.setExpireDate(LocalDate.now().plusMonths(24));
        batch3.setTotalQuantity(30);
        vaccineService.createBatch(batch3);

        VaccineBatch batch4 = new VaccineBatch();
        batch4.setVaccineId(3L);
        batch4.setBatchNo("FLU2026001");
        batch4.setProductionDate(LocalDate.now().minusMonths(1));
        batch4.setExpireDate(LocalDate.now().plusMonths(6));
        batch4.setTotalQuantity(80);
        vaccineService.createBatch(batch4);

        vaccineService.addInventory(1L, 1L, 1L, 50);
        vaccineService.addInventory(1L, 1L, 2L, 20);
        vaccineService.addInventory(2L, 1L, 1L, 30);
        vaccineService.addInventory(2L, 2L, 3L, 20);
        vaccineService.addInventory(3L, 3L, 4L, 40);
        vaccineService.addInventory(3L, 1L, 2L, 30);

        User user1 = new User();
        user1.setIdCard("110101199001011234");
        user1.setName("张三");
        user1.setPhone("13800138001");
        user1.setRiskLevel(1);
        user1.setBirthDate(LocalDate.of(1990, 1, 1));
        user1.setAddress("北京市朝阳区");
        userService.createUser(user1);

        User user2 = new User();
        user2.setIdCard("110101198505055678");
        user2.setName("李四");
        user2.setPhone("13800138002");
        user2.setRiskLevel(3);
        user2.setBirthDate(LocalDate.of(1985, 5, 5));
        user2.setAddress("北京市海淀区");
        userService.createUser(user2);

        User user3 = new User();
        user3.setIdCard("110101199508089012");
        user3.setName("王五");
        user3.setPhone("13800138003");
        user3.setRiskLevel(2);
        user3.setBirthDate(LocalDate.of(1995, 8, 8));
        user3.setAddress("北京市西城区");
        userService.createUser(user3);
    }
}
