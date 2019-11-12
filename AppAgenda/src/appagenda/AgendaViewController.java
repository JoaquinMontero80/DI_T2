/***********************************************************************************************************
** Nombre :   Joaquin Montero. 2DAM. DI --> T2A4                                                        ****
** Ejercicio : AppAgenda ( Agenda con DB apache derbie                                                  ****
** Fuente : https://javiergarciaescobedo.es/programacion-en-java/9-bases-de-datos                       ****
**          pdf ( Desde página 26 hasta final de PDF )                                                  ****
************************************************************************************************************/
package appagenda;

import entidades.Persona;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.Query;

// Clase controladora de ka clase AgendaView.fxml
public class AgendaViewController implements Initializable 
{
    // Generado desde AgendaView - makecontroler, una vez que hemos creado gráfica desde scene builder
    @FXML
    private TableView<Persona> tableViewAgenda;
    @FXML
    private TableColumn<Persona, String> columnNombre;  // Cambio interrogante por tabla Persona
    @FXML
    private TableColumn<Persona, String> columnApellidos; // Cambio interrogante por tabla Persona, String
    @FXML
    private TableColumn<Persona, String> columnEmail;
    @FXML
    private TableColumn<Persona, String> columnProvincia;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private Button buttonGuardar; 
    @FXML
    private AnchorPane rootAgendaView;
    
    private EntityManager entityManager;
    // Si usuario modifica en tabla algún dato, hay que guardar dicho cambio
    // variable para guardar el objeto correspondiente a la persona que se haya seleccionado
    private Persona personaSeleccionada;
 
//=========================================================================================
    // Instancia EntityManager
    // El controlador obtiene acceso al objeto EntityManager atraves del metodo "set"
    public void setEntityManager(EntityManager entityManager) 
    {
        this.entityManager = entityManager;
    }
 
//==============================================================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // Asocia columnas a propiedades de la clase entidad ( en este caso Persona ) 
        // Propiedad de la clase persona se muestra en columnNombre
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // cellData.getValue()obtiene el objeto Persona correspondiente a una determinada fila del TableView.
        // método getProvincia()para obtener el objeto Provincia
        // método getNombre() para obtener como String el nombre de la provincia,
        columnProvincia.setCellValueFactory( cellData-> 
        {
            SimpleStringProperty property = new SimpleStringProperty();
            // Comprueba que si una determinada persona no tiene asociada ninguna provincia
            // Evita un NullPointerException 
            if(cellData.getValue().getProvincia() != null) 
            {
                property.setValue(cellData.getValue().getProvincia().getNombre());
            }
             return property;
        });
        
        // se ejecutará cada vez que se cambia la selección en el TableView.
        // Guarda en variable personaSeleccionada nuevo valor aniadido
        tableViewAgenda.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue)-> {
                    personaSeleccionada = newValue;
                    if(personaSeleccionada != null)
                    {
                        textFieldNombre.setText(personaSeleccionada.getNombre());
                        textFieldApellidos.setText(personaSeleccionada.getApellidos());
                    }
                    else
                    {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
        });
    }

//====================================================================
    // consulta a la base de datos y rellena el TableView.
    // La lista al TableView con el método setItems, que requiere convertir la lista de tipo List al tipo ->
    // -> ObservableArrayList, lo cual se hace con el método FXCollections.observableArrayList.
    public void cargarTodasPersonas() 
    {
        Query queryPersonaFindAll = entityManager.createNamedQuery("Persona.findAll");
        List<Persona> listPersona = queryPersonaFindAll.getResultList();
        tableViewAgenda.setItems(FXCollections.observableArrayList(listPersona));
    } 

