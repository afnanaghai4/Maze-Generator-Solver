import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;
import java.util.*;

class Grid extends JFrame {
    public int size = 0;
    MyButton Startingpoint;
    MyButton Endingpoint;
    Boolean[][] visited;//visited Array, is true when the visited has been visited in DFS(Maze generation)
    MyButton[][] Grid;//Custom button class Ive extended Jbutton to make my custom Vertex class
    JFrame frame;
    JPanel panel;
    int dimension;
    int rows = 0;
    int cols = 0;

    public void Gridcreate(int size) {
        size=size+1;
        dimension = (size) * 10;
        int noofcolumns = size;
        Grid = new MyButton[noofcolumns][noofcolumns];//Array of Buttons(Vertex)
        size = size * size;//to create a square
        panel = new JPanel();//my custom Panel class extends JPANEL
        frame = new JFrame("MAZE");
        this.size = size;
        Container con = getContentPane();
        panel.setLayout(null);//so that I can map the buttons wherever I want to map them plus this allows me to create an Array while Im placing them
        con.add(panel);
        for (int i = 0; i < dimension; i += (int) dimension / (noofcolumns)) {//increasing the value of i by button's width
            if ((i + dimension / noofcolumns) > dimension)
                break;
            for (int j = 0; j < dimension; j += (int) dimension / (noofcolumns)) {//increasing j by buttons width
                JButton b1 = new MyButton();
                b1.setBounds(j, i, (int) dimension / (noofcolumns), (int) dimension / (noofcolumns));//buttons width=dimension/number of columns
                Grid[rows][cols] = (MyButton) b1;
                Grid[rows][cols].setBackground(Color.black);
                b1.setBorderPainted(false);//so it looks good
                b1.setEnabled(false);//button cant be clicked
                panel.add(b1);
                cols++;// the array
            }
            rows++;//the array
            cols = 0;
        }
        frame.add("Center", con);
        frame.setResizable(false);
        frame.setSize(dimension + 2 * ((int) dimension / noofcolumns), dimension + 4 * ((int) dimension / noofcolumns));//to account for Spacing between each button
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setBackground(Color.black);
        frame.setVisible(true);
        computeGridEdges();
        MazeGenerator();
        recomputeedges();
        shortestpath();
        //numberofneighbours();

    }

    public void computeGridEdges() {//computes the neighbours ..
        for (int r = 1; r < Grid[1].length - 1; r++) {
            for (int c = 1; c < Grid[1].length - 1; c++) {

                Grid[r][c].neighbors.add('N');
                Grid[r][c].neighbors.add('S');
                Grid[r][c].neighbors.add('E');//initially Ive added all North south east west
                Grid[r][c].neighbors.add('W');
                if (c == 1 || r == 1 || c == Grid[1].length - 2 || r == Grid[1].length - 2) {
                    if (c == 1) {//if left most column dont want to link it to the border

                        Grid[r][c].neighbors.remove((Character) 'W');//node to the left
                        //[r][c].setBackground(Color.RED);

                    }
                    if (r == 1) {//top row

                        Grid[r][c].neighbors.remove((Character) 'N');

                    }
                    if (c == Grid[1].length - 2) {//rightmost column

                        Grid[r][c].neighbors.remove((Character) 'E');

                    }
                    if (r == Grid[1].length - 2) {//bottom row

                        Grid[r][c].neighbors.remove((Character) 'S');
                       ;
                    }
                }
            }
        }
    }

    public void MazeGenerator() {
        visited = new Boolean[Grid[0].length][Grid[0].length];// havent initialized it because thats not necessary.. it will just add to the complexity
        int rows = Grid[0].length;
        int cols = Grid[0].length;//because its always going to be a square
        //now generate a random row and a random column and go to that location on the grid
        int startingrow = 1 + (int) (Math.random() * ((rows - 2) - 1) + 1);
        int startingcol = 1 + (int) (Math.random() * ((cols - 2) - 1) + 1);
        //Grid[startingrow][startingcol].setBackground(Color.RED);
        //visited[startingrow][startingcol]=true;
        //stopping criteria keep going until there is no where else to go.. when no 2 straight buttons are left
        traversemaze(startingrow, startingcol);
        //traversemaze(1, 1);
        Grid[startingrow][startingcol].setBackground(Color.WHITE);
        //Grid[1][1].setBackground(Color.RED);//ill be using this if I want the maze to start generating from 1,1
    }

