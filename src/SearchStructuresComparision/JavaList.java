/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchStructuresComparision;

import java.util.ArrayList;

/**
 *
 * @author Hooman
 */
public class JavaList extends SearchStructure
{
    ArrayList<Item> items=new ArrayList<Item>();
    
    public boolean insert(Integer key,Integer data)
    {
        if (search(key)==null)
        {
            items.add(new Item(key,data));
            return true;
        }
        else
            return false;
    }
    public boolean delete(Integer key)
    {
       int index=findIndex(key);
       if (index!=-1)
       {
           items.remove(index);
           return true;
       }
       else
           return false;  
    }
    public Integer search(Integer key)
    {
       int index=findIndex(key); 
       if (index!=-1)
       {
           return items.get(index).data;
       }
       else
           return null;
    }
    private int findIndex(Integer key)
    {
        for(int i=0;i<items.size();i++)
        {
            if(items.get(i).key.equals(key))
                return i;
        }
        return -1;// not found
    }
    public void print()
    {
        for (int i=0;i<items.size();i++)
        {
            System.out.print(items.get(i));
        } 
        System.out.println();
    }
} 
