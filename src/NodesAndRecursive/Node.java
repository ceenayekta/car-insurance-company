package NodesAndRecursive;
public class Node {
  private String ID;
  private double value;
  private Node next;

  public Node(String ID, double value) {
    this.ID = ID;
    this.value = value;
    this.next = null;
  }

  public void addNext(Node next) {
    this.next = next;
  }

  public String toString() {
    return ID + " " + value;
  }

  public void print() {
    System.out.println(this);
    if (next != null) next.print();
  }

  public void printIt() {
    Node current = this;
    while (current != null) {
      System.out.println(current);
      current = current.next;
    }
  }

  public double totalValue() {
    double total = value;
    if (next != null) total += next.totalValue();
    return total;
  }

  public double totalValueIt() {
    double total = 0;
    Node current = this;
    while (current != null) {
      total += current.value;
      current = current.next;
    }
    return total;
  }

}