    public void traversemaze(int Irow, int Icol) {//DFS generating a maze
        int n = Grid[Irow][Icol].neighbors.size();//number of neighbours it has
        //push it into the stack
        int randompath = (int) (Math.random() * n);//getting a random index in the neighbours array list (basically getting a random Neighbour)
        Stack<MyButton> stk = new Stack();
        visited[Irow][Icol] = true;
        Grid[Irow][Icol].iswall = false;//this is not a wall anymore(initially every button was a wall)
        stk.push(Grid[Irow][Icol]);
        //ERROR NUMBER 1 ALREADY KNOW THE SOLUTION..
        //you might find an issue in which the maze doesnt go towards the left that is because we are moving traversing 3 vertexes in the first iterations.. there an easy fix for that.. we just move 1 vertex before the loops start
        //Startingpoint=stk.peek();
        while (!stk.isEmpty()) {//stack empty means there are no more buttons left with any more neighbours that could be traversed
            while (n > 0) {//while number of neighbours is greater than 0 if it is zero that means this button has been used there is nowhere else to go.. once that happens pop the stack
                randompath = (int) (Math.random() * n);//recomputing a random neighbour again for the loop
                // Grid[Irow][Icol].setText(Grid[Irow][Icol].neighbors.get(randompath) + "");
                visited[Irow][Icol] = true;
                Grid[Irow][Icol].iswall = false;
                //if(stk.peek()!=Grid[Irow][Icol])
                //stk.push(Grid[Irow][Icol]);
                Character x = Grid[Irow][Icol].neighbors.get(randompath);//retrieves the data stored in the neighbour list either North south east west
                //ERROR no 2 One of these if statements Might not be correct
                if (x == 'N' && Grid[Irow - 1][Icol].neighbors.contains('N') && visited[Irow - 1][Icol] == null && visited[Irow - 2][Icol] == null && visited[Irow - 1][Icol + 1] == null && visited[Irow - 1][Icol - 1] == null && visited[Irow - 2][Icol + 1] == null && visited[Irow - 2][Icol - 1] == null) {//all the conditions that have to be checked before I more 2 steps in a direction
                //if (x == 'N' && Grid[Irow - 1][Icol].neighbors.contains('N') && visited[Irow - 1][Icol] == null && visited[Irow - 2][Icol] == null&& visited[Irow - 1][Icol + 1] == null && visited[Irow - 1][Icol - 1] == null){
                    visited[Irow - 1][Icol] = true;
                    //north= row-1
                    visited[Irow - 2][Icol] = true;
                    //push it into the stack
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {//slowing it down makes it look smooth
                        e.printStackTrace();
                    }
                    Grid[--Irow][Icol].setBackground(Color.WHITE);
                    Grid[Irow][Icol].iswall = false;
                    stk.push(Grid[Irow][Icol]);//push each button(2) to the stack for back tracking later on
                    Grid[--Irow][Icol].setBackground(Color.WHITE);
                    Grid[Irow][Icol].iswall = false;
                    stk.push(Grid[Irow][Icol]);

                } else if (x == 'S' && Grid[Irow + 1][Icol].neighbors.contains('S') && visited[Irow + 1][Icol] == null && visited[Irow + 2][Icol] == null && visited[Irow + 1][Icol + 1] == null && visited[Irow + 1][Icol - 1] == null && visited[Irow + 2][Icol + 1] == null && visited[Irow + 2][Icol - 1] == null) {
                //else if (x == 'S' && Grid[Irow + 1][Icol].neighbors.contains('S') && visited[Irow + 1][Icol] == null && visited[Irow + 2][Icol] == null&& visited[Irow + 1][Icol + 1] == null && visited[Irow + 1][Icol - 1] == null){
                visited[Irow + 1][Icol] = true;
                    visited[Irow + 2][Icol] = true;
                    //push it into the stack
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Grid[++Irow][Icol].setBackground(Color.WHITE);
                    Grid[Irow][Icol].iswall = false;
                    stk.push(Grid[Irow][Icol]);
                    Grid[++Irow][Icol].setBackground(Color.WHITE);
                    Grid[Irow][Icol].iswall = false;
                    stk.push(Grid[Irow][Icol]);

                } else if (x == 'E' && Grid[Irow][Icol + 1].neighbors.contains('E') && visited[Irow][Icol + 1] == null && visited[Irow][Icol + 2] == null && visited[Irow - 1][Icol + 1] == null && visited[Irow + 1][Icol + 1] == null && visited[Irow - 1][Icol + 2] == null && visited[Irow + 1][Icol + 2] == null) {
                  //else if (x == 'E' && Grid[Irow][Icol + 1].neighbors.contains('E') && visited[Irow][Icol + 1] == null && visited[Irow][Icol + 2] == null&& visited[Irow - 1][Icol + 1] == null && visited[Irow + 1][Icol + 1] == null ){
                    visited[Irow][Icol + 1] = true;
                    visited[Irow][Icol + 2] = true;
                    //push it into the stack
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Grid[Irow][++Icol].setBackground(Color.WHITE);
                    Grid[Irow][Icol].iswall = false;
                    stk.push(Grid[Irow][Icol]);
                    Grid[Irow][++Icol].setBackground(Color.WHITE);
                    Grid[Irow][Icol].iswall = false;
                    stk.push(Grid[Irow][Icol]);
                    // Grid[Irow][Icol].setText(Grid[Irow][Icol].neighbors.size()+"");
                    //n = Grid[Irow][Icol].neighbors.size();
                } else if (x == 'W' && Grid[Irow][Icol - 1].neighbors.contains('W') && visited[Irow][Icol - 1] == null && visited[Irow][Icol - 2] == null && visited[Irow - 1][Icol - 1] == null && visited[Irow + 1][Icol - 1] == null && visited[Irow - 1][Icol - 2] == null && visited[Irow + 1][Icol - 2] == null) {
                //else if (x == 'W' && Grid[Irow][Icol - 1].neighbors.contains('W') && visited[Irow][Icol - 1] == null && visited[Irow][Icol - 2] == null && visited[Irow - 1][Icol - 1] == null && visited[Irow + 1][Icol - 1] == null ){
                visited[Irow][Icol - 1] = true;
                    visited[Irow][Icol - 2] = true;
                    //push it into the stack
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Grid[Irow][--Icol].setBackground(Color.WHITE);
                    Grid[Irow][Icol].iswall = false;
                    stk.push(Grid[Irow][Icol]);
                    Grid[Irow][--Icol].setBackground(Color.WHITE);
                    Grid[Irow][Icol].iswall = false;
                    stk.push(Grid[Irow][Icol]);

                } else {
                    Grid[Irow][Icol].neighbors.remove((Character) x);//error what if randompath n doesnt exist I DONT THINK SO
                    //if one of the condition for any neighbour fails we need to remove it so that we dont try to go there anymore

                }
                n = Grid[Irow][Icol].neighbors.size(); //recompute the size

            }
            if (!stk.isEmpty()) {//all neighbours have been checked
                MyButton stkp = stk.pop();
                int[] a = getindex(stkp);//get the row and column values of the button at the top of the stack
                Irow = a[0];
                Icol = a[1];
                //Grid[Irow][Icol].setBackground(Color.CYAN);
                n = Grid[Irow][Icol].neighbors.size();


            }
        }
        //this is when all the vertexes and buttons have been traversed
        int[] end = findlast();
        Endingpoint = Grid[end[0]][end[1]];//the bottom most button
        Endingpoint.setBackground(Color.blue);
        int[] start = findfirst();
        Startingpoint = Grid[start[0]][start[1]];//top most button
        Startingpoint.setBackground(Color.blue);


    }

