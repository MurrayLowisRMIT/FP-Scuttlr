package scuttlr.application.model;

import java.io.Serializable;
import java.util.LinkedList;

import static scuttlr.application.Main.userController;

public class Board implements Serializable
{
    private LinkedList<Column> columns;
    private String boardName;
    // board is only accessible to user with matching username and password
    private String userName;
    private String userPassword;

    public Board(String boardName, String userPassword)
    {
        this.userName = userController.getCurrentUser().getUsername();
        this.boardName = boardName;
        this.userPassword = userPassword;
        this.columns = new LinkedList<>();
    }

    public String getBoardName()
    {
        return this.boardName;
    }

    // get name of board's owner - used for verification
    public String getUserName()
    {
        return this.userName;
    }

    public LinkedList<Column> getColumns()
    {
        return this.columns;
    }

    public void addColumn()
    {
        Column column = new Column(this.columns.size());
        this.columns.add(column);
    }

    public void setColumns(LinkedList<Column> columns)
    {
        // empty existing list
        this.columns.clear();
        // set new list
        this.columns.addAll(columns);
    }

    // delete column and update columnIDs
    public void removeColumn(int columnID)
    {
        this.columns.remove(columnID);
        for (Column column : this.columns)
        {
            column.setColumnID(columnID);
        }
    }

    // reorders columns in linkedList when columns are moved in the active board
    public void reorderColumns()
    {
        for (int i = 0; i < this.columns.size(); i++)
        {
            this.columns.get(i).setColumnID(i);
        }
    }
}