package toDoApp.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Karol Wlazło
 * toDoApp
 */
@DatabaseTable(tableName = "category")
public class Category {

    public Category() {
    }
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "CATEGORY_NAME", canBeNull = false)
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return this.categoryName;
    }

    public int getId() {
        return id;
    }
}
