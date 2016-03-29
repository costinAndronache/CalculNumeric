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
public class KeyboardVectorSource implements VectorSource
{

    @Override
    public int getNumOfElements() 
    {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("\nPlease insert the number of elements: ");
        return sc.nextInt();
    }

    @Override
    public double getElementAtIndex(int index) 
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please insert the element at " + index + ": ");
        return sc.nextDouble();
    }
    
}
