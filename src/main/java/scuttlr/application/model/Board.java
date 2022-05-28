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
        for (int i = 0; i < columns.size(); i++)
        {
            this.columns.add(columns.get(i));
        }
    }

    // TODO use this
    public boolean verifyOwner(String userName, String userPassword)
    {
        boolean verified = false;
        if (userName.matches(this.userName) && userPassword.matches(this.userPassword))
        {
            verified = true;
        }
        return verified;
    }

    // new boards have no password until saved
    public boolean checkIfNew()
    {
        return this.userPassword == null;
    }

    // swap the place of two columns
    public void swapColumns(Column c1, Column c2)
    {
        for (Column column : this.columns)
        {
            if (column.getColumnID() == c1.getColumnID())
            {
                column.setColumnID(c2.getColumnID());
            }
            else if (column.getColumnID() == c2.getColumnID())
            {
                column.setColumnID(c1.getColumnID());
            }
        }
        reorderColumns();
    }

    // reorders columns in linkedList when columns are moved in the active board
    public void reorderColumns()
    {
        LinkedList<Column> newList = new LinkedList<>();
        for (int i = 0; i < this.columns.size(); i++)
        {
            if (this.columns.get(i).getColumnID() == i)
            {
                newList.add(this.columns.get(i));
            }
        }
        this.columns = new LinkedList<>(newList);
    }
}