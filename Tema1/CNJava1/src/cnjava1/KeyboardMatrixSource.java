/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnjava1;

import java.util.Scanner;

/**
 *
 * @author Costi
 */
public class KeyboardMatrixSource implements  MatrixSource 
{

    @Override
    public int getNumOfLines() 
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Num of lines: ");
        return sc.nextInt();
        
    }

    @Override
    public int getNumOfColumns() 
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Num of columns: ");
        return sc.nextInt();
    }

    @Override
    public double getElementAtLineAndColumn(int line, int column) 
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Element at [" + line + "][" + column + "]: ");
        return sc.nextDouble();
    }
    
}
