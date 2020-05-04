package toDoApp.Controllers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import toDoApp.Models.Category;
import toDoApp.Models.Task;
import toDoApp.Utils.DbManager;
import toDoApp.Utils.DialogUtils;
import toDoApp.Utils.FxmlUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Karol Wlazło
 * toDoApp
 */

public class mainController {

    @FXML
    public Button addCategoryButton;
    @FXML
    public Button deleteCategoryButton;
    @FXML
    public TextField categoryTextField;
    @FXML
    private leftPaneController leftPaneController;
    @FXML
    public BorderPane mainBorderPane;

    ResourceBundle bundle = FxmlUtils.getResource();

    @FXML
    public void initialize(){
        leftPaneController.setMainController(this);
        setStartCenter();
        addCategoryButton.disableProperty().bind(categoryTextField.textProperty().isEmpty());
        deleteCategoryButton.disableProperty().bind(leftPaneController.categoryGroup.selectedToggleProperty().isNull());
    }
    @FXML
    public void addCategory() throws SQLException {
        Dao<Category, Integer> daoCategory = DaoManager.createDao(DbManager.getConnectionSource(), Category.class);
        String tmpCategory = categoryTextField.getText();
        List<Category> result = daoCategory.query(daoCategory.queryBuilder().where().eq("CATEGORY_NAME", tmpCategory).prepare());
        if(!result.isEmpty() || categoryTextField.getLength() == 0){
            DialogUtils.categoryExsistInBase();
            categoryTextField.clear();
        }
        else {
            DbManager.createConnectionSource();
            //TableUtils.dropTable(DbManager.getConnectionSource(), Category.class, true);
            TableUtils.createTableIfNotExists(DbManager.getConnectionSource(), Category.class);
            Category category = new Category();
            category.setCategoryName(categoryTextField.getText());
            daoCategory.create(category);
            DbManager.closeConnectionSource();
            leftPaneController.loadCategoryList();
            leftPaneController.loadCategoryButtons();
            leftPaneController.printButtonList();
            categoryTextField.clear();
        }
    }
    @FXML
    public void deleteCategory(){
        ToggleButton result = (ToggleButton)leftPaneController.categoryGroup.getSelectedToggle();
        if(result == null) {
            DialogUtils.categoryToDeleteNotSelected();
            categoryTextField.clear();
        }
        else{
            Dao<Category, Integer> daoCategory = null;
            Dao<Task, Integer> daoTask = null;
            try {
                daoCategory = DaoManager.createDao(DbManager.getConnectionSource(), Category.class);
                daoTask = DaoManager.createDao(DbManager.getConnectionSource(), Task.class);

                List<Category> categoryList = daoCategory.query(daoCategory.queryBuilder().where().eq("CATEGORY_NAME", result.getText()).prepare());
                Category category = categoryList.get(0);

                List<Task> tasksList = daoTask.query(daoTask.queryBuilder().where().eq("CATEGORY_ID", category.getId()).prepare());
                daoTask.delete(tasksList);

                daoCategory.delete(categoryList);
            } catch (SQLException error) {
                DialogUtils.errorAlert(error.getMessage());
            }
            setStartCenter();
            leftPaneController.loadCategoryList();
            leftPaneController.loadCategoryButtons();
            leftPaneController.printButtonList();
            categoryTextField.clear();
        }
    }
    public void setCenter(Pane panel){
        mainBorderPane.setCenter(panel);
    }

    public void setStartCenter(){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/listOfAllTasks.fxml"));
        loader.setResources(FxmlUtils.getResource());
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException error) {
            DialogUtils.errorAlert(error.getMessage());
        }
    }
    @FXML
    public void showAllTasks() {
        if(leftPaneController.categoryGroup.getSelectedToggle()!= null)
            leftPaneController.categoryGroup.getSelectedToggle().setSelected(false);
        setStartCenter();
    }
}
