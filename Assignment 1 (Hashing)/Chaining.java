import java.io.*;
import java.util.*;

public class Chaining {
    
     public int m; // number of SLOTS 
     public int A; // the default random number
     int w;
     int r;
     public ArrayList<ArrayList<Integer>>  Table;

    // if A==-1, then a random A is generated. else, input A is used.
    protected Chaining(int w, int seed, int A){
         this.w = w;
         this.r = (int) (w-1)/2 +1;
         this.m = power2(r);
         this.Table = new ArrayList<ArrayList<Integer>>(m);
         for (int i=0; i<m; i++) {
             Table.add(new ArrayList<Integer>());
         }
         if (A==-1){
         this.A = generateRandom((int) power2(w-1), (int) power2(w),seed);
        }
        else{
            this.A = A;
        }
     }
    
    /** Calculate 2^w*/
     public static int power2(int w) {
         return (int) Math.pow(2, w);
     }
     
     //generate a random number in a range (for A)
     public static int generateRandom(int min, int max, int seed) {     
         Random generator = new Random(); 
                 if(seed>=0){
                    generator.setSeed(seed);
                 }
         int i = generator.nextInt(max-min-1);
         return i+min+1;     
    }

    /**Implements the hash function h(k)*/
    //Using the formula given in the assignment hand out to compute the hash value.
    public int chain (int key) {
    	return ((A*key)%(power2(w))) >> (w-r);
    }

    /**Inserts key k into hash table. Returns the number of collisions encountered*/
    /* Computing hash value and inserting in the corresponding slot of array. */
    public int insertKey(int key){
    	int hash = chain(key);
    	int size = 0;
    	ArrayList<Integer> a_inner = Table.get(hash);
        if (a_inner.isEmpty()) {
            a_inner.add(key);
        }
        else 
        {	
        	size = a_inner.size();
        	a_inner.add(0,key);
        }
        return size;
    }
       
    /**Sequentially inserts a list of keys into the HashTable. Outputs total number of collisions */
    public int insertKeyArray (int[] keyArray){
        int collision = 0;
        for (int key: keyArray) {
            collision += insertKey(key);
        }
        return collision;
    }
}
