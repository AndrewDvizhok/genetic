/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myP;
import java.util.Random;
/**
 *
 * @author azhidkov
 */
public class myc_map {
    myc_point[][] point;
    int sizex,sizey;
    int summLIGHT;
    myc_map(int szx,int szy ){ //
        point = new myc_point[szx][szy];
        for(int i=0; i<szx; i++){
            for (int j=0;j<szy;j++){
                point[i][j]=new myc_point();
            }
        }
        this.sizex=szx;
        this.sizey=szy;
    }
    myc_map(){
        point = new myc_point[256][256];
        for(int i=0; i<256; i++){
            for (int j=0;j<256;j++){
                point[i][j]=new myc_point();
            }
        }
        this.sizex=256;
        this.sizey=256;
    }
    void surface(int segm){
        Random random = new Random();
        int xsegm,ysegm,x,y,i,j;
        xsegm=sizex/segm;
        ysegm=sizey/segm;
        //this.point[1][1].typeground=1;
        for(i=0;i<segm;i++){
            for(j=0;j<segm;j++){
                int tg;
                if(random.nextInt(10)>8)tg=0;
                else tg=1+random.nextInt(3);
                for(x=i*xsegm;x<((i+1)*xsegm);x++){
                    for(y=j*ysegm;y<((j+1)*ysegm);y++){
                        this.point[x][y].typeground=tg;
                    }
                }
            }
        }
        
    }
    int sollar(int energy){
        Random random = new Random();
        for(int i=0; i<this.sizex; i++){
            for (int j=0;j<this.sizey;j++){
                if (random.nextInt(energy+2)>energy){
                    point[i][j].typeobj=1;
                    point[i][j].volume=random.nextInt(30)+1;
                    this.summLIGHT+=point[i][j].volume;
                }
            }
        }
        return this.summLIGHT;
    }
    
    
}
