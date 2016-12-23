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
public class myc_bakt {
    int energy;
    String ID;
    int lifetime;
    String DNK;
    int posx,posy;
    myc_bakt(int x, int y,int enrg, myc_map map){
       this.energy=enrg;
       this.lifetime=0;
       map.point[x][y].bakteri=this.ID;
    }
}
