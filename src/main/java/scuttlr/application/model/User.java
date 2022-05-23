package scuttlr.application.model;

import java.io.Serializable;
import java.util.LinkedList;

public class User implements Serializable
{
    private String username;
    private String password;
    private byte[] avatarData;
    private LinkedList<String> userBoardNames;
    private String currentBoard;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.userBoardNames = new LinkedList<String>();
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean checkPassword(String password)
    {
        boolean confirmPassword = false;
        if (password.matches(this.password))
        {
            confirmPassword = true;
        }
        return confirmPassword;
    }

    public String getUsername()
    {
        return this.username;
    }

    public byte[] getAvatar()
    {
        return this.avatarData;
    }

    public void setAvatar(byte[] avatarData)
    {
        this.avatarData = avatarData;
    }

    public LinkedList<String> getUserBoardNames()
    {
        return this.userBoardNames;
    }

    // creating board here ensures password is added securely
    // kinda pointless since the serialized object is still partially human-readable anyway
    public Board createBoard(String boardName)
    {
        Board board = new Board(boardName, this.password);
        saveBoard(boardName);
        return board;
    }

    public void saveBoard(String boardName)
    {
        this.userBoardNames.add(boardName);
    }
}
