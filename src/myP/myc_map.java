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
public final class myc_map {
    myc_point[][] point;
    int sizex,sizey;
    int summLIGHT;
    int summCHEM;
    int summSUGAR;
    int summGROUND;
    int summICE;
    int summWATER;
    myc_map(int szx,int szy ){ //
        point = new myc_point[szx][szy];
        for(int i=0; i<szx; i++){
            for (int j=0;j<szy;j++){
                point[i][j]=new myc_point();
                this.setground(i, j, -1);//пока почва не определена
                this.setbakt(i,j,-1);//бактерий нет
                //this.
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
                this.setground(i, j, -1);//пока почва не определена
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
                else tg=1+random.nextInt(2);
                for(x=i*xsegm;x<((i+1)*xsegm);x++){
                    for(y=j*ysegm;y<((j+1)*ysegm);y++){
                        this.setground(x, y, tg);
                    }
                }
            }
        }
        
    }
    void sollar(int energy){//добавляем на карту объеты света - 1
        Random random = new Random();
        for(int i=0; i<this.sizex; i++){
            for (int j=0;j<this.sizey;j++){
                int position=Math.abs(this.sizey/2-j);
                if (random.nextInt(position+50)>(position+48)){
                //if (random.nextInt(100)==5){
                    point[i][j].typeobj=1; //1-свет
                    point[i][j].volume=random.nextInt(energy)+1;
                    this.summLIGHT+=point[i][j].volume;
                }
            }
        }
    }
    void sugar(int energy){//добавляем на карту сахар - тип 3 и химию тип 2
        Random random = new Random();
        for(int n=0; n<=energy; n++){
            int x=random.nextInt(this.sizex-this.sizex/18);
            int xmax=x+this.sizex/20;
            int y=random.nextInt(this.sizey-this.sizex/18);
            int ymax=y+this.sizey/20;
            int ni,nj;
            for(int i=x;i<xmax;i++){
                for(int j=y;j<ymax;j++){
                    if (random.nextInt(20)>10){
                        point[i][j].typeobj=2; //2-химия
                        point[i][j].volume=random.nextInt(10)+1;
                        this.summCHEM+=point[i][j].volume;
                    }
                    if (random.nextInt(20)>(18)){
                    point[i][j].typeobj=3; //3-сахар
                    point[i][j].volume=random.nextInt(30)+30;
                    this.summSUGAR+=point[i][j].volume;
                    }
                }
            }
        }
    }
        void remap(int intens, int tg){
        Random random = new Random();
        for(int k=0; k<10+intens; k++){
        for(int i=0; i<this.sizex; i++){
            if(random.nextInt(1000)==5)this.setground(i, random.nextInt(this.sizey), tg);
            for (int j=0;j<this.sizey;j++){
                if(random.nextInt(100)<5){
                        int r,l,t,b;
                        r=this.getground(i+1, j);
                        l=this.getground(i-1, j);
                        t=this.getground(i, j-1);
                        b=this.getground(i, j+1);
                        if(r==tg|l==tg|t==tg|b==tg)this.setground(i, j, tg);                  
                }
                
            }
        }
        }
    }
        
