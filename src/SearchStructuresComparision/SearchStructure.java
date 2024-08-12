/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchStructuresComparision;

/**
 *
 * @author Hooman
 */
public abstract class SearchStructure
{
    abstract public boolean insert(Integer key,Integer data);
    abstract public boolean delete(Integer key);// return success
    abstract public Integer search(Integer key);// return data
    abstract public void print();
} 
