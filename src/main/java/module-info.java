/**
 * @author Karol Wlazło
 * toDoApp
 */module toDoApp {
     requires javafx.fxml;
     requires javafx.controls;
     requires ormlitebuild;
     requires log4j;
     requires java.sql;
    requires itextpdf;

    opens toDoApp;
     opens toDoApp.Controllers;
     opens toDoApp.Models;
}