/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnjava1;

import java.util.Scanner;
import java.io.*;

/**
 *
 * @author Costi
 */
public class FileVectorSource implements  VectorSource 
{
    private Scanner scanner;
    
    public FileVectorSource(File file) throws Exception
    {
        scanner = new Scanner(new FileInputStream(file));      
    }

    @Override
    public int getNumOfElements() 
    {
        return scanner.nextInt();
    }

    @Override
    public double getElementAtIndex(int index) 
    {
        return scanner.nextDouble();
    }
}
