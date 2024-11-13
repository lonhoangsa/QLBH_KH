package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Order;
import com.project.qlbh_kh.utils.JDBCUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class TruyXuatKhoController extends BasicController {
    @FXML private TableView<Order> tableView;
    @FXML private TableColumn<Order, String> orderIdColumn;
    @FXML private TableColumn<Order, String> productColumn;
    @FXML private TableColumn<Order, Integer> quantityColumn;
    @FXML private TableColumn<Order, String> dateColumn;
    @FXML private TableColumn<Order, String> operationColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Call the initialize method of the superclass to ensure it runs
        super.initialize(url, resourceBundle);

        // Set up table columns here
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        operationColumn.setCellValueFactory(new PropertyValueFactory<>("operation"));
        ObservableList<Order> data = FXCollections.observableArrayList();

        String query = "use BTL_QL_BanHang\n" +
                "SELECT \n" +
                "    order_in_tb.order_in_id AS \"mã hóa đơn\",\n" +
                "    products_tb.prod_name AS \"mặt hàng\",\n" +
                "    order_in_details_tb.quantity AS \"số lượng\",\n" +
                "    order_in_tb.order_date AS \"ngày\",\n" +
                "    'nhập' AS \"thao tác\",\n" +
                "    customers_in_tb.firstname + ' ' + customers_in_tb.lastname AS \"khách hàng\"\n" +
                "FROM \n" +
                "    order_in_tb\n" +
                "JOIN \n" +
                "    order_in_details_tb ON order_in_tb.order_in_id = order_in_details_tb.order_in_id\n" +
                "JOIN \n" +
                "    customers_in_tb ON order_in_tb.customer_in_id = customers_in_tb.customer_in_id\n" +
                "JOIN\n" +
                "\tproducts_tb on order_in_details_tb.prod_id = products_tb.prod_id\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT \n" +
                "    order_out_tb.order_out_id AS \"mã hóa đơn\",\n" +
                "    products_tb.prod_name AS \"mặt hàng\",\n" +
                "    order_out_details_tb.quantity AS \"số lượng\",\n" +
                "    order_out_tb.order_date AS \"ngày\",\n" +
                "    'xuất' AS \"thao tác\",\n" +
                "    customers_out_tb.firstname + ' ' + customers_out_tb.lastname AS \"khách hàng\"\n" +
                "FROM \n" +
                "    order_out_tb\n" +
                "JOIN \n" +
                "    order_out_details_tb ON order_out_tb.order_out_id = order_out_details_tb.order_out_id\n" +
                "JOIN \n" +
                "    customers_out_tb ON order_out_tb.customer_out_id = customers_out_tb.customer_out_id\n" +
                "JOIN\n" +
                "\tproducts_tb on order_out_details_tb.prod_id = products_tb.prod_id;\n";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                data.add(new Order(
                        rs.getString("mã hóa đơn"),
                        rs.getString("mặt hàng"),
                        rs.getInt("số lượng"),
                        rs.getString("ngày"),
                        rs.getString("thao tác")
                ));
            }
            tableView.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @FXML
    public void executeQuery()
    {
        System.out.println(selectedProductId);
        if (fromDate != null) System.out.println("From Date: " + fromDateValue);
        if (toDate != null) System.out.println("To Date: " + toDateValue);
        if (operation != null) System.out.println("Operation: " + operation);
        if (selectedProductName != null) System.out.println("Selected Product Name: " + selectedProductName);
        if (selectedProductName == null) System.out.println("Not Selected Product Name");
    }


}
