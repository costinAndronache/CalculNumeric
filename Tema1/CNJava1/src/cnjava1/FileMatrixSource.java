/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnjava1;
import java.util.*;
import java.io.*;

/**
 *
 * @author Costi
 */
public class FileMatrixSource implements MatrixSource 
{
    private final Scanner scanner;
    
    public FileMatrixSource(File file) throws Exception
    {
        scanner = new Scanner(new FileInputStream(file));
        
    }

    @Override
    public int getNumOfLines() 
    {
        return scanner.nextInt();
    }

    @Override
    public int getNumOfColumns() 
    {
        return scanner.nextInt();
    }

    @Override
    public double getElementAtLineAndColumn(int line, int column) 
    {
        return scanner.nextDouble();
    }
}
