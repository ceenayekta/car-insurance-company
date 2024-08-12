package NodesAndRecursive;
public class NodeTest {
    public static void main(String[] args) throws Exception {
        Node n1 = new Node("A", 5);
        Node n2 = new Node("B", 10);
        Node n3 = new Node("C", 15);
        Node n4 = new Node("D", 0);
        Node n5 = new Node("E", 35);
        n1.addNext(n2);
        n2.addNext(n3);
        n3.addNext(n4);
        n4.addNext(n5);

        //print test
        System.out.println("Printing only last leaf:");
        n5.print();
        System.out.println();
        System.out.println("Printing tree:");
        n1.print();

        //value test
        System.out.println();
        System.out.println("Total value of last 3 leaves: " + n3.totalValue());
        System.out.println("Printing total value of tree: " + n1.totalValue());
    }
}
