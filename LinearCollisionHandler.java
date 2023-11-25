package structures;

public class LinearCollisionHandler <K> implements CollisionHandler <K>{
    private int probeLength;

    /**
  * Constructors to set probeLength to 1, or a different length.
  */
    public LinearCollisionHandler(){
        this.probeLength = 1;
    }

    public LinearCollisionHandler(int probeLength){
        this.probeLength = probeLength;
    }

/**
  * Method starts at index and searches linearly until an open spot
  * is found in the array. This could include index itself.
  * index = (index + probeLength) % size
  * M is the size of the table
  */
  public int probe(int index, boolean[] activeArray, int M) {
    //TODO: Implement this method.
    if (activeArray[index] == false) {
      return index;
    }
    while (activeArray[index] == true) {
      index = Math.abs(index+probeLength) % M;
    }
   return index;
 }

/**
* Start at index and search the array linearly until the target
* is found. Then return the array index of the target. 
* Return -1 if not found.
*/
  public int search(int startIndex, K target, K[] keyArray, boolean [] activeArray, int M){
    //TODO: Implement this method.

    //make sure that the startIndex less than M
    if (startIndex >= M) {
      return -1;
    }
    int index = startIndex;

    if(keyArray[index] == null) {
      return -1;
    }

    
    while (!(keyArray[index] != null && keyArray[index].equals(target) && activeArray[index])) {
        index = Math.abs(index+probeLength) % M;
        if (keyArray[index] == null) {
          return -1;
        }
        if (index == startIndex) {
          //all the elements have been searched and the index got back to startIndex
          return -1;
        }
    }
    return index;
  }

}
