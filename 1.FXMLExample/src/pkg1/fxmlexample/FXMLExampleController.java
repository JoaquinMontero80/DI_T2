/***********************************************************************************************************
**   Joaquin Montero. 2DAM. DI --> T2A1                                                                 ****
**        1. FXMLExample (Clase que usa los metodos de controlador marcado por FXML)                    ****
**            Fuente : https://docs.oracle.com/javase/8/javafx/get-started-tutorial/fxml_tutorial.htm   ****
************************************************************************************************************/
package pkg1.fxmlexample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
 
public class FXMLExampleController {
    @FXML private Text actiontarget;
    
    // Establece la action cuand pulsa el boton
    @FXML protected void handleSubmitButtonAction(ActionEvent event) 
    {
        // Muestra mensaje al enviar pulsando boton
        actiontarget.setText("Sign in button pressed");
    }

}