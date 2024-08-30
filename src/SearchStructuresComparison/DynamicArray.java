/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchStructuresComparison;

/**
 *
 * @author Sina
 */
public class DynamicArray extends SearchStructure
{
    int length = 3;
    int current = -1;
    Item[] items = new Item[length];
    
    public boolean insert(Integer key,Integer data)
    {
        if (findIndex(key) != -1) return false;
        if (current >= length - 1) reSize();
        items[++current] = new Item(key,data);
        return true;
    }
    private void reSize()
    {
        length *= 2;
        Item[] newItems = new Item[length];
        System.arraycopy(items, 0, newItems, 0, items.length);
        items = newItems;
    }
    public boolean delete(Integer key)
    {
        int index = findIndex(key);
        if (index == -1) return false;
        for (int i = index; i < current; i++) {
            items[i] = items[i + 1];
        }
        current--;
        return true;
    }
    public Integer search(Integer key)
    {
        int index = findIndex(key);
        if (index == -1) return null;
        return items[index].data;
    }
    private int findIndex(Integer key)
    {
        for (int i = 0; i < current + 1; i++) {
            if (items[i].key.equals(key)) return i;
        }
        return -1;
    }
    public void print()
    {
        for (int i=0;i<=current;i++)
        {
            System.out.print(items[i]);
        } 
        System.out.println();
    }
} 
