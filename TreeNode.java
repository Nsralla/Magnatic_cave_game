package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;

public class TreeNode {
    private int value; // 1,0,-1
    private char [][]colors=new char[8][8];
    private int [][]indexes=new int [8][8];
    private ArrayList<TreeNode> children=new ArrayList<>();
    private boolean generateChildrens=true;
    private int lastRowPlaced;
    private int lastColumnPlaced;
    private int countH=1;
    private int countV=1;
    private int countD=1;
    private int count=0;
    private int count2=0;


    public TreeNode(){

    }
    public TreeNode(int value, char[][] colors, ArrayList<TreeNode>children,int[][]indexes) {
        this.value = value;
        this.colors = colors;
        this.children=children;
        this.indexes=indexes;
    }

    public TreeNode(int value, char[][] colors, int[][] indexes, ArrayList<TreeNode> children, boolean generateChildrens, int lastRowPlaced, int lastColumnPlaced, int countH, int countV, int countD) {
        this.value = value;
        this.colors = colors;
        this.indexes = indexes;
        this.children = children;
        this.generateChildrens = generateChildrens;
        this.lastRowPlaced = lastRowPlaced;
        this.lastColumnPlaced = lastColumnPlaced;
        this.countH = countH;
        this.countV = countV;
        this.countD = countD;
    }

    public TreeNode(int value, char[][] colors, int[][] indexes, ArrayList<TreeNode> children, boolean generateChildrens, int lastRowPlaced, int lastColumnPlaced, int countH, int countV, int countD, int count, int count2) {
        this.value = value;
        this.colors = colors;
        this.indexes = indexes;
        this.children = children;
        this.generateChildrens = generateChildrens;
        this.lastRowPlaced = lastRowPlaced;
        this.lastColumnPlaced = lastColumnPlaced;
        this.countH = countH;
        this.countV = countV;
        this.countD = countD;
        this.count = count;
        this.count2 = count2;
    }

    public int getCount2() {
        return count2;
    }

    public void setCount2(int count2) {
        this.count2 = count2;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isGenerateChildrens() {
        return generateChildrens;
    }

    public int getCountH() {
        return countH;
    }

    public void setCountH(int countH) {
        this.countH = countH;
    }

    public int getCountV() {
        return countV;
    }

    public void setCountV(int countV) {
        this.countV = countV;
    }

    public int getCountD() {
        return countD;
    }

    public void setCountD(int countD) {
        this.countD = countD;
    }

    public void setGenerateChildrens(boolean generateChildrens) {
        this.generateChildrens = generateChildrens;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public char[][] getColors() {
        return colors;
    }
    public char getColors(int i,int j) {
        return colors[i][j];
    }

    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }

    public void setColors(char[][] colors) {
        this.colors = colors;
    }

    public void setColors(char[][] colors,int k,int l,char color) {
        //make copy of the colors array and save it
        for (int i = 0; i < colors.length; i++) {
            System.arraycopy(colors[i], 0, this.colors[i], 0, colors[i].length);
        }//make copy of the original color's board
        this.colors[k][l]=color;
    }

    public int[][] getIndexes() {
        return indexes;
    }
    public int getIndexes(int i,int j) {
        return indexes[i][j];
    }
    public void setIndexes(int[][] indexes,int k,int l) {
        for (int i = 0; i < indexes.length; i++) {
            System.arraycopy(indexes[i], 0, this.indexes[i], 0, indexes[i].length);
        }//make copy of the original integer's board
       this.indexes[k][l]=1;
    }
    public void setIndexes(int[][] indexes) {
        this.indexes = indexes;
    }

    public int getLastRowPlaced() {
        return lastRowPlaced;
    }

    public void setLastRowPlaced(int lastRowPlaced) {
        this.lastRowPlaced = lastRowPlaced;
    }

    public int getLastColumnPlaced() {
        return lastColumnPlaced;
    }

    public void setLastColumnPlaced(int lastColumnPlaced) {
        this.lastColumnPlaced = lastColumnPlaced;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", colors=" + Arrays.toString(colors) +
                ", children=" + children +
                '}';
    }
}
