package FrequentPatternGrowth;

import java.util.LinkedList;
import java.util.List;

public class TreeNode implements Comparable<TreeNode> {
    private String name;
    private Long count = 0L;
    private TreeNode next;
    private List<TreeNode> children;
    private TreeNode parent;

    public TreeNode findChild(String name) {
        if (children != null)
            for (TreeNode child : children) {
                if (child.getName() != null && child.getName().equals(name)) {
                    return child;
                }
            }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public TreeNode getNext() {
        return next;
    }

    public void setNext(TreeNode next) {
        this.next = next;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public void increase(Long increaseNum) {
        this.count += increaseNum;
    }

    public void increase() {
        this.count++;
    }

    public void addChild(TreeNode node) {
        if (children == null)
            children = new LinkedList<>();
        children.add(node);

    }

    @Override
    public String toString() {
        return name + " = " + count;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass()) {
            return false;
        }

        TreeNode node = (TreeNode) obj;

        return node.name.equals(name);
    }

    @Override
    public int compareTo(TreeNode o) {
        return this.count - o.count > 0 ? -1 : 1;
    }


}
