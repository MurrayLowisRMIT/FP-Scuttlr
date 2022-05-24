package scuttlr.application.controllers;

import scuttlr.application.model.Board;

import java.io.*;

import static scuttlr.application.Main.boardController;
import static scuttlr.application.Main.userController;

public class Writer
{
    // save a user as a serialized file including their username, password, avatar, and list of names of boards they own
    public void saveUser()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/scuttlr/application/accounts/" + userController.getCurrentUser().getUsername() + "_data.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(userController.getCurrentUser());
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

    // save a board as a serialized file including contents, and owner's username and password (for verification)
    public void saveBoard(Board board)
    {
        if (board == null)
        {
            userController.getCurrentUser().createBoard("New board");
            board = boardController.getCurrentBoard();
        }

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
