package section12;

public class BinarySearchTree<T extends Comparable<T>> implements Tree<T> {

    private Node<T> root;

    @Override
    public void insert(T data) {
        if (root == null) {
            root = new Node<>(data, null);
        } else {
            insert(data, root);
        }
    }

    private void insert(T data, Node<T> node) {
        if (node.getData().compareTo(data) > 0) {
            if (node.getLeftChild() != null) {
                insert(data, node.getLeftChild());
            } else {
                node.setLeftChild(new Node<>(data, node));
            }
        } else {
            if (node.getRightChild() != null) {
                insert(data, node.getRightChild());
            } else {
                node.setRightChild(new Node<>(data, node));
            }
        }
    }

    @Override
    public void remove(T data) {

    }

    @Override
    public void traversal() {
        if (root == null) return;

        traversal(root);
    }

    private void traversal(Node<T> node) {
        if (node.getLeftChild() != null) {
            traversal(node.getLeftChild());
        }

        System.out.print(node + " - ");

        if (node.getRightChild() != null) {
            traversal(node.getRightChild());
        }
    }

    @Override
    public T getMax() {
        if (root == null) {
            return null;
        }

        return getMax(root);
    }

    private T getMax(Node<T> node) {
        if (node.getRightChild() != null) {
            return getMax(node.getRightChild());
        } else {
            return node.getData();
        }
    }

    @Override
    public T getMin() {
        if (root == null) {
            return null;
        }

        return getMin(root);
    }

    private T getMin(Node<T> node) {
        if (node.getLeftChild() != null) {
            return getMin(node.getLeftChild());
        } else {
            return node.getData();
        }
    }
}
