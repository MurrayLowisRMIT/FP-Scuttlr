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
        this.columns.add(new Column());
    }

    public void setColumns(LinkedList<Column> columns)
    {
        this.columns = columns;
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
}