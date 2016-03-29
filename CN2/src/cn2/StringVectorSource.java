/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn2;

import cnjava1.VectorSource;

/**
 *
 * @author Costi
 */
public class StringVectorSource implements VectorSource 
{
    private final String[] elementsAsStrings;
    
    public StringVectorSource(String vectorString)
    {
        elementsAsStrings = vectorString.split("\\s*,\\s*");
    }
    
    @Override
    public int getNumOfElements() 
    {
        return elementsAsStrings.length;
    }

    @Override
    public double getElementAtIndex(int i) 
    {
        String elem = this.elementsAsStrings[i];
        return Double.parseDouble(elem);
    }
    
}
