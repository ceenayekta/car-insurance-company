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
public class Item 
{
    public Integer key;
    public Integer data;
    public Item()
    {
        //key=0;
        //data=0;
    }
    public Item(Integer key,Integer data)
    {
        this.key=key;
        this.data=data;
    }
    public String toString()
    {
        return key+" -> "+data+" , ";
    }
}
