/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchStructuresComparison;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Hooman
 */
public class Coordinator
{
    private int[] generateArray(int n,int min,int max)
    {
        int [] array=new int[n];
        for ( int i=0;i<n;i++)
        {
            array[i]=(int)(Math.random()*(double)(max-min)+min);
        }
        return array;
    }
    private long getTime()
    {
        return System.nanoTime();
    }
    public void experiment(int n,int maxRep) throws IOException
    {
       BufferedWriter writer=new BufferedWriter(new FileWriter("output.txt"));
       for (int num=100;num<=n;num+=50) // all  sizes
       {
           for (int rep=0;rep<maxRep;rep++)
           {
               System.out.println("Testing n= "+num);
               JavaHashMap javaHashMap=new JavaHashMap();
               JavaList javaList=new JavaList();
               DynamicArray dynamicArray=new DynamicArray();
               DynamicSortedArray dynamicSortedArray=new DynamicSortedArray();
               MyLinkedList myLinkedList=new MyLinkedList();
               
               ArrayList<SearchStructure> structs=new ArrayList<SearchStructure>();
               structs.add(javaHashMap);
               structs.add(javaList);
               structs.add(dynamicArray);
               structs.add(dynamicSortedArray);
               structs.add(myLinkedList);
               
               writer.write(num+",");
               int[] keys=generateArray(num, 1, 2000000000); //key 1 to max
               int [] vals=generateArray(num, 1000, 300000000); //value 1000 to 3000000 
               
               for ( SearchStructure struct:structs)
               {
                   long begin=getTime();
                   for (int i=0;i<num;i++)
                   {
                        if(!struct.insert(keys[i], vals[i]))
                            System.out.println(struct.toString()+ " : error in insert");
                       
                   } 
                   long finish=getTime();
                   writer.write(/*struct.toString()+","+*/(finish-begin)/num+",");
                   //struct.print();
                   begin=getTime();
                   for (int i=0;i<num;i++)
                   {
                       int temp=struct.search(keys[i]);
                       if (temp!=vals[i]) // test & validation
                           System.out.println(struct.toString()+" : error in search. search result="+ temp+" but searched for key: "+keys[i]+" expected val: "+vals[i]);   
                       //else
                            //System.out.println("good");
                   } 
                   finish=getTime();
                   writer.write(/*struct.toString()+","+*/(finish-begin)/num+",");
                   begin=getTime();
                   for (int i=0;i<num;i++)
                   {
                        if(!struct.delete(keys[i]))
                            System.out.println(struct.toString()+ " : error in delete");
                   } 
                   finish=getTime();
                   writer.write(/*struct.toString()+","+*/(finish-begin)/num+",");
                   //System.out.println(struct.toString()+" "+(finish-begin)/num);
               }
               writer.write("\n");
           }
       }
       writer.close();
    }    
}