    public void shortestpath() {//BFS
        int nsize;
        MyButton but1 = new MyButton();
        int i;
        int[] index = new int[2];//just declaring it for later
        Queue<MyButton> Q = new ArrayDeque();
        MyButton thisbutton = Startingpoint;//first button(top most)
        thisbutton.setBackground(Color.cyan);
        Q.add(thisbutton);//enqueue
        thisbutton.previous = null;//because its the first element
        while (!Q.isEmpty()) {//when Q = empty the entire maze has been traversed just exit
            but1 = ((ArrayDeque<MyButton>) Q).pop();
            index = getindex(but1);//index of this button
            int row = index[0];
            int col = index[1];
            Grid[row][col].pathvisited= true;
            nsize = but1.neighbors.size();
            while (nsize > 0) {//neighbour list size(if it is zero we need to back out)
                //but1.setBackground(Color.BLUE);
                i = (int) (Math.random() * nsize);//random neighbour we want to go to now
                //but1.setBackground(Color.blue);
                Character x = but1.neighbors.get(i);// either north south east west (neigbours)
                if (x == 'N') {
                    but1.neighbors.remove((Character) 'N');//because we dont need this anymore
                    //but1.setBackground(Color.MAGENTA);
                    if (but1 == Endingpoint) {//if this is the final destiation just exit the loops
                        but1.setBackground(Color.RED);
                        Q.clear();
                        break;
                    }
                   // if (visited[row - 1][col] != null && visited[row - 1][col] != false) {
                    if(!Grid[row-1][col].iswall && Grid[row-1][col].pathvisited==false){//if its not a wall and hasnt been visited
                        Q.add(Grid[row - 1][col]);//add it to the queue
                        Grid[row - 1][col].pathvisited = true;
                        Grid[row - 1][col].previous = but1;//previous button
                        //but1 = Grid[row - 1][col];
                        //nsize = but1.neighbors.size();
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        but1.setBackground(Color.cyan);
                        //Grid[row-2][col].setBackground(Color.yellow);
                        //if (nsize == 0)
                          //  but1.setBackground(Color.RED);
                        if (but1 == Endingpoint) {
                            but1.setBackground(Color.RED);
                            Q.clear();
                            break;
                        }
                    }
                    nsize = but1.neighbors.size();

                }
                else if (x == 'S') {
                    but1.neighbors.remove((Character) 'S');
                   // but1.setBackground(Color.MAGENTA);
                    if (but1 == Endingpoint) {
                        but1.setBackground(Color.RED);
                        Q.clear();
                        break;
                    }
                   // if (visited[row + 1][col] != null && visited[row + 1][col] != false) {
                    if(!Grid[row+1][col].iswall&& Grid[row+1][col].pathvisited==false){
                        Q.add(Grid[row + 1][col]);
                        Grid[row + 1][col].pathvisited = true;
                        Grid[row + 1][col].previous = but1;
                        //but1 = Grid[row + 1][col];
                        //nsize = but1.neighbors.size();
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        but1.setBackground(Color.cyan);
                       // Grid[row+2][col].setBackground(Color.yellow);
                        //if (nsize == 0)
                          //  but1.setBackground(Color.RED);
                        if (but1 == Endingpoint) {
                            but1.setBackground(Color.RED);
                            Q.clear();
                            break;
                        }
                    }
                    nsize = but1.neighbors.size();
                }
                else if (x == 'E') {
                    but1.neighbors.remove((Character) 'E');
                    //but1.setBackground(Color.MAGENTA);
                    if (but1 == Endingpoint) {
                        but1.setBackground(Color.RED);
                        Q.clear();
                        break;
                    }
                    //if (visited[row][col + 1] != null && visited[row][col + 1] != false) {
                    if(!Grid[row][col+1].iswall&& Grid[row][col+1].pathvisited==false){
                        Q.add(Grid[row][col + 1]);
                        Grid[row][col + 1].pathvisited = true;
                        Grid[row][col + 1].previous = but1;
                        //but1 = Grid[row][col + 1];
                        //nsize = but1.neighbors.size();
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        but1.setBackground(Color.cyan);
                        //Grid[row][col+2].setBackground(Color.yellow);
                        //if (nsize == 0)
                            //but1.setBackground(Color.RED);
                        if (but1 == Endingpoint) {
                            but1.setBackground(Color.RED);
                            Q.clear();
                            break;
                        }
                    }
                    nsize = but1.neighbors.size();
                }
                else if (x == 'W') {
                    but1.neighbors.remove((Character) 'W');
                    //but1.setBackground(Color.MAGENTA);
                    if (but1 == Endingpoint) {
                        but1.setBackground(Color.RED);
                        Q.clear();
                        break;
                    }
                    //if (visited[row][col - 1] != null && visited[row][col - 1] != false) {
                    if(!Grid[row][col-1].iswall&& Grid[row][col-1].pathvisited==false){
                        Q.add(Grid[row][col - 1]);
                        Grid[row][col - 1].pathvisited = true;
                        Grid[row][col - 1].previous = but1;
                        //but1 = Grid[row][col - 1];
                        //nsize = but1.neighbors.size();
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        but1.setBackground(Color.cyan);
                        //Grid[row][col-2].setBackground(Color.yellow);
                        //if (nsize == 0)
                          //  but1.setBackground(Color.RED);
                        if (but1 == Endingpoint) {
                            but1.setBackground(Color.RED);
                            Q.clear();
                            break;
                        }
                    }
                    nsize = but1.neighbors.size();//recompute the neighbour size.. because we've removed a neighbour
                }
            }
        }
        thisbutton = Endingpoint;//it can only exit when it has reached the end point or traversed the entire maze
        //either way the we can assume the end point has been traversed
        while (thisbutton.previous != null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            thisbutton.setBackground(Color.orange);
            thisbutton = thisbutton.previous;
        }
        Endingpoint.setBackground(Color.BLUE);
        Startingpoint.setBackground(Color.RED);
    }


