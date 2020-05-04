package toDoApp.Controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import toDoApp.Models.Category;
import toDoApp.Models.TaskWindow;
import toDoApp.Utils.DbManager;
import toDoApp.Utils.FxmlUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Karol Wlazło
 * toDoApp
 */
public class leftPaneController {

    @FXML
    public VBox vBoxCategory;

    ObservableList<Category> categoryList = FXCollections.observableArrayList();
    ObservableList<ToggleButton> listOfButtons = FXCollections.observableArrayList();
    ToggleGroup categoryGroup = new ToggleGroup();
    mainController mainController;

    @FXML
    public void initialize(){
        loadCategoryList();
        loadCategoryButtons();
        printButtonList();

    }

    public void loadCategoryList(){
        categoryList.clear();
        Dao<Category, Integer> daoCategory = null;
        try {
            daoCategory = DaoManager.createDao(DbManager.getConnectionSource(), Category.class);
            List<Category> result = daoCategory.queryForAll();
            result.forEach(e->{
                categoryList.add(e);
            });
        } catch (SQLException error) {
            System.out.println(FxmlUtils.getResource().getString("error.during.loading.cateogry.list") + error.getMessage());
        }
    }
    public void loadCategoryButtons(){
        vBoxCategory.getChildren().clear();
        categoryList.forEach(e->{
            ToggleButton button = new ToggleButton(e.getCategoryName());
            button.setToggleGroup(categoryGroup);
            button.setPrefWidth(163);
            button.setPrefHeight(34);
            vBoxCategory.getChildren().add(button);
            listOfButtons.add(button);
        });
    }

    public void printButtonList(){
        listOfButtons.forEach(e->{
            e.setOnAction(button->{
                TaskWindow taskWindow = new TaskWindow(e.getText());
                mainController.setCenter(taskWindow.getPanel());
            });
        });
    }

    public void setMainController(toDoApp.Controllers.mainController mainController) {
        this.mainController = mainController;
    }
}
