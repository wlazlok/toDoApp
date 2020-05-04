package toDoApp.Models;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import toDoApp.Utils.DbManager;
import toDoApp.Utils.DialogUtils;
import toDoApp.Utils.FxmlUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Karol Wlazło
 * toDoApp
 */

public class TaskWindow {
    private String categoryName;
    private ResourceBundle bundle = FxmlUtils.getResource();

    Pane panel = new Pane();
    Label categorylabel;
    TableView tableView = new TableView();
    Button deleteButton = new Button(bundle.getString("delete.button"));
    Button editButton = new Button(bundle.getString("edit.button"));
    TableColumn description = new TableColumn(bundle.getString("column.descrpiption"));
    TableColumn timeAdded = new TableColumn(bundle.getString("column.time.added"));
    Label taskLabel = new Label(bundle.getString("label"));
    TextField newTaskField = new TextField();
    Button addTask = new Button(bundle.getString("add.button"));

    ObservableList<Task> tasks = FXCollections.observableArrayList();

    public TaskWindow(String categoryName) {
        this.categoryName = categoryName;
        loadComponents();
        creatTable();
        initAction();
        initTaskList();
        description.setCellValueFactory(new PropertyValueFactory<Task, Integer>("taskDesc"));
        timeAdded.setCellValueFactory(new PropertyValueFactory<Task, Integer>("addedDate"));
        tableView.setItems(tasks);
        addTask.disableProperty().bind(newTaskField.textProperty().isEmpty());
    }
    public void loadComponents(){
        categorylabel = new Label(categoryName);
        categorylabel.setLayoutX(300);
        categorylabel.setLayoutY(62);
        categorylabel.setFont(Font.font(20));

        tableView.setPrefHeight(269);
        tableView.setPrefWidth(546);
        tableView.setLayoutX(40);
        tableView.setLayoutY(123);
        description.setPrefWidth(350);
        timeAdded.setPrefWidth(196);
        tableView.getColumns().clear();
        tableView.getColumns().addAll(description, timeAdded);

        deleteButton.setLayoutX(514);
        deleteButton.setLayoutY(429);
        deleteButton.setPrefHeight(25);
        deleteButton.setPrefWidth(72);

        editButton.setLayoutX(419);
        editButton.setLayoutY(429);
        editButton.setPrefHeight(25);
        editButton.setPrefWidth(72);

        taskLabel.setLayoutY(435);
        taskLabel.setLayoutX(40);
        taskLabel.setPrefHeight(17);
        taskLabel.setPrefWidth(7782);

        newTaskField.setLayoutX(126);
        newTaskField.setLayoutY(431);

        addTask.setLayoutX(287);
        addTask.setLayoutY(431);
        addTask.setPrefHeight(25);
        addTask.setPrefWidth(72);

        panel.getChildren().addAll(categorylabel, tableView, deleteButton, editButton, taskLabel, newTaskField, addTask);
    }

    public Pane getPanel() {
        return panel;
    }

    public void initTaskList(){
        tasks.clear();
        try {
            Dao<Category, Integer> categoryDao = DaoManager.createDao(DbManager.getConnectionSource(), Category.class);
            List<Category> categoryList = categoryDao.query(categoryDao.queryBuilder().where().eq("CATEGORY_NAME", categoryName).prepare());
            Dao<Task, Integer> dao = DaoManager.createDao(DbManager.getConnectionSource(), Task.class);
            List<Task> result = dao.query(dao.queryBuilder().where().eq("CATEGORY_ID", categoryList.get(0)).prepare());
            result.forEach(e->{
                tasks.add(e);
            });
        } catch (SQLException error) {
            DialogUtils.errorAlert(error.getMessage());
        }
    }

    public void initAction(){
        addTask.setOnAction(e->{
            try {
                Dao<Task, Integer> daoBook = DaoManager.createDao(DbManager.getConnectionSource(), Task.class);
                Task task = new Task();
                task.setTaskDesc(newTaskField.getText());
                Dao<Category, Integer> categoryDao = DaoManager.createDao(DbManager.getConnectionSource(), Category.class);
                List<Category> categoryList = categoryDao.query(categoryDao.queryBuilder().where().eq("CATEGORY_NAME", categoryName).prepare());
                task.setCategory(categoryList.get(0));
                task.setAddedDate(new Date());
                daoBook.create(task);
                newTaskField.clear();
                initTaskList();
            } catch (SQLException error) {
                DialogUtils.errorAlert(error.getMessage());
            }
        });

        deleteButton.setOnAction(e->{
            Task result = (Task) tableView.getSelectionModel().getSelectedItem();
            try {
                Dao<Task, Integer> daoBook = DaoManager.createDao(DbManager.getConnectionSource(), Task.class);
                daoBook.delete(result);
            } catch (SQLException error) {
                DialogUtils.errorAlert(error.getMessage());
            }
            initTaskList();
        });

        editButton.setOnAction(e->{
            if( tableView.getSelectionModel().getSelectedItem() == null)
                DialogUtils.errorAlert("Task not selected");
            else {
                Task result = (Task) tableView.getSelectionModel().getSelectedItem();
                DialogUtils.editTaskDialog(result);
                initTaskList();
            }
        });
    }
    public void creatTable(){
        DbManager.createConnectionSource();
        try {
            //TableUtils.dropTable(DbManager.getConnectionSource(), Task.class, true);
            TableUtils.createTableIfNotExists(DbManager.getConnectionSource(), Task.class);
        } catch (SQLException error) {
            DialogUtils.errorAlert(error.getMessage());
        }
        DbManager.closeConnectionSource();
    }
}
