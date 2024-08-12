/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchStructuresComparision;

import java.util.HashMap;

/**
 *
 * @author Hooman
 */
class JavaHashMap extends SearchStructure
{
    HashMap<Integer,Integer> items=new HashMap<Integer,Integer>();
    public boolean insert(Integer key,Integer data)
    {
        if (items.get(key)==null)
        {
            items.put(key, data);
            return true;
        }
        else
            return false;
    }
    public boolean delete(Integer key)
    {
		if(items.remove(key)!=null)
			return true;
		else
			return false;   
    }
    public Integer search(Integer key)
    {
        return items.get(key);
    }
    public void print()
    {
        System.out.println(items);
    }
}