/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myP;
import java.util.*;
/**
 *
 * @author azhidkov
 */
public class myc_bakt {
    int energy; //энергия бактреии
    int ID; //имя бактерии
    int lifetime; // время жизни бактерии
    List DNK;// = new ArrayList(); // Набор команд бактерии
    int posx,posy; // позиционирование на карте
    //int uron; //цена единицы жизни бактерии, зависит от условии и способности
    boolean light; //способность поглащения света
    boolean chem;//способность поглащения химии
    boolean sugar;//способность поглащения сахара/бактерии
    boolean acid;//способнсоть противостоять кислоте
    boolean acidpush;//способность выделять кислоту
    boolean lungs;//наличие легких
    boolean gills;//наличие жабр
    char rotate;// вращение бактерии(где находится морда) 0-вверх,1-вправо,2-вниз,3-влево
    int taxground=5;//штраф за перемещение без способности
    int taxice=5;//штраф за перемещение по льду
    int taxeat=5;//штраф за поглощение без способности
    List Sensor; //список датчиков
    public void myc_bakt(int x, int y,int enrg, myc_map map){
       this.energy=enrg;
       this.lifetime=0;
       this.posx=x;
       this.posy=y;
       this.rotate=1;
       map.point[x][y].bakteri=this.ID;
       DNK = new ArrayList();
       Sensor = new ArrayList();
    }
    
    void work(myc_map map){
        int uron=1; //за обращение
        int abilities=0; //способности
        if(this.acid)abilities++; //увеличиваем цену за количество навыков
        if(this.acidpush)abilities++;
        if(this.chem)abilities++;
        if(this.light)abilities++;
        if(this.lungs)abilities++;
        if(this.gills)abilities++;
        if(this.sugar)abilities++;
        uron+=Math.pow(abilities, 3);//1,8,27,64,125,216,343
        System.out.println("uron= "+uron);
        char[] kodon = new char[6];
        this.lifetime++;//живем циклы.
        //Выполняем программу записанную в ДНК
        for(int i=0;i<this.DNK.size();i++){
            //чтение кодона
            kodon = (char[])DNK.get(i);
            
            switch(kodon[0]){
                case 0://MOVE d G //программа передвижения
                    //this.prisemove(map, i, i)
                    //System.out.println("uron= "+this.move(map, kodon[1]));
                    this.energy-=this.move(map, kodon[1]);//перешли в новую точку
                    //System.out.println("uron= "+this.move(map, kodon[1]));
                    //System.out.println("Kodon прочитался!!"+i);
                    if(kodon[2]>i)i=kodon[2]; //переходим к следующему кодону
                    
                    //System.out.println(this.energy);
                    break;
                case 3://RT r G
                    this.energy-=this.prisemove(map, this.posx, this.posx);
                    switch(kodon[1]){//0-вправо 1-влево 2-развернуться
                        case 0:
                            if(this.rotate==3)this.rotate=0;
                            else this.rotate++;
                        case 1:
                            if(this.rotate==0)this.rotate=3;
                            else this.rotate--;
                        case 2:
                            if(this.rotate==0)this.rotate=2;
                            if(this.rotate==1)this.rotate=3;
                            if(this.rotate==2)this.rotate=0;
                            if(this.rotate==3)this.rotate=1;
                    }
                    break;
            }
            
        }
        if(map.point[map.safeX(this.posx)][this.posy].bakteri>0){ //мы стоим на другой бактерии и не пытаемся её поглотить, то умираем
            this.energy=0;
        }else{
            map.point[map.safeX(this.posx)][this.posy].bakteri=this.ID; //теперь мы стоим в этой точке
        }
        this.energy-=uron;
    }
    
    int prisemove(myc_map map, int x, int y){
        int uron=1;//за обращение -1
        int dist=0;//сколько бежим
        int dx=x-this.posx;
        int dy=y-this.posy;
        if(this.posx!=x|this.posy!=y){//если бежим
            dist=(int)Math.sqrt(dx*dx+dy*dy);
        }
        //System.out.println("dist= "+dist);
        switch(map.getground(this.posx, this.posy)){//смотрим где стоим
            case 0: //стоим в снегу
                uron+=this.taxice;
                if(!this.lungs)uron+=this.taxground;//нет легких   
                if(dist>0)uron+=2*dist;
                break;
            case 1://стоим на земле
                if(!this.lungs)uron+=this.taxground;//нет легких
                if(dist>0)uron+=dist;
                break;
            case 2: //находимся в воде
                if(!this.gills)uron+=this.taxground;//нет жабр
                if(dist>0)uron+=dist;
                break;
        }   
        if(map.getobj(this.posx, this.posy)[0]==4){//если стоим в кислоте
            if(!this.acid){ //стоим в кислоте и не можем ей противостоять
                uron+=map.getobj(this.posx, this.posy)[1];
                map.setobj(this.posx, this.posy, 0, 0);//убрали кислоту
            }
        }
        //System.out.println("uron= "+uron);
        return uron;
        
    }
    
    int priseeat(myc_map map){
        int uron=5; //за обращение
        switch(map.getobj(this.posx, this.posy)[0]){
            case 1: //свет
                if(!this.light)uron+=this.taxeat; //пытался сожрать, то чего не может переварить
            case 2: //химия
                if(!this.chem)uron+=this.taxeat;
            case 3: //cахар
                if(!this.sugar)uron+=this.taxeat;
        }
        return uron;
    }
    int move(myc_map map, char dist){//перемещаемся
        int x=this.posx,y=this.posy;
        map.point[this.posx][this.posy].bakteri=0;//уходим с теущей клетки
        
        switch(this.rotate){//вращение бактерии(где находится морда) 0-вверх,1-вправо,2-влево,3-вниз
            case 0:
                y-=dist;
                break;
            case 1:
                x+=dist;
                //System.out.println("Karl x "+x);
                break;
            case 2:
                x-=dist;
                break;
            case 3:
                y+=dist;
                break;
        }
        int uron = this.prisemove(map, map.safeX(x), map.safeY(y));
        //System.out.println("uron= "+uron);
        this.posx=map.safeX(x);
        //System.out.println("Karl sx "+map.safeX(x));
        this.posy=map.safeY(y);//ушли в новую точку
        return uron;
    }
    
    
}
