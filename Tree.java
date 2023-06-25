package com.example.demo;

public class Tree {
    private TreeNode root;

    public  Tree(){
        root=null;
    }

    public Tree(TreeNode root) {
        this.root = root;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void insert(TreeNode newNode, TreeNode parent){
        parent.getChildren().add(newNode);
    }

}
