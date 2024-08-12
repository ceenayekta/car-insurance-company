package NodesAndRecursive;
public class BinaryNode {
  private String ID; 
  private double value; 
  private BinaryNode left; 
  private BinaryNode right; 

  public BinaryNode(String ID, double value) {
    this.ID = ID; 
    this.value = value; 
    this.left = null;
    this.right = null;
  }

  public void addLeft(BinaryNode left) {
    this.left = left;
  }

  public void addRight(BinaryNode right) {
    this.right = right;
  }

  @Override
  public String toString() {
    return ID + " " + value;
  }

  public void preorder() {
    System.out.println(this);
    if (left != null) left.preorder();
    if (right != null) right.preorder();
  }

  public void inorder() {
    if (left != null) left.inorder();
    System.out.println(this);
    if (right != null) right.inorder();
  }

  public void postorder() {
    if (left != null) left.postorder();
    if (right != null) right.postorder();
    System.out.println(this);
  }

  public double totalValue() {
    double total = value;
    if (left != null) total += left.totalValue();
    if (right != null) total += right.totalValue();
    return total;
  }
}
