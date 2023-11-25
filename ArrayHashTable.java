package structures;

import java.util.Collection;
import java.util.Iterator;

//This class implements a hashtable for a generic key and value type using an array backend.
public class ArrayHashTable<K, V> implements HashTable<K, V> {

  //Default constructor values- DO NOT CHANGE.
  public static final int DEFAULT_CAPACITY = 5;
  public static final double DEFAULT_LOAD_FACTOR = 0.7;

  //Provided instance variables- DO NOT CHANGE
  protected K[] keyArray;  //stores all keys 
  protected V[] valueArray;  // stores all values
  protected boolean[] isActiveArray;  // if true the cell is active, false the cell has data to be deleted.
  protected CollisionHandler <K> collisionHandler;  // collision handler can be either linear or quadratic.
  protected int count; // counts the number of key/value pairs on the hashtable
  protected int capacity;  // length of the arrays
  private double loadFactor;  // the ratio (0 < loadFactor < 1) that determines when to resize the hashtable.

  /**
   *   Initialize count to 0, capacity and loadFactor to the respective DEFAULT values,
   *   collisionHandler to the collisionHdler argument, create the three arrays
   *   for keys, values, and the isActive boolean array.
   */
  public ArrayHashTable(CollisionHandler <K> collisionHdler) {
      //TODO: Implement this method.
      count = 0;  
      capacity = DEFAULT_CAPACITY;;
      loadFactor = DEFAULT_LOAD_FACTOR;
      keyArray = (K[]) new Object[capacity];
      valueArray = (V[]) new Object[capacity];
      isActiveArray = new boolean[capacity];
      collisionHandler = collisionHdler;
  }

  /**
   *   Initialize count to 0, capacity to the argument, loadFactor to the respective DEFAULT values,
   *   collisionHandler to the collisionHdler argument, create the three arrays
   *   for keys, values, and the isActive boolean array.
   */
  public ArrayHashTable(int capacity, CollisionHandler <K> collisionHdler){
      //TODO: Implement this method.
      count = 0;
      this.capacity = capacity;
      loadFactor = DEFAULT_LOAD_FACTOR;
      keyArray = (K[]) new Object[capacity];
      valueArray = (V[]) new Object[capacity];
      isActiveArray = new boolean[capacity];
      collisionHandler = collisionHdler;
  }

  /**
   *   Initialize count to 0, capacity and loadFactor to the respective argument values,
   *   collisionHandler to the collisionHdler argument, create the three arrays
   *   for keys, values, and the isActive boolean array.
   */
  public ArrayHashTable(int capacity, double loadFactor, CollisionHandler <K> collisionHdler){
      //TODO: Implement this method.
      count = 0;
      this.capacity = capacity;
      this.loadFactor = loadFactor;
      keyArray = (K[]) new Object[capacity];
      valueArray = (V[]) new Object[capacity];
      isActiveArray = new boolean[capacity];
      collisionHandler = collisionHdler;
  }

  private int getHash(K key) {
    // hashCode method: a function that returns the hashcode value of an object on calling
    // gets the key
    return Math.abs(key.hashCode());
  }

  // returns the array index for the value associated with the provided key
  private int getIndex(K key) {
    int hashCode = getHash(key);
    int hashIndex = hashCode % capacity;
    return Math.abs(hashIndex);
  }

  /** 
   * Returns the number of elements in the hash table.
   */
  public int getSize() {
      //TODO: Implement this method.
      return count;
  }

  private int resolveCollision(int index) {
    int newIndex = collisionHandler.probe(index, isActiveArray, capacity);
    return newIndex;
  }

   /**
   * One approach: Double the capacity of the hashtable. Create new, larger arrays.
   * For each key in the key array, if that cell is active (true) in
   * the activeArray, get the index by calling the hash function, use the collisionHandler
   * probe method to get the new index (it may be different from the original hash if
   * a collision occurred). Enter the key in the new keyArray, value in the new valueArray,
   * and set the new activeArray to true. Finally, assign the current keyArray, valueArray,
   * and activeArray to the new arrays.
   */
  private void resizeArray() {
    //TODO: Implement this method
    capacity = capacity*2;
    ArrayHashTable<K, V> newTable = new ArrayHashTable<K, V>(capacity, loadFactor, collisionHandler);
    for (int i = 0; i < keyArray.length; i++) {
      if (isActiveArray[i] == true) {
        newTable.put(keyArray[i], valueArray[i]);
      }
    }
    keyArray = newTable.keyArray;
    valueArray = newTable.valueArray;
    isActiveArray = newTable.isActiveArray;
    count = newTable.count;
  } 

  /**
  * Calculates and returns the load factor of the hash table.
  * This is the ratio of the number of elements stored in the hashtable
  * divided by the total capacity of the hashtable.
  */
  private double calcLoadFactor() {
   //TODO: Implement this method
      //double loadFactor = (double)count/capacity;
      return (double)count/capacity;
  } 

