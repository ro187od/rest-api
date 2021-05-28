package com.vojtechruzicka.javafxweaverexample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
@FxmlView("infoMethod.fxml")
public class InfoMethodController extends DefaultJavaFXController{

    @FXML
    private Text textInfo;

    @FXML
    private Button closeButton;


    protected final static String TITLE = "Информация о методе";

    @FXML
    public void initialize(){
        String text = "Метод парных сравнений\n" +
                "Метод предусматривает использование эксперта, который проводит\n" +
                "оценку целей:\n" +
                "Z1, Z2, ...,Zn.\n" +
                "Согласно методу осуществляются парные сравнения целей во всех\n" +
                "возможных сочетаниях. В каждой паре выделяется наиболее\n" +
                "предпочтительная цель. И это предпочтение выражается с помощью оценки\n" +
                "по какой-либо шкале. Обработка матрицы оценок позволяет найти веса\n" +
                "целей, характеризующие их относительную важность. Одна из возможных\n" +
                "модификаций метода состоит в следующем:\n" +
                "1) составляется матрица бинарных предпочтений, в которой\n" +
                "предпочтение целей выражается с помощью булевых переменных;\n" +
                "2) определяется цена каждой цели путем суммирования булевых\n" +
                "переменных по соответствующей строке матрицы.\n";
        textInfo.setText(text);
    }
    public void reportToFile(ActionEvent actionEvent) {

        try(FileWriter fos=new FileWriter("method_report.txt"))
        {
            // перевод строки в байты

            fos.write(textInfo.getText());
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        System.out.println("The file has been written");
    }

    public void close(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
