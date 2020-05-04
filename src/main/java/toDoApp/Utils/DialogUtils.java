package toDoApp.Utils;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import toDoApp.Models.Category;
import toDoApp.Models.Task;

import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Karol Wlazło
 * toDoApp
 */
public class DialogUtils {

    private static ResourceBundle bundle = FxmlUtils.getResource();

    public static void categoryExsistInBase() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(FxmlUtils.getResource().getString("error"));
        alert.setHeaderText(FxmlUtils.getResource().getString("category.exist"));
        alert.showAndWait();
    }

    public static void categoryToDeleteNotSelected() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(FxmlUtils.getResource().getString("error"));
        alert.setHeaderText(FxmlUtils.getResource().getString("category.not.selected"));
        alert.showAndWait();
    }

    public static void errorAlert(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(FxmlUtils.getResource().getString("error"));
        alert.setContentText(error);
        alert.showAndWait();
    }

    public static void editTaskDialog(Task task) {
        // adding all categories to list
        ObservableList<Category> categoryList = FXCollections.observableArrayList();
        try {
            Dao<Category, Integer> daoCategory = DaoManager.createDao(DbManager.getConnectionSource(), Category.class);
            List<Category> result = daoCategory.queryForAll();
            result.forEach(e -> {
                categoryList.add(e);
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //

        Dialog dialog = new Dialog();
        dialog.setTitle("Edit Task");
        ButtonType changeButtonType = new ButtonType("Change", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(changeButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleTextField = new TextField();
        ComboBox<Category> categoryComboBox = new ComboBox();
        categoryComboBox.setItems(categoryList);

        grid.add(new Label("Task"), 0, 0);
        grid.add(titleTextField, 1, 0);
        grid.add(new Label("Category"), 0, 1);
        grid.add(categoryComboBox, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(e -> {
            if (e == changeButtonType) {
                if (titleTextField.getLength() == 0 && categoryComboBox.getSelectionModel().getSelectedItem() == null)
                    noChangesInEdit();
                else {
                    try {
                        Dao<Task, Integer> daoTask = DaoManager.createDao(DbManager.getConnectionSource(), Task.class);
                        if (titleTextField.getLength() != 0)
                            task.setTaskDesc(titleTextField.getText());
                        if (categoryComboBox.getSelectionModel().getSelectedItem() != null)
                            task.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());
                        daoTask.update(task);
                    } catch (SQLException error) {
                        System.out.println(FxmlUtils.getResource().getString("error.during.updating.task") + error.getMessage());
                    }
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    public static void noChangesInEdit() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(FxmlUtils.getResource().getString("error"));
        alert.setHeaderText(FxmlUtils.getResource().getString("error.no.changes"));
        alert.showAndWait();
    }
}
