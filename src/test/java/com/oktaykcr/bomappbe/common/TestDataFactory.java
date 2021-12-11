package com.oktaykcr.bomappbe.common;

import com.oktaykcr.bomappbe.model.bom.Bom;
import com.oktaykcr.bomappbe.model.component.Component;
import com.oktaykcr.bomappbe.model.component.ComponentUsed;
import com.oktaykcr.bomappbe.model.inventory.Inventory;
import com.oktaykcr.bomappbe.model.user.User;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestDataFactory {

    public static Bom createBom() {
        Bom bom = new Bom();
        bom.setUser(createUser());
        bom.setTitle("title");
        bom.setDescription("description");
        return new Bom("title", "description");
    }

    public static User createUser() {
        return new User();
    }

    public static Inventory createInventory() {
        Inventory inventory = new Inventory();
        inventory.setUser(createUser());
        return inventory;
    }

    public static Component createComponent() {
        Component component = new Component();
        component.setDescription("des");
        component.setImageUrl("image url");
        component.setManufacturerName("manufacturer");

        Inventory inventory = new Inventory();
        inventory.setUser(createUser());
        component.setInventory(inventory);
        component.setSupplierLink("supplier");
        component.setQuantityOnHand(500);
        component.setMouserPartNumber("71-TNPU06037K50BZEN0");
        return component;
    }

    public static ComponentUsed createComponentUsed() {
        ComponentUsed componentUsed = new ComponentUsed();
        componentUsed.setComponent(createComponent());
        componentUsed.setBom(createBom());
        componentUsed.setCost(10);
        componentUsed.setQuantity(100);
        componentUsed.setLeadTime(1);
        return componentUsed;
    }

    public static String createMouserData() {
        return readDataFile("mouserData.json");
    }

    public static String createEmptyMouserData() {
        return readDataFile("mouserDataEmpty.json");
    }

    private static String readDataFile(String fileName) {
        String content = "";
        File file;
        try {
            file = ResourceUtils.getFile("classpath:data/" + fileName);
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
