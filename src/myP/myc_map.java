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
    myc_map(int szx,int szy ){ //
        point = new myc_point[szx][szy];
        this.sizex=szx;
        this.sizey=szy;
    }
    myc_map(){
        point = new myc_point[256][256];
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
                int tg=random.nextInt(4);
                for(x=i*xsegm;x<((i+1)*xsegm);x++){
                    for(y=j*ysegm;y<((j+1)*ysegm);y++){
                        this.point[x][y].typeground=tg;
                    }
                }
            }
        }
    }
    
    
}
