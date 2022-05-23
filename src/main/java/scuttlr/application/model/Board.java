package scuttlr.application.model;

import scuttlr.application.controllers.Writer;

import java.io.Serializable;
import java.util.LinkedHashSet;

import static scuttlr.application.Main.userController;

public class Board implements Serializable
{
    private LinkedHashSet<TaskList> lists;
    private String boardName;
    private String userName;
    private String userPassword;

    public Board(String boardName, String userPassword)
    {
        this.userName = userController.getCurrentUser().getUsername();
        this.boardName = boardName;
        this.userPassword = userPassword;
    }

    public void setBoardName(String boardName)
    {
        this.boardName = boardName;
    }

    public String getBoardName()
    {
        return this.boardName;
    }

    public String getUserName()
    {
        return this.userName;
    }

    public boolean verifyOwner(String userName, String userPassword)
    {
        boolean verified = false;
        if (userName.matches(this.userName) && userPassword.matches(this.userPassword))
        {
            verified = true;
        }
        return verified;
    }
}