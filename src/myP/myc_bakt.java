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
       map.setbakt(this.posx, this.posy, this.ID);//map.point[x][y].bakteri=this.ID;
       DNK = new ArrayList();
       Sensor = new ArrayList();
    }
    
    void work(myc_map map, myc_bakt[] bakt){
        Random random = new Random();
        int uron=1; //за обращение
        int abilities=0; //способности
        if(this.acid)abilities++; //увеличиваем цену за количество навыков
        if(this.acidpush)abilities++;
        if(this.chem)abilities++;
        if(this.light)abilities++;
        if(this.lungs)abilities++;
        if(this.gills)abilities++;
        if(this.sugar)abilities++;
        uron+=Math.pow(abilities, 2);//1,8,27,64,125,216,343//1,4,9,16,25,36,49
        //System.out.println("uron= "+uron);
        uron+=this.Sensor.size();//плата за сенсоры
        
        char[] kodon = new char[6];
        this.lifetime++;//живем циклы.
        //Выполняем программу записанную в ДНК
        for(int i=0;i<this.DNK.size();i++){ ///////// Чтение ДНК
            //чтение кодона
            kodon = (char[])this.DNK.get(i);
            
            switch(kodon[0]){//===================================== Чтение кодона =============
                case 0://MOVE d G //программа передвижения=====================================
                    if(map.getbakt(this.posx, this.posy)==this.ID){//если мы стоим на этой точке
                        map.setbakt(this.posx, this.posy, -1);//уходим с точки
                    } 
                    this.energy-=this.move(map, kodon[1]);//перешли в новую точку
                    if(kodon[2]>i)i=kodon[2]; //переходим к следующему кодону
                    break;
                case 1: //MOVES xy G перемещение на датчик xy ================================
                    if(map.getbakt(this.posx, this.posy)==this.ID){//если мы стоим на этой точке
                        map.setbakt(this.posx, this.posy, -1);//уходим с точки
                    }
                    int[] arr = new int[2];
                    arr=(int[])this.Sensor.get(kodon[1]);
                    this.energy-=this.moves(map, arr[0], arr[1]);
                    //System.out.println("kodon1:"+kodon[1]+"; 2:"+kodon[2]);
                    if(kodon[2]>i)i=kodon[2]; //переходим к следующему кодону
                    break;
                case 2: //EAT G попытка съесть объект ========================================
                    
                    switch(map.getobj(this.posx, this.posy)[0]){//проверяем что за объект мы пытаемся поглотить
                        case 1://свет
                            if(this.light){
                                System.out.println("EAT "+map.getobj(this.posx, this.posy)[1]);
                                this.energy+=map.getobj(this.posx, this.posy)[1];//забираем энергию
                                map.setobj(this.posx, this.posy, 0, 0);//опустошаем точку
                                
                            }
                            break;
                        case 2://химия
                            if(this.chem){
                                this.energy+=map.getobj(this.posx, this.posy)[1];//забираем энергию
                                map.setobj(this.posx, this.posy, 0, 0);//опустошаем точку
                            }
                            break;
                        case 3: // сахар
                            if(this.sugar){
                                this.energy+=map.getobj(this.posx, this.posy)[1];//забираем энергию
                                map.setobj(this.posx, this.posy, 0, 0);//опустошаем точку
                            }
                            break;
                    }                 
                    if((this.sugar)&(map.getbakt(this.posx, this.posy)>-1)&(map.getbakt(this.posx, this.posy)!=this.ID)){
                    //способны поглотить бактерию, под нами есть бактерия и это не мы
                        this.energy+=bakt[map.getbakt(this.posx, this.posy)].energy; //забрали энергию
                        bakt[map.getbakt(this.posx, this.posy)].energy=0;//убили бактерию
                        map.setbakt(this.posx, this.posy, this.ID);//встаем на ёё место
                    }
                    this.energy-=this.taxeat;
                    if(kodon[1]>i)i=kodon[1]; //переходим к следующему кодону
                    break;
                case 3://RT r G //программа поворота ==========================================
                    this.energy-=this.prisemove(map, this.posx, this.posx);
                    //System.out.println("kodon ok= "+(int)kodon[1]);
                    switch(kodon[1]){//0-вправо 1-влево 2-развернуться
                        case 0:
                            if(this.rotate==3)this.rotate=0;
                            else this.rotate++;
                            break;
                        case 1:
                            if(this.rotate==0)this.rotate=3;
                            else this.rotate--;
                            break;
                        case 2:
                            if(this.rotate==0)this.rotate=2;
                            if(this.rotate==1)this.rotate=3;
                            if(this.rotate==2)this.rotate=0;
                            if(this.rotate==3)this.rotate=1;
                            break;
                    }
                    if(kodon[2]>i)i=kodon[2]; //переходим к следующему кодону
                    break;
                case 4: // CLONE e m G программа порождения потомка ==============================
                    //вращение бактерии(где находится морда) 0-вверх,1-вправо,2-вниз,3-влево   
                    switch(this.rotate){//в зависимости от разворота, откладываем клона позади себя если там пусто
                        case 0:
                            if(map.getbakt(this.posx, this.posy+1)==-1){
                                this.cloner(this.posx, this.posy+1,kodon[1],kodon[2],bakt,map);
                            }                            
                            break;
                        case 1:
                            if(map.getbakt(this.posx-1, this.posy)==-1){
                                this.cloner(this.posx-1, this.posy,kodon[1],kodon[2],bakt,map);
                            }
                            break;
                        case 2:
                            if(map.getbakt(this.posx, this.posy-1)==-1){
                                this.cloner(this.posx, this.posy-1,kodon[1],kodon[2],bakt,map);
                            }
                            break;
                        case 3:
                            if(map.getbakt(this.posx+1, this.posy)==-1){
                                this.cloner(this.posx+1, this.posy,kodon[1],kodon[2],bakt,map);
                            };
                            break;
                    }
                    break;
            }
            
        }
        if((map.getbakt(this.posx, this.posy)>-1)&(map.getbakt(this.posx, this.posy)!=this.ID)){ //мы стоим на другой бактерии и не пытаемся её поглотить, то умираем
            this.energy=0;//умираем
        }else{
            map.setbakt(this.posx, this.posy, this.ID); //теперь мы стоим в этой точке
        }
        this.energy-=uron;
    }
    
    int prisemove(myc_map map, int x, int y){//====================================
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
    
    int move(myc_map map, char dist){//перемещаемся===============================================
        int x=this.posx,y=this.posy;
        map.point[this.posx][this.posy].bakteri=0;//уходим с теущей клетки
        
        switch(this.rotate){//вращение бактерии(где находится морда) 0-вверх,1-вправо,2-вниз,3-влев
            case 0:
                y-=dist;
                break;
            case 1:
                x+=dist;
                //System.out.println("Karl x "+x);
                break;
            case 2:
                y+=dist;
                break;
            case 3:
                x-=dist;
                break;
        }
        int uron = this.prisemove(map, map.safeX(x), map.safeY(y));
        //System.out.println("uron= "+uron);
        this.posx=map.safeX(x);
        //System.out.println("Karl sx "+map.safeX(x));
        this.posy=map.safeY(y);//ушли в новую точку
        return uron;
    }
    int moves(myc_map map, int x,int y){//перемеаститься на датчик xy ============================
        map.point[this.posx][this.posy].bakteri=0;//уходим с теущей клетки
        //x-=20; y-=20; //преобразуем координаты
        int nx=x,ny=y;
        switch(this.rotate){//вращение бактерии(где находится морда) 0-вверх,1-вправо,2-вниз,3-влев
            case 0:
                nx+=this.posx;
                ny+=this.posy;//перешли к абсолютным координатам
                break;
            case 1://повернули вправо
                nx=-y;//rotated_point.x = point.x * cos(angle) - point.y * sin(angle);
                ny=x;//rotated_point.y = point.x * sin(angle) + point.y * cos(angle);
                nx+=this.posx;//перешли к абсолютным координатам
                ny+=this.posy;
                break;
            case 2:
                nx=-x;
                ny=-y;
                nx+=this.posx;//перешли к абсолютным координатам
                ny+=this.posy;
                break;
            case 3:
                nx=y;
                ny=-x;
                nx+=this.posx;//перешли к абсолютным координатам
                ny+=this.posy;
                break;
        }
        
        this.posx=map.safeX(nx);
        this.posy=map.safeY(ny);
        int uron = this.prisemove(map, map.safeX(nx), map.safeY(ny));
        return uron;
    }
    
    void cloner(int x, int y, int mutation, int enrg, myc_bakt bakt[], myc_map map){ //создает копию себя ======================================
        int minlife=bakt[0].lifetime;
        int mini=0;
        for(int i=0;i<bakt.length;i++){// ищем среди бактерии мертвую и делаем её своим потомком, или убиваем самую менее живучую
            if(bakt[i].lifetime<minlife){minlife=bakt[i].lifetime; mini=i;}
            if(bakt[i].energy==0){mini=i; break;}
        }
        bakt[mini].energy=this.energy*enrg/10;//передаем часть энергии
        this.energy-=bakt[mini].energy;
        bakt[mini].lifetime=this.lifetime;//наследует нашу продолжительност жизни
        if(mutation>0){
           // тут надо добавить код мутации 
        }else{// без мутации, точная копия.
            bakt[mini].DNK=this.DNK;
            bakt[mini].Sensor=this.Sensor;
            bakt[mini].acid=this.acid;
            bakt[mini].acidpush=this.acidpush;
            bakt[mini].chem=this.chem;
            bakt[mini].gills=this.gills;
            bakt[mini].light=this.light;
            bakt[mini].lungs=this.lungs;
            bakt[mini].rotate=this.rotate;
            bakt[mini].sugar=this.sugar;
        }
        
        map.setbakt(bakt[mini].posx, bakt[mini].posy, -1);//убираем бактерию с точки
        map.setbakt(x, y, mini);//указываем карте, что теперь в этой клетке стоим мы
        bakt[mini].posx=x;//перемещаем бактерию
        bakt[mini].posy=y;
    }
    
    void mutation(List fDNK, List DNK, int mutation){// мутирование ДНК бактерии удаление(-1), 0  изменение, 1 добавление
        Random random=new Random ();
        DNK.clear();//очистим вторую ДНК
        for(int i=0; i<fDNK.size(); i++){//клонируем днк
            //DNK.add((int[])fDNK.get(i));
        }
        switch(mutation){
            case -1://удаляем случайный кодон
                int delr=random.nextInt(DNK.size());
                int[] dnk = new int [6];
                for(int i=delr; i<DNK.size(); i++){//подправим оставшуюся цепь ДНК для коррекции ссылок
                    dnk = (int[])DNK.get(i);
                    switch(dnk[0]){
                        case 0: case 1: case 3: 
                            if(dnk[2]>delr)dnk[2]--;
                            break;
                        case 2:
                            if(dnk[1]>delr)dnk[1]--;
                            break;
                        case 4: case 5:
                            if(dnk[3]>delr)dnk[3]--;
                            break;
                        case 7: case 8:
                            if(dnk[3]>delr)dnk[3]--;
                            if(dnk[4]>delr)dnk[4]--;
                            break;
                        case 9:
                            if(dnk[4]>delr)dnk[4]--;
                            if(dnk[5]>delr)dnk[5]--;
                            break;
                    }
                    //DNK
                }
                break;
        }
        
    }
    
    
    
    
}
