package lk.ijse.mobileshop.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import lk.ijse.mobileshop.Util.validationutil;
import lk.ijse.mobileshop.db.dbconnection;
import lk.ijse.mobileshop.dto.Reload;
import lk.ijse.mobileshop.model.ReloadModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReloadFormController implements Initializable {

    public TableColumn colReloadId;
    public TableColumn colEmpId;
    public TableColumn colAmount;
    public TableView tblReload;

    @FXML
    private JFXTextField txtEmpid;

    @FXML
    private JFXTextField txtreId;

    @FXML
    private JFXTextField txtreamount;

    @FXML
    void Redelete(ActionEvent event) throws SQLException {
        Optional<ButtonType> are_you_sure = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure", ButtonType.YES, ButtonType.NO).showAndWait();
        if (are_you_sure.isPresent()) {
            if (are_you_sure.get().equals(ButtonType.YES)) {
                boolean delete = ReloadModel.delete(txtreId.getText());
                if (delete) {
                    new Alert(Alert.AlertType.INFORMATION, "Deleted Successful :)").show();
                    return;
                }
            }
            new Alert(Alert.AlertType.ERROR, "Delete Failed").show();

        }
        setData();
        generateNexttxtreid();
    }

    @FXML
    void Resave(ActionEvent event) throws SQLException {
        if(!isValidated()){
            new Alert(Alert.AlertType.WARNING,
                    "Fill All Fields Correctly !",
                    ButtonType.OK
            ).show();
            return;
        }
        boolean isCreate = false;
        try {
            isCreate = ReloadModel.saveReload(correctData());
            if (isCreate) {
                new Alert(Alert.AlertType.CONFIRMATION, "Save reload Successfully...");


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        txtEmpid.clear();
        txtreamount.clear();
        generateNexttxtreid();
        setData();
    }
    public void btnSearch(ActionEvent actionEvent) throws SQLException {
        Reload search = ReloadModel.search(txtreId.getText());
        if (search != null)
            set(search);
    }

    public void Reupdate(ActionEvent actionEvent) throws SQLException {
        try {
            boolean update = ReloadModel.update(correctData());
            if (update) {
                new Alert(Alert.AlertType.INFORMATION, "Update Successful :)").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update Failed :(").show();
            }
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setData();
        generateNexttxtreid();
    }

    private void generateNexttxtreid() {
        try {
            String nextId = ReloadModel.generateNextReload_Id();
            txtreId.setText(nextId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setTextFieldValidation(){
        validationutil.setFocus(txtreId,validationutil.ReIdPAttern);
        validationutil.setFocus( txtEmpid,validationutil.EmpIdPAttern);
        validationutil.setFocus(txtreamount,validationutil.SallaryPAttern);
    }
    private boolean isValidated(){
        if(txtreId.getFocusColor().equals(Paint.valueOf("red")) ||
                txtEmpid.getFocusColor().equals(Paint.valueOf("red")) ||
                txtreamount.getFocusColor().equals(Paint.valueOf("red"))
        ){
            return false;
        }else if(txtreId.getText().equals("") ||
                txtEmpid.getText().equals("") ||
                txtreamount.getText().equals("")
        ){
            return false;
        }
        return true;
    }

    public void visualize() {
        colReloadId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpId.setCellValueFactory(new PropertyValueFactory<>("empid"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }

    public void setData() throws SQLException {
            ObservableList<Reload> items = tblReload.getItems();
            ResultSet all = ReloadModel.getAll();
            while (all.next()) {
                items.addAll(new Reload(all.getString(1)
                        , all.getString(2), all.getDouble(3)));
            }
        }

    private Reload correctData() {
        String id = txtreId.getText();
        String empid = txtEmpid.getText();
        Double amount = Double.valueOf(txtreamount.getText());
        return new Reload(id,empid,amount);
    }

    public void set(Reload ob) {
        txtEmpid.setText(ob.getEmpid());
        txtreamount.setText(String.valueOf(ob.getAmount()));
    }

    public void txtKeyPress(KeyEvent keyEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextFieldValidation();
        visualize();
        try {
            setData();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        generateNexttxtreid();
    }

    public void btnprint(ActionEvent actionEvent) {
        new Thread() {
            private dbconnection DBConnection;
            @Override
            public void run() {
                try {
                    JasperDesign load = JRXmlLoader.load(new File("C:\\Users\\user\\IdeaProjects\\mobileShop\\src\\main\\resources\\JsReport\\reload_all_form.jrxml"));

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