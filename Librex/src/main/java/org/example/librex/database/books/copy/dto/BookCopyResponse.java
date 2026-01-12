package org.example.librex.database.books.copy.dto;

public class BookCopyResponse {
    private Integer id;
    private String inventoryNumber;
    private boolean available;
    private String condition;

    public BookCopyResponse(Integer id, String inventoryNumber, boolean available, String condition) {
        this.id = id;
        this.inventoryNumber = inventoryNumber;
        this.available = available;
        this.condition = condition;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}