
package SearchStructuresComparison;

/**
 *
 * @author Sina
 */

public class DynamicSortedArray extends SearchStructure
{
    int length=3;
    int current=-1;
    Item [] items=new Item[length];
    
    public boolean insert(Integer key,Integer data)
    {
        if (current >= length - 1) reSize();

        int index = -1;
        if (current == -1 || items[0].key > key) {
            index = 0;
        } else if (items[current].key < key) {
            index = current + 1;
        }

        int lower = 0;
        int upper = current;
        while (index == -1) {
            int pointer = (upper - lower) / 2 + lower;
            if (items[pointer].key.equals(key)) return false;
            if (items[pointer].key > key) {
                if (upper - lower <= 2) {
                    if (items[lower].key.equals(key)) return false;
                    index = items[lower].key > key ? lower : pointer;
                    break;
                }
                upper = pointer;
            } else {
                if (upper - lower <= 2) {
                    if (items[upper].key.equals(key)) return false;
                    index = items[upper].key > key ? upper : current + 1;
                    break;
                }
                lower = pointer;
            }
        }

        for (int i = current; i >= index; i--) {
            items[i + 1] = items[i];
        }
        items[index] = new Item(key, data);
        current++;
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
        if (current < 1) return current;
        
        int lower = 0;
        int upper = current;
        while (true) {
            int pointer = (upper - lower) / 2 + lower;
            if (items[pointer].key.equals(key)) return pointer;
            if (items[pointer].key > key) {
                if (upper - lower <= 2) return items[lower].key.equals(key) ? lower : -1;
                upper = pointer;
            } else {
                if (upper - lower <= 2) return items[upper].key.equals(key) ? upper : -1;
                lower = pointer;
            }
        }
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