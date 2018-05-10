package base.zc.com.project.test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

public class JavaTest {

    public static void main(String[] args){

        TreeNode node16 = new TreeNode(16, null, null);
        TreeNode node15 = new TreeNode(15, null, null);
        TreeNode node14 = new TreeNode(14, null, null);
        TreeNode node13 = new TreeNode(13, null, null);
        TreeNode node12 = new TreeNode(12, null, null);
        TreeNode node11 = new TreeNode(11, null, null);
        TreeNode node10 = new TreeNode(10, node16, null);
        TreeNode node9 = new TreeNode(9, null, null);
        TreeNode node8 = new TreeNode(8, null, null);
        TreeNode node7 = new TreeNode(7, node14, node15);
        TreeNode node6 = new TreeNode(6, node12, node13);
        TreeNode node5 = new TreeNode(5, node10, node11);
        TreeNode node4 = new TreeNode(4, node8, node9);
        TreeNode node3 = new TreeNode(3, node6, node7);
        TreeNode node2 = new TreeNode(2, node4, node5);
        TreeNode root = new TreeNode(1, node2, node3);

        Stack stack = new Stack();
        stack.push(root);
        while(!stack.empty()){
            TreeNode node = (TreeNode) stack.pop();
            System.out.println("Stack二叉树---" + node.val);
            if(node.right != null){
                stack.push(node.right);
            }
            if(node.left != null){
                stack.push(node.left);
            }
        }

        Queue queue = new ArrayDeque();
        queue.offer(root);
        while(!queue.isEmpty()){
            TreeNode node = (TreeNode) queue.poll();
            System.out.println("Queue二叉树---" + node.val);
            if(node.left != null){
                queue.offer(node.left);
            }
            if(node.right != null){
                queue.offer(node.right);
            }
        }

        printTree(root);

        StringBuffer buffer = new StringBuffer();
        buffer.append("213");
        buffer.toString();
    }

    public static ArrayList<ArrayList<Integer>> printTree(TreeNode root){

        Map<Integer, ArrayList> map = setTreeData(root, 0);

        System.out.println("二叉树信息");
        for(Map.Entry<Integer, ArrayList> entry: map.entrySet()){
            Integer level = entry.getKey();
            ArrayList list = entry.getValue();
            System.out.println("级别---" + level + "---" + list.toString());
        }

        return null;
    }

    public static Map<Integer, ArrayList> setTreeData(TreeNode treeNode, int level){
        return setTreeData(treeNode, level, null);
    }

    public static Map<Integer, ArrayList> setTreeData(TreeNode treeNode, int level, Map<Integer, ArrayList> map){
        if(map == null){
            map = new TreeMap<>();
        }
        ArrayList<Integer> list = map.get(level);
        if(list == null){
            list = new ArrayList<>();
            map.put(level, list);
        }
        list.add(treeNode.val);
        System.out.print("level---" + level + "---" + treeNode.val);
        System.out.println("---left:" + (treeNode.left != null ? treeNode.left.val : "空") + "---right:" + (treeNode.right != null ? treeNode.right.val : "空"));
        if(treeNode.left != null){
            setTreeData(treeNode.left, level + 1, map);
        }
        if(treeNode.right != null){
            setTreeData(treeNode.right, level + 1, map);
        }
        return map;
    }

    static class TreeNode{

        int val;
        TreeNode left, right;

        public TreeNode(int val, TreeNode left, TreeNode right){
            this.val = val;
            this.left = left;
            this.right = right;
        }

    }

}
