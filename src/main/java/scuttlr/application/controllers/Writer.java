package scuttlr.application.controllers;

import scuttlr.application.model.Board;

import java.io.*;
import java.util.LinkedList;

import static scuttlr.application.Main.userController;

public class Writer
{
    // TODO should probably hash this or something so data cannot be read from serialized file?
    // save a user as a serialized file including their username, password, avatar, and list of names of boards they own
    public void saveUser()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/scuttlr/application/accounts/" + userController.getCurrentUser().getUsername() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(userController.getCurrentUser());
            out.close();
            fileOut.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    // TODO should probably hash this or something so data cannot be read from serialized file?
    // save a board as a serialized file including contents, and owner's username and password (for verification)
    public void saveBoard(Board board)
    {
        // TODO do not create a new board every time
        board = userController.getCurrentUser().createBoard(board);

        try
        {
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/scuttlr/application/boards/" + board.getUserName() + "_" + board.getBoardName() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(board);
            out.close();
            fileOut.close();
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
