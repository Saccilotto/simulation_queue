public class Queue {

    private int capacity;
    private Node first, last;

    public Queue(int capacity){
        this.capacity = capacity;
        first = null;
        last = null;
    }

    public void enqueue(int data){
        Node node = new Node(data);
        if(isEmpty()){
            first = node;
        }else {
            last.setNextNode(node);
        }
        last = node;
        capacity++;
    }

    public int dequeue() throws Exception {
        if(isEmpty()){
            throw new Exception("Empty queue");
        }
        int result = first.getData();
        first = first.getNextNode();
        capacity--;
        if(isEmpty()){
            last = null;
        }
        return result;
    }

    public int first() throws Exception {
        if(isEmpty()){
            throw new Exception("Queue is empty");
        }
        return first.getData();
    }

    public  boolean isEmpty(){
        return capacity == 0;
    }

    public int size(){
        return capacity;
    }
}
