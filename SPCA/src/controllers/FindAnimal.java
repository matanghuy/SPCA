package controllers;

import beans.Animal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;
import spca.datalayer.DataContext;
import spca.datalayer.DataResult;
import spca.datalayer.DataRow;
import spca.datalayer.SpcaDataLayerFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by matanghuy on 7/3/14.
 */
public class FindAnimal implements Initializable{

    @FXML private Button btnSearch;
    @FXML private TextField findName;
    @FXML private TableView<Animal> tableAnimals;
    @FXML private TableColumn<Animal, String> colName;
    @FXML private TableColumn<Animal, String> colChipNumber;
    @FXML private TableColumn<Animal, String> colType;
    @FXML private TableColumn<Animal, String> colSourceCity;

    private ObservableList<Animal> animalsList;
    private DataContext dataContext;

    private static final Logger logger = Logger.getLogger(FindAnimal.class);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            dataContext = SpcaDataLayerFactory.getDataContext();
        } catch (IOException e) {
            logger.error("Can not get data context", e);
        }
        initTable();
    }

    private void initTable() {
        animalsList = FXCollections.observableArrayList();
        colName.setCellValueFactory(new PropertyValueFactory<Animal, String>("name"));
        colChipNumber.setCellValueFactory(new PropertyValueFactory<Animal, String>("chipNumber"));
        colType.setCellValueFactory(new PropertyValueFactory<Animal, String>("type"));
        colSourceCity.setCellValueFactory(new PropertyValueFactory<Animal, String>("sourceCity"));
        tableAnimals.setItems(animalsList);

    }

    @FXML
    public void search() {
        try {
            animalsList.clear();
            DataResult result = dataContext.getAnimals(null, findName.getText(), null, null, null);
            for(DataRow row : result.getRows()) {
                animalsList.add(buildAnimal(row));
            }
        } catch (SQLException e) {
            logger.error("Couldn't get Animals list from database, e");
        }
    }

    private Animal buildAnimal(DataRow row) {
        Animal animal = new Animal();
        animal.setChipNumber((String) row.getObject("ChipNum"));
        animal.setId((Integer) row.getObject("Id"));
        animal.setName((String) row.getObject("Name"));
        animal.setSourceCity((String) row.getObject("CityName"));
        animal.setType((String) row.getObject("AnimalTypeName"));
        return animal;
    }

    @FXML
    public void accept(ActionEvent event) {
        CommonUtils.animal = tableAnimals.getSelectionModel().getSelectedItem();
        ((Node)event.getSource()).getScene().getWindow().hide();

    }

}
