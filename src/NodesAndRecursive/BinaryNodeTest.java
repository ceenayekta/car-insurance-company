package NodesAndRecursive;
public class BinaryNodeTest {
    public static void main(String[] args) throws Exception {
        BinaryNode a = new BinaryNode("A", 5);
        BinaryNode b = new BinaryNode("B", 10);
        BinaryNode c = new BinaryNode("C", 15);
        BinaryNode d = new BinaryNode("D", 0);
        BinaryNode e = new BinaryNode("E", 35);
        BinaryNode f = new BinaryNode("F", 5);
        BinaryNode g = new BinaryNode("G", 10);
        BinaryNode h = new BinaryNode("H", 15);
        BinaryNode i = new BinaryNode("I", 0);

        f.addLeft(b);
        f.addRight(g);
        b.addLeft(a);
        b.addRight(d);
        d.addLeft(c);
        d.addRight(e);
        g.addRight(i);
        i.addLeft(h);

        //traverse test
        System.out.println("C traverse methods");
        System.out.println("preorder:");
        c.preorder();
        System.out.println("inorder:");
        c.inorder();
        System.out.println("postorder:");
        c.postorder();
        System.out.println();
        System.out.println("B traverse methods");
        System.out.println("preorder:");
        b.preorder();
        System.out.println("inorder:");
        b.inorder();
        System.out.println("postorder:");
        b.postorder();
        System.out.println();
        System.out.println("F traverse methods");
        System.out.println("preorder:");
        f.preorder();
        System.out.println("inorder:");
        f.inorder();
        System.out.println("postorder:");
        f.postorder();

        //value test
        System.out.println();
        System.out.println("C total value:" + c.totalValue());
        System.out.println("B total value:" + b.totalValue());
        System.out.println("F total value:" + f.totalValue());
    }
}
