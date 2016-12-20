/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myP;

/**
 *
 * @author azhidkov
 */
public class myc_map {
    //public myc_point[][] map = new myc_point[10][10];
    //int[] map = new int[10];
    //int[][] map2 = new int[10][10];
    //myc_point[][] point = new myc_point[100][100];
    myc_point[][] point;
    //myc_point[] point2 = new myc_point[10];
    //myc_point point2[1] = new myc_point();
    
    myc_map(int szx,int szy ){
        point = new myc_point[szx][szy];
    }
    myc_map(){
        point = new myc_point[10][10];
    }
}
