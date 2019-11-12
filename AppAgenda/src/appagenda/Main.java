/***********************************************************************************************************
** Nombre :   Joaquin Montero. 2DAM. DI --> T2A4                                                        ****
** Ejercicio : AppAgenda ( Agenda con DB apache derbie                                                  ****
** Fuente : https://javiergarciaescobedo.es/programacion-en-java/9-bases-de-datos                       ****
**          pdf ( Desde página 26 hasta final de PDF )                                                  ****
************************************************************************************************************/
package appagenda;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//Clase principal del proyecto APPAgenda
public class Main extends Application 
{
    // Atributos
    // objeto EntityManager, mantiene la información de la conexión con la base de datos.
    private EntityManagerFactory emf;
    private EntityManager em;
    
    @Override
    public void start(Stage primaryStage) throws IOException 
    {
        // Nuevo contenedor StackPane se almacena en variable ( rootmain )
        // Apila paneles o vistas una encima de otra
        // Es ahora el contenedor principal
        StackPane rootMain = new StackPane();
        // Eliminado -->    Parent root = fxmlLoader.load();
        
        // Sirve de referencia al archivo fxml creado anteriormente y que da formato a la ventana
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AgendaView.fxml"));
       
        // Aniade AgendaView.fxml a StackPane creado 
        Parent rootAgendaView = fxmlLoader.load();
        rootMain.getChildren().add(rootAgendaView);

        // Conexión a la BD creando los objetos EntityManager y
        // EntityManagerFactory
        emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        em = emf.createEntityManager();
        
        // Obtención del objeto asociado al controlador del archivo FXML
        AgendaViewController agendaViewController = (AgendaViewController) fxmlLoader.getController();
        agendaViewController.setEntityManager(em);
        agendaViewController.cargarTodasPersonas();
        
        //Configura la ventana
        Scene scene = new Scene(rootMain,600,400);
        primaryStage.setTitle("App Agenda");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    //Se encarga de cerrar la conexion con la base de datos al cerrarse la aplicacion
    @Override
    public void stop() throws Exception 
    {
        // Elimino super.stop() para que no implemente su propio metodo
        em.close();
        emf.close();
        try 
        {
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        } 
        catch (SQLException ex) {}
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        launch(args);
    } 
}
