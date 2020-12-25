import java.io.*;
import java.util.*;

public class Open_Addressing {
     public int m; // number of SLOTS AVAILABLE
     public int A; // the default random number
     int w;
     int r;
     public int[] Table;

     protected Open_Addressing(int w, int seed, int A) {
         this.w = w;
         this.r = (int) (w-1)/2 +1;
         this.m = power2(r);
         if (A==-1){
            this.A = generateRandom((int) power2(w-1), (int) power2(w),seed);
         }
        else{
            this.A = A;
        }
         this.Table = new int[m];
         for (int i =0; i<m; i++) {
             Table[i] = -1;
         }
     }
     
     /** Calculate 2^w*/
     public static int power2(int w) {
         return (int) Math.pow(2, w);
     }
     
     public static int generateRandom(int min, int max, int seed) {     
         Random generator = new Random(); 
                 if(seed>=0){
                    generator.setSeed(seed);
                 }
         int i = generator.nextInt(max-min-1);
         return i+min+1;
     }
     
        /**Implements the hash function g(k)*/
     	//Using the formula given in the assignment hand out to compute the hash value.
        public int probe(int key, int i) {
    
        int h = ((A*key)%(power2(w))) >> (w-r);        
        if ( i<m ) {
        	return ((h+i)%(power2(r)));
        }
        else {
        	return -1;
        }
        
     }

     
        /**Inserts key k into hash table. Returns the number of collisions encountered*/
        /* I am starting with a collis value of 0 and adding to it for each collision.
         * Computing the hash value with i from 0 to m-1 and inserting in the first empty slot.*/
        public int insertKey(int key){
        	int collis = 0;
        	
        	for(int i=0 ; i<m ; i++) {
        		int hash = probe(key, i);
        		if (Table[hash] == -1 || Table[hash] == -2) {
        			Table[hash] = key;
        			break;
        		}
        		else {
        			collis++;
        		}
        	}
        	return collis;
        }
        
        /**Sequentially inserts a list of keys into the HashTable. Outputs total number of collisions */
        public int insertKeyArray (int[] keyArray){
            int collision = 0;
            for (int key: keyArray) {
                collision += insertKey(key);
            }
            return collision;
        }
            
         /**Inserts key k into hash table. Returns the number of collisions encountered*/
        /* Iterating through the array and removing the element when found, replacing the value with -2.*/
        public int removeKey(int key){
        	int collis = 0;
        	for(int i=0 ; i<m ; i++) {
        		int hash = probe(key, i);
        		
        		if (Table[hash] == key) {
        			Table[hash] = -2;
        			break;
        		}
        		else {
        			collis++;	
        		}
        	}
        	return collis;
        }
}
