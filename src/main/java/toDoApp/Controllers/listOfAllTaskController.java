package toDoApp.Controllers;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import toDoApp.Models.Task;
import toDoApp.Utils.DbManager;
import toDoApp.Utils.FxmlUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * @author Karol Wlazło
 * toDoApp
 */
public class listOfAllTaskController extends Window {
    @FXML
    public Pane test;
    @FXML
    public TableColumn taskColumn;
    @FXML
    public TableColumn categoryColumn;
    @FXML
    public TableColumn dateColumn;
    @FXML
    public TableView taskTableView;

    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    static ResourceBundle bundle = FxmlUtils.getResource();

    @FXML
    public void initialize(){
        loadList();
        taskColumn.setCellValueFactory(new PropertyValueFactory<Task, Integer>("taskDesc"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<Task, Integer>("category"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Task, Integer>("addedDate"));
        taskTableView.setItems(taskList);
    }

    public void loadList(){
        taskList.clear();
        try {
            Dao<Task, Integer> daoTask = DaoManager.createDao(DbManager.getConnectionSource(), Task.class);
            List<Task> result = daoTask.queryForAll();
            result.forEach(e->{
                taskList.add(e);
            });
        } catch (SQLException error) {
            System.out.println(FxmlUtils.getResource().getString("error.during.loading.list") + error.getMessage());
        }
    }
    @FXML
    public void exportToPDF() throws FileNotFoundException, DocumentException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(this);
        if(file != null)
            saveFile(file);
    }

    public void saveFile(File file) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        PdfPTable table = new PdfPTable(3);
        addTableHeader(table);
        addRows(table, taskList);

        document.add(table);
        document.close();
    }
    private static void addRows(PdfPTable table, ObservableList<Task> taskList) {
        taskList.forEach(e->{
            table.addCell(e.getTaskDesc());
            table.addCell(e.getCategory().getCategoryName());
            table.addCell(String.valueOf(e.getAddedDate()));
        });
    }

    private static void addTableHeader(PdfPTable table) {
        Stream.of("Task", "Category", "Added date")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }
}