  /** 
   * Insert the provided key value pair into the hashtable. If the key exists in 
   * the table, the stored value is overwritten with the new value passed in.
   * Please use the provided getHash method to get the hash for the array index.
   * Steps: check if current load is > loadFactor. If true, call resizeArray.
   * Then see if key is in the table by calling getIndex. If key exists, overwrite value
   * in the valueArray. If key does not exist, get the index by calling the hash function (getHash).
   * Then call resolveCollision to see if a collision occurs and if so, that methos will use the 
   * collision handler to resolve it. Then enter the key in the keyArray using the index returned from 
   * resolveCollision, enter the value in the valueArray, and set the isActiveArray to true. Finally,
   * increment the count.
   */
  public void put(K key, V value) {
      //TODO: Implement this method.
    if (calcLoadFactor() > loadFactor) {
      resizeArray();
    }
    int startIndex = getIndex(key);
    int index = collisionHandler.search(startIndex, key, keyArray, isActiveArray, capacity);

    if (index == -1) {
      int returnIndex = resolveCollision(startIndex);
      keyArray[returnIndex] = key;
      valueArray[returnIndex] = value;
      isActiveArray[returnIndex] = true;
      count++; 
    }
    else {
      valueArray[index] = value;
      isActiveArray[startIndex] = true;
    } 
  }

  /**
   * Returns the value associated with the key if it exists in the hashtable.
   * Get the index by calling the hash function, then check if key exists by calling getIndex.
   * Return the value in the valueArray if key is in the table.
   * Returns null if the key is not in the table.
   */
  public V getValue(K target) {
   //TODO: Implement this method
    int startIndex = getIndex(target);

    int index = collisionHandler.search(startIndex, target, keyArray, isActiveArray, capacity);

    if (index != -1) {
        return valueArray[index];
    }
    else {
      return null;
    }
  }

  /**
   * Removes the key-value pair specified by the targetKey from the hashtable and returns the value. 
   * The method gets the hash on the targetKey, then checks if the key is in the table by calling getIndex.
   * Then, if targetKey is in the table, get the value associated with the targetKey from the valueArray,
   * then set the activeArray to false as this cell is now open, decrement the count, and return the value.
   * 
   * @throws ElementNotFoundException if the target does not exist in the table.
   */
  public V remove(K targetKey)throws ElementNotFoundException {
    //TODO: Implement this method
    V value = null;
    //int hash = getHash(targetKey);
    int startIndex = getIndex(targetKey);
    int index = collisionHandler.search(startIndex, targetKey, keyArray, isActiveArray, capacity);

    if (index != -1) {
      value = valueArray[index];

      isActiveArray[index] = false;
      count--;
      return value;
    }
    else {
      throw new ElementNotFoundException("The key does not exist");
    }
  }
  /**
   *  Returns a KeyIterator for the keys in this HashTable. 
   */
  public Iterator<K> keyIterator(){
       return new KeyIterator<K>(keyArray, count);
  }

    /******** methods used for testing purposes ***********/
    public int getCapacity(){
      return capacity;
    }

    public double getLoadFactor(){
      return loadFactor;
    }
  
    public K[] getKeyArray(){
       return keyArray;
    }  
  
    public V[] getValueArray(){
      return valueArray;
   }

   public boolean[] getIsActiveArray(){
     return isActiveArray;
   }

   public CollisionHandler <K>  getCollisionHandler(){
     return collisionHandler;
   }
  /************************************************************/

   /**  
    * Use this code for testing your ArrayHashTable. 
    * Comment out statements as needed. Use the debugger to see
    * what your code is doing by stepping in, out, and over methods
    * and statements, and checking the variables pane to see the state 
    * of your table and its variables.
    */
  public static void main(String[] args) throws Exception {
      // Init the type of collision handler: linear or quadratic.
     CollisionHandler <String> collisionHdler = new LinearCollisionHandler<>();
     //CollisionHandler <String> collisionHdler = new QuadraticCollisionHandler<>();
     ArrayHashTable<String, String> table = new ArrayHashTable<String, String> (collisionHdler);
     table.put("fatimeh", "ef4#B%k");
     // test the get method after the put method was called:
     System.out.println("test get after put: "+table.getValue("fatimeh"));
     table.put("matt", "A9d%&b");
     table.put("fadhil", "2h*k9s");
     table.put("rumeng", "j8*shX2");
    // this next call should cause a resize- uncomment when the table works on the above data:
     table.put("harper", "m8Ut6%#a");
    // test the keyIterator
     Iterator<String> keyIter = table.keyIterator();
     while(keyIter.hasNext()){
       String curKey = keyIter.next();
       System.out.print(curKey+ " ");
       System.out.println(table.getValue(curKey)+", ");
     }
    // test removing 
    table.remove("matt");
    System.out.println("test remove: "+table.getValue("matt"));
  }
}