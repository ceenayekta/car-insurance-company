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
public class MyLinkedList extends SearchStructure
{
    class Node
    {
        Item item;
        Node next;
		//Node previous;// double direction	
		public Node(Integer key,Integer data)
        {
            this.item=new Item(key,data);
            next = null;
            //previous = null;
		}
    }
    public Node head=null;

    public boolean insert(Integer key,Integer data)
    {
        Node node=new Node(key,data);
        node.next=null;
        if ( head==null)
            head=node;
        else
        {
            Node last=head;
            while(last.next!=null)
            {
                if ( last.item.key.equals(key))
                    return false;// unique key instead of search
                last=last.next;
            }
            if(last.item.key.equals(key))
                return false;
            last.next=node;  
        }
        return true;
    }
    public boolean delete(Integer key)
    {
        if ( head==null)
            return false;
        if (head.item.key.equals(key))
        {
            head=head.next;
            return true;
        }
        Node prev=head;
        Node pointer=head.next;
        while( pointer!= null)
        {
            if (pointer.item.key.equals(key))
            {
               prev.next=pointer.next;
               return true;
            }
            prev=pointer;
            pointer=pointer.next;
        }
        return false;
    }
    public Integer search(Integer key)
    {
        Node pointer=head;
        while( pointer!= null)
        {
            if (pointer.item.key.equals(key))
            {   
               return pointer.item.data;
            }
            pointer=pointer.next;
        }
        return null;
    }
    public void print()
    {
        Node pointer=head;
        while( pointer!= null)
        {
            System.out.print(pointer.item);
            pointer=pointer.next;
        }
        System.out.println();
    }
} 
