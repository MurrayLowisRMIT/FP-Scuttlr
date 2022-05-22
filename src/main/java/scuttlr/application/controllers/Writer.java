package scuttlr.application.controllers;

import scuttlr.application.model.Board;

import java.io.*;

import static scuttlr.application.Main.userController;

public class Writer
{
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

    public void saveBoard(Board board)
    {
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
