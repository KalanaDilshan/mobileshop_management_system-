package lk.ijse.mobileshop.Util;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Paint;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validationutil {
    public static final Pattern namePattern = Pattern.compile("^[A-z ]{3,30}$");
    public static final Pattern contactPattern = Pattern.compile("^(071|072|073|074|070|075|076|077|078|079)[-]?[0-9]{7}$");
    public static final Pattern CustIdPAttern = Pattern.compile("^(C0-)[0-9]{3}$");
    public static final Pattern AddressPAttern = Pattern.compile("^[A-z0-9/ ]{3,30}$");
    public static final Pattern EmpIdPAttern = Pattern.compile("^(Em-)[0-9]{3}$");
    public static final Pattern SallaryPAttern = Pattern.compile("^()[0-9]{2,7}$");
    public static final Pattern ItemIdPAttern = Pattern.compile("^(It-)[0-9]{3}$");
    public static final Pattern QtyPAttern= Pattern.compile("^()[0-9]{2,7}$");
    public static final Pattern PayIdPAttern = Pattern.compile("^(Pa-)[0-9]{3}$");
    public static final Pattern OderIdPAttern = Pattern.compile("^(Or-)[0-9]{3}$");
    public static final Pattern RepIdPattern = Pattern.compile("^(Re-)[0-9]{3}$");
    public static final Pattern ReIdPAttern = Pattern.compile("^(Re-)[0-9]{3}$");
    public static final Pattern SupIdPAttern = Pattern.compile("^[0-9]{3}$");

    public static void setFocus(JFXTextField textField, Pattern pattern) {
        textField.setOnKeyReleased(keyEvent -> {
            Matcher matcher = pattern.matcher(textField.getText());

            if (textField.getText().isEmpty() || textField.getText().isBlank() || !matcher.matches() ){
                textField.setFocusColor(Paint.valueOf("red"));
                textField.setUnFocusColor(Paint.valueOf("red"));
            }else {
                textField.setFocusColor(Paint.valueOf("blue"));
                textField.setUnFocusColor(Paint.valueOf("blue"));
            }

        });
    }
}
