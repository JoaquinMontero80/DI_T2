/***********************************************************************************************************
** Nombre :   Joaquin Montero. 2DAM. DI --> T2A4                                                        ****
** Ejercicio : AppAgenda ( Agenda con DB apache derbie                                                  ****
** Fuente : https://javiergarciaescobedo.es/programacion-en-java/9-bases-de-datos                       ****
**          pdf ( Desde página 26 hasta final de PDF )                                                  ****
************************************************************************************************************/
package appagenda;

import entidades.Persona;
import entidades.Provincia;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

// Clase controladora de la vista detalle de la aplicacion
public class Agenda2ViewController implements Initializable 
{
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private TextField textFieldTelefono;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldNumHijos;
    @FXML
    private TextField textFieldSalario;
    @FXML
    private CheckBox checkBoxJubilado;
    @FXML
    private ComboBox<Provincia> comboBoxProvincia;
    @FXML
    private RadioButton radioButtonSoltero;
    @FXML
    private RadioButton radioButtonCasado;
    @FXML
    private RadioButton radioButtonViudo;
    @FXML
    private DatePicker datePickerFechaNacimiento;
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private Button buttonGuardar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private AnchorPane rootAgenda2View;
    
    private Pane rootAgendaView;
    private TableView tableViewPrevio;
    private Persona persona;
    private EntityManager entityManager;
    private boolean nuevaPersona;
    
    public static final char CASADO = 'C';
    public static final char SOLTERO = 'S';
    public static final char VIUDO = 'V';
    public static final String CARPETA_FOTOS = "Fotos";
  
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    //Asigna la funcionalidad al boton guardar de la vista detalle
    @FXML
    private void onActionButtonGuardar(ActionEvent event) 
    {
        boolean errorFormato = false;
        
        
        
        if(!errorFormato)
        {
            try
            {

                StackPane rootMain = (StackPane)rootAgenda2View.getScene().getRoot();
                rootMain.getChildren().remove(rootAgenda2View);

                rootAgendaView.setVisible(true);

                persona.setNombre(textFieldNombre.getText());
                persona.setApellidos(textFieldApellidos.getText());
                persona.setTelefono(textFieldTelefono.getText());
                persona.setEmail(textFieldEmail.getText());
                
                int numFilaSeleccionada;
                
                //Los datos introducidos son correctos
                if(nuevaPersona)
                {
                    tableViewPrevio.getItems().add(persona);
                    numFilaSeleccionada = tableViewPrevio.getItems().size() - 1;
                    tableViewPrevio.getSelectionModel().select(numFilaSeleccionada);
                    tableViewPrevio.scrollTo(numFilaSeleccionada);
                }
                else 
                {
                    numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
                    tableViewPrevio.getItems().set(numFilaSeleccionada, persona);
                }
                TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
                tableViewPrevio.getFocusModel().focus(pos);
                tableViewPrevio.requestFocus();
                
                
                
                if(!textFieldNumHijos.getText().isEmpty()) 
                {
                    try 
                    {
                        persona.setNumHijos(Short.valueOf(textFieldNumHijos.getText()));
                    }
                    catch(NumberFormatException ex) 
                    {
                        errorFormato = true;
                        Alert alert = new Alert(AlertType.INFORMATION, "Número de hijos no válido");
                        alert.showAndWait();
                        textFieldNumHijos.requestFocus();
                    }
                }
                
                if(!textFieldSalario.getText().isEmpty()) 
                {
                    try 
                    {   
                        persona.setSalario(BigDecimal.valueOf(Double.valueOf(textFieldSalario.getText()).doubleValue()));
                    } catch(NumberFormatException ex) 
                    {
                        errorFormato = true;
                        Alert alert = new Alert(AlertType.INFORMATION, "Salario no válido");
                        alert.showAndWait();
                        textFieldSalario.requestFocus();
                    }
                }
                
                persona.setJubilado(checkBoxJubilado.isSelected());
                
                if(radioButtonCasado.isSelected()) 
                {
                    persona.setEstadoCivil(CASADO);
                }
                else if(radioButtonSoltero.isSelected()) 
                {
                    persona.setEstadoCivil(SOLTERO);
                }
                else if(radioButtonViudo.isSelected()) 
                {
                    persona.setEstadoCivil(VIUDO);
                }
                
                if(datePickerFechaNacimiento.getValue() != null) 
                {
                    LocalDate localDate = datePickerFechaNacimiento.getValue();
                    ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
                    Instant instant = zonedDateTime.toInstant();
                    Date date = Date.from(instant);
                    persona.setFechaNacimiento(date);
                } 
                else 
                {
                    persona.setFechaNacimiento(null);
                }
                
                if(comboBoxProvincia.getValue() != null) 
                {
                    persona.setProvincia(comboBoxProvincia.getValue());
                }
                else
                {
                    Alert alert = new Alert(AlertType.INFORMATION, "Debe indicar una provincia");
                    alert.showAndWait();
                    errorFormato = true;
                }
                
                 // Esta parte debe de ir aquí y no al principio
                if(nuevaPersona)
                {
                    entityManager.persist(persona);
                }
                else
                {
                    entityManager.merge(persona);
                }
                entityManager.getTransaction().commit();
                
            }
            catch (RollbackException ex) 
            { // Los datos introducidos no cumplen los requisitos de la BD
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("No se han podido guardar los cambios. "
                + "Compruebe que los datos cumplen los requisitos");
                alert.setContentText(ex.getLocalizedMessage());
                alert.showAndWait();
            }
        }  
    }
        
