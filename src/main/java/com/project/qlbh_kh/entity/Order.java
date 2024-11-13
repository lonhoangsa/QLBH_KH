package com.project.qlbh_kh.entity;

public class Order {
    private String orderId;
    private String product;
    private int quantity;
    private String date;
    private String operation;

    public Order(String orderId, String item, int quantity, String date, String operation) {
        this.orderId = orderId;
        this.product = item;
        this.quantity = quantity;
        this.date = date;
        this.operation = operation;
    }

    // Getters and setters
    public String getOrderId() { return orderId; }
    public String getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public String getDate() { return date; }
    public String getOperation() { return operation; }

    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setProduct(String product) { this.product = product; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setDate(String date) { this.date = date; }
    public void setOperation(String operation) { this.operation = operation; }

}
