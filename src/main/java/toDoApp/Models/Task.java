package toDoApp.Models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * @author Karol Wlazło
 * toDoApp
 */
@DatabaseTable(tableName = "task")
public class Task {
    public Task() {
    }
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "TASK_DESC", canBeNull = false)
    private String taskDesc;
    @DatabaseField(columnName = "CATEGORY_ID", foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, canBeNull = false)
    private Category category;
    @DatabaseField(columnName = "ADDED_DATE")
    private Date addedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
}