    //Asigna la funcionalidad al boton editar de la vista detalle
    @FXML
    private void onActionButtonCancelar(ActionEvent event) 
    {
        StackPane rootMain = (StackPane)rootAgenda2View.getScene().getRoot();
        rootMain.getChildren().remove(rootAgenda2View);
        
        rootAgendaView.setVisible(true);
        
        entityManager.getTransaction().rollback();
        
        int numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
        TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
    }
    
    public void setRootAgendaView(Pane rootAgendaView) 
    {
        this.rootAgendaView = rootAgendaView;
    }
    
    public void setTableViewPrevio(TableView tableViewPrevio) 
    {
        this.tableViewPrevio = tableViewPrevio;
    }
    
    // Averigua si la persona existe ya en la tabla o es un nuevo campo introducido
    public void setPersona(EntityManager entityManager, Persona persona, boolean nuevaPersona)
    {
        this.entityManager = entityManager;
        entityManager.getTransaction().begin();
        if(!nuevaPersona)
        {
            this.persona = entityManager.find(Persona.class, persona.getId());
        }
        else
        {
            this.persona = persona;
        }
        this.nuevaPersona = nuevaPersona;
    }
    
    public void mostrarDatos() 
    {
        textFieldNombre.setText(persona.getNombre());
        textFieldApellidos.setText(persona.getApellidos());
        textFieldTelefono.setText(persona.getTelefono());
        textFieldEmail.setText(persona.getEmail());
        // Falta implementar el código para el resto de controles
        if(persona.getNumHijos() != null) 
        {
            textFieldNumHijos.setText(persona.getNumHijos().toString());
        }
        if(persona.getSalario() != null) 
        {
            textFieldSalario.setText(persona.getSalario().toString());
        }
        
        if (persona.getJubilado() != null) 
        {
            checkBoxJubilado.setSelected(persona.getJubilado());
        }
        
        if (persona.getEstadoCivil() != null) 
        {
            switch (persona.getEstadoCivil()) 
            {
                case CASADO:
                    radioButtonCasado.setSelected(true);
                    break;
                case SOLTERO:
                    radioButtonSoltero.setSelected(true);
                    break;
                case VIUDO:
                    radioButtonViudo.setSelected(true);
                    break;
            }
        }
        
        if (persona.getFechaNacimiento() != null) 
        {
            Date date = persona.getFechaNacimiento();
            Instant instant = date.toInstant();
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate localDate = zdt.toLocalDate();
            datePickerFechaNacimiento.setValue(localDate);
        }
        
        Query queryProvinciaFindAll = entityManager.createNamedQuery("Provincia.findAll");
        List listProvincia = queryProvinciaFindAll.getResultList();
        comboBoxProvincia.setItems(FXCollections.observableList(listProvincia));
        
        if (persona.getProvincia() != null) 
        {
            comboBoxProvincia.setValue(persona.getProvincia());
        }
        
        comboBoxProvincia.setCellFactory((ListView<Provincia> l) -> new ListCell<Provincia>() 
        {
        @Override
        protected void updateItem(Provincia provincia, boolean empty)
        {
            super.updateItem(provincia, empty);
            if (provincia == null || empty) 
            {
                setText("");
            }
            else 
            {
                setText(provincia.getCodigo() + "-" + provincia.getNombre());
            }
        }
        });
        
        comboBoxProvincia.setConverter(new StringConverter<Provincia>() {
        @Override
        public String toString(Provincia provincia) 
        {
            if (provincia == null) 
            {
                return null;
            }
            else
            {
                return provincia.getCodigo() + "-" + provincia.getNombre();
            }
        }
        @Override
        public Provincia fromString(String userId) 
        {
            return null;
        }
    });
        
        if (persona.getFoto() != null) 
        {
            String imageFileName = persona.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if (file.exists()) 
            {
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);  
            } 
            else
            {
                Alert alert = new Alert(AlertType.INFORMATION, "No se encuentra la imagen");
                alert.showAndWait();
            }
        }   
    }
    
    @FXML
    private void onActionButtonExaminar(ActionEvent event) 
    {
        File carpetaFotos = new File(CARPETA_FOTOS);
        if(!carpetaFotos.exists()) 
        {
            carpetaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.jpg", "*.png"),
            new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        File file = fileChooser.showOpenDialog(rootAgenda2View.getScene().getWindow());
        if(file != null) 
        {
            try
            {
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/"+file.getName()).toPath());
                persona.setFoto(file.getName());
                Image image = new Image(file.toURI().toString());
                imageViewFoto.setImage(image);
            }
            catch (FileAlreadyExistsException ex) 
            {
                Alert alert = new Alert(AlertType.WARNING, "Nombre de archivo duplicado");
                alert.showAndWait();
            } 
            catch (IOException ex) 
            {
                Alert alert = new Alert(AlertType.WARNING, "No se ha podido guardar la imagen");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void onActionSuprimirFoto(ActionEvent event) 
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar supresión de imagen");
        alert.setHeaderText("¿Desea SUPRIMIR el archivo asociado a la imagen, \n"
        + "quitar la foto pero MANTENER el archivo, \no CANCELAR la operación?");
        alert.setContentText("Elija la opción deseada:");
        ButtonType buttonTypeEliminar = new ButtonType("Suprimir");
        ButtonType buttonTypeMantener = new ButtonType("Mantener");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeEliminar, buttonTypeMantener, buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeEliminar)
        {
            String imageFileName = persona.getFoto();
            File file = new File(CARPETA_FOTOS + "/" + imageFileName);
            if(file.exists()) 
            {
                file.delete();
            }
            persona.setFoto(null);
            imageViewFoto.setImage(null);
            }
        else if (result.get() == buttonTypeMantener) 
        {
            persona.setFoto(null);
            imageViewFoto.setImage(null);
        }
    }
}
