/***********************************************************************************************************
** Nombre :   Joaquin Montero. 2DAM. DI --> T2A3                                                        ****
** Ejercicio : FXMLTableView ( Libreta de direcciones con FXML )                                        ****
** Fuente : https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/table-view.htm#CJAGAAEE     ****
************************************************************************************************************/
package pkg3.fxmltableview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FXMLTableView extends Application 
{
    
    @Override
    public void start(Stage primaryStage) throws Exception 
    {
        primaryStage.setTitle("FXML TableView Example");
        Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("fxml_tableview.fxml"));
        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.show();
    }
 
    public static void main(String[] args) 
    {
        launch(args);
    }
}
