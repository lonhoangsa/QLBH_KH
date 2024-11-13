package com.project.qlbh_kh.entity;

public class Product {
    private String prod_name;
    private int prod_id;
    private int quantity;
    private String unit_sold;
    private double price_in;
    private double price_out;
    public Product(int prod_id, String prod_name)
    {
        this.prod_name = prod_name;
        this.prod_id = prod_id;
    }
    public Product(String prod_name, int quantity)
    {
        this.prod_name = prod_name;
        this.quantity = quantity;
    }
    public int getProd_id() {
        return prod_id;
    }

    //    public product(String tenMatHang, int soLuong) {
//        this.prod_name = tenMatHang;
//        this.quantity = soLuong;
//    }
    public Product(String prod_name)
    {
        this.prod_name = prod_name;
    }
    public int getQuantity() {
        return quantity;
    }

    public String getProd_name() {
        return prod_name;
    }
    @Override
    public String toString() {
        return this.prod_name; // Trả về tên sản phẩm để hiển thị
    }
}
