package bbidder.utils;

public class MyIntStream implements MyStream<Integer> {
    final int[] arr;
    int i = 0;

    public MyIntStream(int[] arr) {
        super();
        this.arr = arr;
    }
    
    @Override
    public Integer next() {
        if (i >= arr.length) {
           return null; 
        }
        return arr[i++];
    }

}
