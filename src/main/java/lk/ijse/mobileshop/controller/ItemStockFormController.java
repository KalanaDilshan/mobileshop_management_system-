package lk.ijse.mobileshop.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import lk.ijse.mobileshop.Util.validationutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.tm.ItemStockTm;
import lk.ijse.mobileshop.dto.ItemStock;
import lk.ijse.mobileshop.model.ItemStockModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemStockFormController implements Initializable {

    public TableView tblItemStock;
    public TableColumn colItemStockId;
    public TableColumn colItemName;
    public JFXTextField txtItemName;
    public JFXTextField txtItemStockQty;
    public TableColumn colItemQty;
    public JFXButton ItemUpdate;
    public JFXButton ItemSave;
    public JFXButton ItemDelete;
    public JFXButton ItemSave1;
    public JFXTextField txtUnitPrice;
    public TableColumn colUnitPrice;

    @FXML
    private JFXTextField txtItemId;

    @FXML
    void btnItemDelete(ActionEvent event) throws SQLException {
            Optional<ButtonType> are_you_sure = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
            if(are_you_sure.isPresent()){
                if(are_you_sure.get().equals(ButtonType.YES)){
                    boolean delete = ItemStockModel.delete(txtItemId.getText());
                    if(delete){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted Successful :)").show();
                        return;
                    }
                }
                new Alert(Alert.AlertType.ERROR,"Delete Failed").show();

            }
        setData();
        generateNexttxtItemId();
    }
    @FXML
    void btnSearch(ActionEvent event) throws SQLException {
        ItemStock search = ItemStockModel.search(txtItemId.getText());
        if(search!=null)
            set(search);
    }


    @FXML
    void  btnItemSave(ActionEvent event) throws SQLException {
        if(!isValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Fields Correctly !",
                    ButtonType.OK
            ).show();
            return;
        }
        boolean isCreate = false;
        try {
            isCreate = ItemStockModel.saveItemStock(correctData());
            if (isCreate) {
                new Alert(Alert.AlertType.CONFIRMATION, "Save item Successfully...");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        generateNexttxtItemId();
        txtItemStockQty.clear();
        txtItemName.clear();
        txtUnitPrice.clear();
        setData();
        generateNexttxtItemId();
    }
    @FXML
    void btnItemUpdate(ActionEvent event)  throws SQLException {
        try {
            boolean update = ItemStockModel.update(correctData());
            if(update){
                new Alert(Alert.AlertType.INFORMATION,"Update Successful :)").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Update Failed :(").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setData();
        generateNexttxtItemId();
    }


    public void set(ItemStock ob){
        txtItemStockQty.setText(String.valueOf(ob.getQty()));
        txtItemName.setText(ob.getItem_Name());
        txtUnitPrice.setText(String.valueOf(ob.getUnitPrice()));
    }

    @FXML
    void txtKeyPress(KeyEvent event) {

    }
    public void setTextFieldValidation(){
        validationutil.setFocus(txtItemId,validationutil.ItemIdPAttern);
        validationutil.setFocus( txtItemStockQty,validationutil.QtyPAttern);
        validationutil.setFocus( txtItemName,validationutil.namePattern);
         validationutil.setFocus( txtUnitPrice,validationutil.SallaryPAttern);


    }
    private boolean isValidated(){
        if(txtItemId.getFocusColor().equals(Paint.valueOf("red")) ||
                txtItemStockQty.getFocusColor().equals(Paint.valueOf("red"))||
                txtItemName.getFocusColor().equals(Paint.valueOf("red"))||
                txtUnitPrice.getFocusColor().equals(Paint.valueOf("red")))

        {
            return false;
        }else if(txtItemId.getText().equals("") ||
                txtItemStockQty.getText().equals("")||
                txtItemName.getText().equals("")||
                txtUnitPrice.getText().equals("")
        ){
            return false;
        }
        return true;
    }
    public void visualize(){
        colItemStockId.setCellValueFactory(new PropertyValueFactory<>("Item_Id"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colItemName.setCellValueFactory(new PropertyValueFactory<>("Item_Name"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));


    }

    public void setData() throws SQLException {
       ObservableList<ItemStockTm>obList = FXCollections.observableArrayList();
        List<ItemStock> list = ItemStockModel.getAll();

        for(ItemStock itemStock : list){
            obList.add(new ItemStockTm(
                    itemStock.getItem_Id(),
                    itemStock.getQty(),
                    itemStock.getItem_Name(),
                    itemStock.getUnitPrice()
            ));
        }
        tblItemStock.setItems(obList);
    }


    private ItemStock correctData() {
        String id = txtItemId.getText();
        int Qty = Integer.parseInt(txtItemStockQty.getText());
        String Item_Name =txtItemName.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());

        return new ItemStock(id, Qty,Item_Name,unitPrice);
    }
    private void generateNexttxtItemId() {
        try {
            String nextId = ItemStockModel.generateNextItemId();
            txtItemId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextFieldValidation();
        visualize();
        try {
            setData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        generateNexttxtItemId();
    }

    public void btnprint(ActionEvent actionEvent) {
        new Thread() {
            private dbconnection DBConnection;
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\ItemStock_all_form.jrxml"));

                    JasperReport js = JasperCompileManager.compileReport(load);

                    JasperPrint jp = JasperFillManager.fillReport(js, null, DBConnection.getInstance().getConnection());

                    JasperViewer viewer = new JasperViewer(jp , false);
                    viewer.show();

                } catch (JRException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                this.stop();
            }
        }.start();
    }
}
