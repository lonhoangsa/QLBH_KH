package com.project.qlbh_kh.controllers;

import com.project.qlbh_kh.entity.Product;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaoHoaDonController implements Initializable {
    @FXML
    private ChoiceBox<String> loaiHangChoiceBox;
    @FXML
    private TextField khachHangTextField;
    @FXML
    private TextField diaChiTextField;
    @FXML
    private TextField soDienThoaiTextField;

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, String> namecolumn;

    @FXML
    private TableColumn<Product, Integer> soluongcolumn;

    @FXML
    private TableColumn<Product, Integer> dongiacolumn;

    @FXML
    private TableColumn<Product, Integer> thanhtiencolumn;

    @FXML
    private TextField tongTienTextField;

    @FXML
    private TextField nguoiNhanHang ;

    @FXML
    private DatePicker ngayNhanHang;

    @FXML
    private Button luuButton;


    private ListView<String> customerListView;  // Danh sách hiển thị khách hàng

    private ObservableList<Product> ProductList;// danh sách mặt hàng trogn bảng
    private ObservableList<Product> availableProducts;//danh sách sản phẩm có sẵn trong csdl

    private Popup customerPopup; //Popup chứa danh sách khách hàng
    private Popup popupProduct; // Popup chứa danh sách mặt hàng

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        super.initialize(url,resourceBundle);
//PHẦN KHÁCH HÀNG-------------------------------------------------------------------------------------------------
        // Khởi tạo lựa chọn loại hàng
        loaiHangChoiceBox.setItems(FXCollections.observableArrayList("Hàng nhập", "Hàng xuất"));

//phan table--------------------------------------------------------------------------
        // Thiết lập độ rộng cho các cột
        namecolumn.setPrefWidth(150);
        soluongcolumn.setPrefWidth(100);
        dongiacolumn.setPrefWidth(100);
        thanhtiencolumn.setPrefWidth(100);

        // Tạo danh sách các mặt hàng trống ban đầu
        ProductList = FXCollections.observableArrayList();

        // Danh sách mặt hàng có sẵn
        availableProducts = FXCollections.observableArrayList(
                new Product("Tôn 3 ly", 19000),
                new Product("Xi măng", 50000),
                new Product("Gạch đỏ", 1200)
        );

        // Tạo Popup chứa danh sách mặt hàng
        popupProduct = new Popup();
        ListView<String> listView = new ListView<>();
        listView.setItems(FXCollections.observableArrayList(
                availableProducts.stream().map(Product::getProd_name).toList()
        ));

        // Xử lý khi chọn mặt hàng trong Popup
        listView.setOnMouseClicked(event -> {
            String selectedProductName = listView.getSelectionModel().getSelectedItem();
            if (selectedProductName != null) {
                availableProducts.stream()
                        .filter(product -> product.getProd_name().equals(selectedProductName))
                        .findFirst()
                        .ifPresent(selectedProduct -> {
                            // Thêm mặt hàng mới vào bảng với số lượng mặc định là 1
                            Product newProduct = new Product(selectedProduct.getProd_name(), selectedProduct.getPrice_in());
                            newProduct.setQuantity(1);
                            ProductList.add(newProduct);
                        });
                popupProduct.hide(); // Ẩn Popup sau khi chọn
            }
        });
//
        // Đặt ListView vào Popup
        popupProduct.getContent().add(listView);

        // Thiết lập tiêu đề của cột "Mặt Hàng" và đặt sự kiện click
        Label columnHeader = new Label("Mặt Hàng");
        columnHeader.setOnMouseClicked(this::showPopup); // Hiển thị Popup khi nhấp vào tiêu đề
        namecolumn.setGraphic(columnHeader); // Đặt Graphic cho cột để có thể lắng nghe sự kiện

        // Thiết lập các cột còn lại
        namecolumn.setCellValueFactory(cellData -> (ObservableValue<String>) cellData.getValue());
        soluongcolumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        soluongcolumn.setOnEditCommit(cellData -> {
            Product product = cellData.getRowValue();
            Integer newValue = cellData.getNewValue();

            // Kiểm tra nếu giá trị mới không hợp lệ
            if (newValue == null || newValue < 0) {
                return; // Không làm gì nếu giá trị không hợp lệ
            }

            // Cập nhật số lượng mới cho sản phẩm
            product.setQuantity(newValue);

            // Tự động cập nhật lại bảng nếu cần thiết
            table.refresh();

            // Tính lại tổng tiền
            tinhTongTien();
        });
        // Cho phép người dùng chỉnh sửa cột đơn giá

        dongiacolumn.setOnEditCommit(cellData -> {
            Product product = cellData.getRowValue();
            Double newValue = Double.valueOf(cellData.getNewValue());

            // Kiểm tra nếu giá trị mới không hợp lệ
            if (newValue == null || newValue < 0) {
                return; // Không làm gì nếu giá trị không hợp lệ
            }

            // Cập nhật đơn giá mới cho sản phẩm
            product.setPrice_in(newValue);

            // Tự động cập nhật lại bảng nếu cần thiết
            table.refresh();

            // Tính lại tổng tiền và cập nhật giá trị thành tiền
            tinhTongTien();
        });
//        thanhtiencolumn.setCellValueFactory(cellData -> dongiacolumn.cellFactoryProperty() * soluongcolumn.cellFactoryProperty());
        thanhtiencolumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            int thanhTien = (int) (product.getQuantity() * product.getPrice_in());
            return new ReadOnlyObjectWrapper<>(thanhTien);
        });

        //xoa mat hang
        table.setOnKeyPressed(event -> {
            System.out.println("Phím được nhấn: " + event.getCode()); // In ra phím được nhấn để kiểm tra
            if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
                Product selectedItem = table.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    System.out.println("dang chon mat hang"); // In ra thông báo khi hàng được chọn
                    ProductList.remove(selectedItem); // Xóa mặt hàng đã chọn
                    table.setItems(ProductList); // Cập nhật lại TableView
                    tinhTongTien();
                }
            }
        });

        table.requestFocus();
        table.setItems(ProductList);
        table.setEditable(true); // Cho phép chỉnh sửa TableView

        /// END TABLE----------------------------------------------------------------------------------


        //PHẦN NGƯỜI NHẬN, TIME------------------------------------------------
        nguoiNhanHang.setOnMouseClicked(event -> {
            System.out.println("chon nguoi nhan hang");
            ////xu ly
        });
        //END PHẦN NGƯỜI NHẬN ,TIME-----------------------------------------------


        //PHẦN NÚT LƯU--------------------------------------------
        luuButton.setOnMouseClicked(mouseEvent -> {
            System.out.println("nút lưu vào csdl");
            //xử lý lưu vào csdl
        });
        //END PHẦN NÚT LƯU---------------------------------------------
    }


    // Hiển thị Popup tại vị trí cụ thể khi nhấp vào tiêu đề cột "Mặt Hàng"
    private void showPopup(MouseEvent event) {
        // Kiểm tra nếu popupProduct đã hiển thị thì không mở lại
        if (popupProduct.isShowing()) {
            return; // Thoát khỏi phương thức nếu popupProduct đã được hiển thị
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("com/project/qlbh_kh/views/product_popup.fxml"));
            Parent popupRoot = loader.load();

            PopUpController popupController = loader.getController();
            popupController.setHelloController(this); // Truyền tham chiếu
            // Truyền danh sách sản phẩm

            Scene popupScene = new Scene(popupRoot);
            Stage popupStage = new Stage();
            popupStage.setScene(popupScene);
            popupStage.setTitle("Chọn Mặt Hàng");
            popupStage.setResizable(false);
            popupStage.setX(event.getScreenX());
            popupStage.setY(event.getScreenY());
            popupStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addProductToTable(Product product) {
        boolean exists = ProductList.stream()
                .anyMatch(matHang -> matHang.getProd_name().equals(product.getProd_name()));

        if (exists) {
            // Nếu sản phẩm đã có trong danh sách, không thêm mới mà chỉ tăng số lượng
            Product existingProduct = ProductList.stream()
                    .filter(matHang -> matHang.getProd_name().equals(product.getProd_name()))
                    .findFirst().orElse(null);
            if (existingProduct != null) {
                existingProduct.setQuantity(existingProduct.getQuantity() + 1);
                tinhTongTien();
            }
        } else {
            // Thêm mặt hàng mới vào danh sách
            Product newProduct = new Product(product.getProd_name(), product.getPrice_in());
            newProduct.setQuantity(1);
            ProductList.add(newProduct);
            tinhTongTien();
        }
        table.setItems(ProductList); // Cập nhật lại TableView
    }

    private void tinhTongTien() {
        Double tongTien = ProductList.stream()
                .mapToDouble(product -> product.getPrice_in() * product.getQuantity())
                .sum();
        tongTienTextField.setText(String.valueOf(tongTien));
    }
}
