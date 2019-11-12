/***********************************************************************************************************
**   Joaquin Montero. 2DAM. DI --> T2A1                                                                 ****
**        1. FXMLExample ( Cuadro que pide usuario y contrasenia con CSS )                              ****
**            Fuente : https://docs.oracle.com/javase/8/javafx/get-started-tutorial/fxml_tutorial.htm   ****
************************************************************************************************************/
package pkg1.fxmlexample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;  
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLExample extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // Carga archivo fuente FXML ( devuelve grafico objeto resultante )
        Parent root = FXMLLoader.load(getClass().getResource("fxml_example.fxml"));
        // Titulo de ventana
        stage.setTitle("FXML Welcome");
        // Tamanio
        stage.setScene(new Scene(root, 300, 275));
        stage.show(); // Muestra
    }
    
    public static void main(String[] args) {
        Application.launch(FXMLExample.class, args);
    }
}
