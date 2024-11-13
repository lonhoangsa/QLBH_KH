package com.project.qlbh_kh.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class QuanLyHangXuatController extends BasicController {
    @FXML
    public void executeQuery()
    {
        if (fromDate != null) System.out.println("From Date: " + fromDateValue);
        if (toDate != null) System.out.println("To Date: " + toDateValue);
        if (selectedCustomerId != 0) System.out.println("Customer out ID " + selectedCustomerId);
        if (selectedProductName != null) System.out.println("Product Name: "+ selectedProductName);
    }
    @FXML
    void openCustomerOutList() {
        try {
            //load view cho danh sach khach hang
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/project/qlbh_kh/views/danhSachKhachHangXuatView.fxml"));
            Scene customerInListScene = new Scene(fxmlLoader.load());
            //set controller cha cho controller cua danh sach ten mat hang
            DanhSachKhachHangXuatController controller = fxmlLoader.getController();
            controller.setMainController(this);
            //tao stage moi
            Stage customerListStage = new Stage();
            customerListStage.initModality(Modality.APPLICATION_MODAL);
            customerListStage.initOwner(customerNameField.getScene().getWindow());
            customerListStage.setTitle("Danh sách khách hàng");
            customerListStage.setScene(customerInListScene);
            //show
            customerListStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
