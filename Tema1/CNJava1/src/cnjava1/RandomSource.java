/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cnjava1;

import java.util.*;

/**
 *
 * @author Costi
 */
public class RandomSource implements MatrixSource, VectorSource
{
    private Random rand;
    private int minValue, maxValue;
    private int maxElems;
    public RandomSource(int maxElems, int minValue, int maxValue)
    {
        this.maxElems = maxElems;
        this.minValue = minValue;
        this.maxValue = maxValue;
        rand = new Random();
    }

    @Override
    public int getNumOfLines() 
    {
        return this.maxElems;
    }

    @Override
    public int getNumOfColumns() 
    {
        return this.maxElems;
    }

    @Override
    public double getElementAtIndex(int index) 
    {
        return nextRand();
    }

    @Override
    public int getNumOfElements() 
    {
        return this.maxElems;
    }
    
    private double nextRand()
    {
        return rand.nextDouble() * (maxValue - minValue) + minValue;
    }

    @Override
    public double getElementAtLineAndColumn(int line, int column) 
    {
        return this.nextRand();
    }
    
    
}