    public int[] getindex(MyButton f) {//gives the array index of any button
        int[] output = new int[2];
        for (int i = 0; i < Grid[0].length; i++) {
            for (int j = 0; j < Grid[0].length; j++) {
                if (Grid[i][j] == f) {
                    output[0] = i;
                    output[1] = j;
                }

            }
        }
        return output;
    }

    public int[] findlast() {//finds the bottom most element (bottom rght most)
        int r = Grid[0].length - 1;//last row cant be visited
        int c = Grid[0].length - 1;//last col cant be visited
        int[] output = new int[2];
        for (int i = r; i > 0; i--) {
            for (int j = c; j > 0; j--) {
                if (visited[i][j] != null) {
                    output[0] = i;
                    output[1] = j;
                    return output;
                }
            }
        }
        return output;
    }

    public int[] findfirst() {//find the top most (left top most)
        int r = 1;//first row and first column are boundries; cant be visited
        int c = 1;
        int[] output = new int[2];
        for (int i = r; i < Grid[0].length; i++) {
            for (int j = c; j < Grid[0].length; j++) {
                if (visited[i][j] != null) {
                    output[0] = i;
                    output[1] = j;
                    return output;
                }
            }
        }
        return output;
    }

    public MyButton[][] returngrid() {
        return Grid;
    } //debugging