//========================================================================
   
    // Accion generada al pulsar el boton guardar ( indicado desde scene builder )
    // Guarda los cambios introducidos por el usuario
    @FXML
    private void onActionButtonGuardar(ActionEvent event) 
    {
        // Comprueba que hay algun registro en TableView
        if (personaSeleccionada != null) 
        {
            // Actualiza los valores recogidos en el TextField
            personaSeleccionada.setNombre(textFieldNombre.getText());
            personaSeleccionada.setApellidos(textFieldApellidos.getText());
        }
        
        // Actualiza en tableView los nuevos valores
            // obtiene el número de la fila seleccionada en el TableView y luego se vuelve a asignar el
            //  mismo objeto a esa fila, de manera que se mostrarán los nuevos valores que contenga el objeto.
        entityManager.getTransaction().begin();
        entityManager.merge(personaSeleccionada);
        entityManager.getTransaction().commit();
        
        // Actualiza los datos de la persona en la tabla
        int numFilaSeleccionada = tableViewAgenda.getSelectionModel().getSelectedIndex();
        tableViewAgenda.getItems().set(numFilaSeleccionada, personaSeleccionada);
        
        // usuario pueda seguir moviéndose por su contenido usando el teclado
        // evitamos que foco de ventana permanezca en boton guardar
        TablePosition pos = new TablePosition(tableViewAgenda, numFilaSeleccionada, null);
        tableViewAgenda.getFocusModel().focus(pos);
        tableViewAgenda.requestFocus();
    }

//====================================================================================
    
    @FXML
    private void onActionButtonNuevo(ActionEvent event) 
    {
        try 
        {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new
            FXMLLoader(getClass().getResource("Agenda2View.fxml"));
            Parent rootAgenda2View = fxmlLoader.load();
            
            Agenda2ViewController agenda2ViewController = (Agenda2ViewController)fxmlLoader.getController();
            agenda2ViewController.setRootAgendaView(rootAgendaView);
            // Pasa la persona seleccionada de la vista lista a la vista detalle
            agenda2ViewController.setTableViewPrevio(tableViewAgenda);
            personaSeleccionada = new Persona();
            agenda2ViewController.setPersona(entityManager, personaSeleccionada, true);
            

            // Esto muestra las provincias
            agenda2ViewController.mostrarDatos();
            
            // Ocultar la vista de la lista
            rootAgendaView.setVisible(false);
            // Añadir la vista de detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane)rootAgendaView.getScene().getRoot();
            rootMain.getChildren().add(rootAgenda2View);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(AgendaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionButtonEditar(ActionEvent event) 
    {
        try 
        {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new
            FXMLLoader(getClass().getResource("Agenda2View.fxml"));
            Parent rootAgenda2View = fxmlLoader.load();
            
            Agenda2ViewController agenda2ViewController = (Agenda2ViewController)fxmlLoader.getController();
            agenda2ViewController.setRootAgendaView(rootAgendaView);
            
            agenda2ViewController.setPersona(entityManager, personaSeleccionada, false);
            // Muestra la informacion de la persona
            agenda2ViewController.mostrarDatos();
            // Ocultar la vista de la lista
            rootAgendaView.setVisible(false);
            // Añadir la vista de detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane)rootAgendaView.getScene().getRoot();
            rootMain.getChildren().add(rootAgenda2View);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(AgendaViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) 
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText("¿Desea suprimir el siguiente registro?");
        alert.setContentText(personaSeleccionada.getNombre() + " "
        + personaSeleccionada.getApellidos());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            // Acciones a realizar si el usuario acepta
            entityManager.getTransaction().begin();
            entityManager.merge(personaSeleccionada);
            entityManager.remove(personaSeleccionada);
            entityManager.getTransaction().commit();
            tableViewAgenda.getItems().remove(personaSeleccionada);
            tableViewAgenda.getFocusModel().focus(null);
            tableViewAgenda.requestFocus();
        }
        else 
        {
            // Acciones a realizar si el usuario cancela
            int numFilaSeleccionada = tableViewAgenda.getSelectionModel().getSelectedIndex();
            tableViewAgenda.getItems().set(numFilaSeleccionada, personaSeleccionada);
            TablePosition pos = new TablePosition(tableViewAgenda, numFilaSeleccionada, null);
            tableViewAgenda.getFocusModel().focus(pos);
            tableViewAgenda.requestFocus();
        }
    }
}