    void freeze(int intens){
        this.remap(intens, 0);
    }
    void grounding(int intens){
        this.remap(intens, 1);
    }
    void water(int lengt){
        Random random = new Random();
        for(int i=0; i<this.sizex; i++){
            for (int j=0;j<this.sizey;j++){
                if(random.nextInt(300)<1){
                    if(this.getground(i, j)==2)
                    this.river(i, j, random.nextInt(lengt), 2);
                }
            }
        }
        this.remap(10, 2);
    }
    void river(int x, int y,int length,int tg){
        Random random = new Random();
        int oldx=x,oldy=y;
        for(int k=0;k<length;k++){
            int napr=random.nextInt(8);
            x=oldx; y=oldy;
        for(int i=0; i<8;i++){
            switch(napr){
                case 0:
                    this.setground(x+i, y+i, tg);
                    oldx=x+i; oldy=y+i;
                    break;
                case 1:
                    this.setground(x-i, y-i, tg);
                    oldx=x-i; oldy=y-i;
                    break;
                case 2:
                    this.setground(x-i, y+i, tg);
                    oldx=x-i; oldy=y+i;
                    break;
                case 3:
                    this.setground(x+i, y-i, tg);
                    oldx=x+i; oldy=y-i;
                    break;
                case 4:
                    this.setground(x+i, y, tg);
                    oldx=x+i; oldy=y;
                    break;
                case 5:
                    this.setground(x-i, y, tg);
                    oldx=x-i; oldy=y;
                    break;
                case 6:
                    this.setground(x, y+i, tg);
                    oldx=x; oldy=y+i;
                    break;
                case 7:
                    this.setground(x, y-i, tg);
                    oldx=x; oldy=y-i;
                    break;
                 
            }
        }
        }
    }
    void setground(int x, int y,int tg){ //безопасно устанавливает тип гурнта
        if(x>=this.sizex)x-=this.sizex;
        if(y>=this.sizey)y-=this.sizey;
        if(x<0)x+=this.sizex;
        if(y<0)y+=this.sizey;
        switch(tg){ //подсчитаем количество типов поверхности
            case 0:
                if(this.getground(x, y)==1){this.summGROUND--;this.summICE++;} //меньше земли больше льда
                if(this.getground(x, y)==2){this.summWATER--;this.summICE++;}//меншье воды больше льда
                if(this.getground(x, y)==-1){this.summICE++;} // если каким-то чудом пропустили ячейку
                break;
            case 1:
                if(this.getground(x, y)==0){this.summGROUND++;this.summICE--;} //меньше льда больше земли
                if(this.getground(x, y)==2){this.summWATER--;this.summGROUND++;}//меншье воды больше земли
                if(this.getground(x, y)==-1){this.summGROUND++;} // если каким-то чудом пропустили ячейку
                break;
            case 2:
                if(this.getground(x, y)==1){this.summGROUND--;this.summWATER++;} //меньше земли больше воды
                if(this.getground(x, y)==0){this.summICE--;this.summWATER++;}//меншье льда больше воды
                if(this.getground(x, y)==-1){this.summWATER++;} // если каким-то чудом пропустили ячейку
                break;
        }
        this.point[x][y].typeground=tg;
    }
    int getground(int x, int y){ // проверяем тип поверхности по координатам
        return this.point[this.safeX(x)][this.safeY(y)].typeground;
    }
    
    int[] getobj(int x, int y){ // проверяем объект,0-тип обеъкта, 1-объем объекта
        int [] obj = new int[2];
        if(x>=this.sizex)x-=this.sizex;
        if(y>=this.sizey)y-=this.sizey;
        if(x<0)x+=this.sizex;
        if(y<0)y+=this.sizey;
        obj[0]=this.point[this.safeX(x)][this.safeY(y)].typeobj;
        obj[1]=this.point[this.safeX(x)][this.safeY(y)].volume;
        return obj;
    }
  
    void setobj(int x, int y,int to,int vlm){ //безопасно устанавливает тип объекта
        this.point[this.safeX(x)][this.safeY(y)].typeobj=to;
        this.point[this.safeX(x)][this.safeY(y)].volume=vlm;
    }
    int getbakt(int x, int y){ //возвращает инфу о бактерии 
        return this.point[this.safeX(x)][this.safeY(y)].bakteri;
    }
    void setbakt(int x, int y, int id){
        this.point[this.safeX(x)][this.safeY(y)].bakteri=id;
    }
    int safeX(int x){
        if(x>=this.sizex)x-=this.sizex;
        if(x<0)x+=this.sizex;
        return x;
    }
    int safeY(int x){
        if(x>=this.sizey)x-=this.sizey;
        if(x<0)x+=this.sizey;
        return x;
    }
}