    public void numberofneighbours() {//debugging
        for (int i = 0; i < Grid[0].length; i++) {
            for (int j = 0; j < Grid[0].length; j++) {
               if(Grid[i][j].neighbors.size()==0){
                   Grid[i][j].setBackground(Color.black);
               }

               if(Grid[i][j].neighbors.contains('N')&& Grid[i][j].neighbors.contains('S')){
                   Grid[i][j].setBackground(Color.GREEN);
               }
                if(Grid[i][j].neighbors.contains('E')&& Grid[i][j].neighbors.contains('W')){
                    Grid[i][j].setBackground(Color.RED);
                }
            }
        }
    }
    public void recomputeedges(){//once the maze has been created we need to recompute its neighbours..
        //basically removed all the black neighbours if there are any left and only add white (path) neighboyrs
        for (int i = 1; i < Grid[0].length-1; i++) {
            for (int j = 1; j < Grid[0].length-1; j++) {
                Grid[i][j].neighbors.clear();
                if(!Grid[i][j].iswall && visited[i][j]!=null ){
                 if(visited[i-1][j]!=null)
                     Grid[i][j].neighbors.add('N');
                 if(visited[i+1][j]!=null)
                     Grid[i][j].neighbors.add('S');
                 if(visited[i][j-1]!=null)
                     Grid[i][j].neighbors.add('W');
                 if(visited[i][j+1]!=null)
                     Grid[i][j].neighbors.add('E');
             }
            }
        }
    }
}


class MyButton extends JButton {//my custom J button class
    //LinkedList<MyButton> neighbors=new LinkedList();
    ArrayList<Character> neighbors = new ArrayList();
    boolean iswall = true;
    MyButton previous;//for BFS
    boolean pathvisited=false;//for BF

}
//TOO EZ



