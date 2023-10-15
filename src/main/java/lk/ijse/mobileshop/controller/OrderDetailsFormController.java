package lk.ijse.mobileshop.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.Employee;
import lk.ijse.mobileshop.dto.OrderDetails;
import lk.ijse.mobileshop.model.CustomerModel;
import lk.ijse.mobileshop.model.EmployeeModel;
import lk.ijse.mobileshop.model.OrderDetailModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OrderDetailsFormController implements Initializable {
    public TableView tblOrderDetails;
    public AnchorPane orderancorpane;
    public TableColumn colItemId;
    public TableColumn colOderId;
    public TableColumn ColCustId;
    public TableColumn colDate;
    public TableColumn colQty;
    public TableColumn colPrice;
    public JFXButton orderDetails;


    public void btnprintorder(ActionEvent actionEvent) {
        new Thread() {
            private dbconnection DBConnection;
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\OrderDetails_all_form.jrxml"));

                    JasperReport js = JasperCompileManager.compileReport(load);

                    JasperPrint jp = JasperFillManager.fillReport(js, null,DBConnection.getInstance().getConnection());

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
    public void setData() throws SQLException {
        ObservableList<OrderDetails> items = tblOrderDetails.getItems();
        ResultSet all = OrderDetailModel.getAll();
        while (all.next()){
            items.add(new OrderDetails(all.getString(1),all.getString(2),all.getDate(3),all.getInt(4),all.getDouble(5),all.getString(6)));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        visualize();
        try {
            setData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void visualize(){
        colItemId.setCellValueFactory(new PropertyValueFactory<>("item_Id"));
        colOderId.setCellValueFactory(new PropertyValueFactory<>("order_Id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        ColCustId.setCellValueFactory(new PropertyValueFactory<>("custId"));
    }
}
