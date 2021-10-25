import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;
import java.util.Random;

/*
完成数据处理
 */

class ReadLesson extends Stage
{
    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    private ObservableList<LessonTabPane.Lesson> LessData = FXCollections.observableArrayList();
    private final ObservableList<Time> LessTime = FXCollections.observableArrayList();
    private final ObservableList<Time> LessTimeLast = FXCollections.observableArrayList();
    private final ObservableList<oneLesson> lessons  = FXCollections.observableArrayList();

    Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
    Statement stmt=conn.createStatement();

    public static class Time
    {
        private final SimpleStringProperty day;
        private final SimpleStringProperty less1;
        private final SimpleStringProperty less2;
        private final SimpleStringProperty less3;
        private final SimpleStringProperty less4;
        private final SimpleStringProperty less5;
        private final SimpleStringProperty less6;
        private final SimpleStringProperty less7;
        private final SimpleStringProperty less8;
        private final SimpleStringProperty less9;
        private final SimpleStringProperty less10;
        private SimpleStringProperty[] lessList = new SimpleStringProperty[10];


        private Time(String day)
        {
            this.day = new SimpleStringProperty(day);
            this.less1 = new SimpleStringProperty("");
            this.less2 = new SimpleStringProperty("");
            this.less3 = new SimpleStringProperty("");
            this.less4 = new SimpleStringProperty("");
            this.less5 = new SimpleStringProperty("");
            this.less6 = new SimpleStringProperty("");
            this.less7 = new SimpleStringProperty("");
            this.less8 = new SimpleStringProperty("");
            this.less9 = new SimpleStringProperty("");
            this.less10 = new SimpleStringProperty("");
            updateList();
        }
        Time(String day, String less1, String less2, String less3, String less4, String less5, String less6, String less7, String less8, String less9, String less10)
        {   this.day = new SimpleStringProperty(day);
            this.less1 = new SimpleStringProperty(less1);
            this.less2 = new SimpleStringProperty(less2);
            this.less3 = new SimpleStringProperty(less3);
            this.less4 = new SimpleStringProperty(less4);
            this.less5 = new SimpleStringProperty(less5);
            this.less6 = new SimpleStringProperty(less6);
            this.less7 = new SimpleStringProperty(less7);
            this.less8 = new SimpleStringProperty(less8);
            this.less9 = new SimpleStringProperty(less9);
            this.less10 = new SimpleStringProperty(less10);
            updateList();
        }
        public SimpleStringProperty[] getDayList()
        {   return lessList;
        }
        public void updateList() {
            lessList[0]=less1;
            lessList[1]=less2;
            lessList[2]=less3;
            lessList[3]=less4;
            lessList[4]=less5;
            lessList[5]=less6;
            lessList[6]=less7;
            lessList[7]=less8;
            lessList[8]=less9;
            lessList[9]=less10;
        }

        public String getDay()
        {   return day.get();
        }

        public void setDay(String d)
        {   day.set(d);
        }

        public String getLess1()
        {   return less1.get();
        }

        public void setLess1(String less)
        {   less1.set(less);
        }

        public String getLess2()
        {   return less2.get();
        }

        public void setLess2(String less)
        {   less2.set(less);
        }

        public void setLess3(String less)
        {   less3.set(less);
        }

        public String getLess3()
        {   return less3.get();
        }

        public void setLess4(String less)
        {   less4.set(less);
        }

        public String getLess4()
        {   return less4.get();
        }

        public void setLess5(String less)
        {   less5.set(less);
        }

        public String getLess5()
        {   return less5.get();
        }

        public void setLess6(String less)
        {   less6.set(less);
        }

        public String getLess6()
        {   return less6.get();
        }

        public void setLess7(String less)
        {   less7.set(less);
        }

        public String getLess7()
        {   return less7.get();
        }

        public void setLess8(String less)
        {   less8.set(less);
        }

        public String getLess8()
        {   return less8.get();
        }

        public void setLess9(String less)
        {   less9.set(less);
        }

        public String getLess9()
        {   return less9.get();
        }

        public void setLess10(String less)
        {   less10.set(less);
        }

        public String getLess10()
        {   return less10.get();
        }
    }

    public static class oneLesson
    {
        String name;
        int chour;

        oneLesson(String name, int chour)
        {
            this.name = name;
            this.chour = chour;
        }

        String getName()
        {
            return name;
        }
        int getChour()
        {
            return chour;
        }
        void setChour(int chour){
            this.chour = chour;
        }

    }

    public ReadLesson(ObservableList<LessonTabPane.Lesson> LessData) throws SQLException {

        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);
        this.LessData = LessData;


        LessonTabPane.Lesson up;
        int allChour = 0;


        LessTime.addAll(new Time("1"), new Time("2"), new Time("3"), new Time("4"), new Time("5"));
        LessTimeLast.addAll(new Time("1"), new Time("2"), new Time("3"), new Time("4"), new Time("5"));

        Time oneday;
        SimpleStringProperty[] d1, d2, d3, d4, d5;
        SimpleStringProperty[] dl1, dl2, dl3, dl4, dl5;
        Time dayl1, dayl2, dayl3, dayl4, dayl5;
        String day, less1, less2, less3, less4, less5, less6, less7, less8, less9, less10;

        for (int i = 0; i < LessData.size(); i++) {
            up = (LessonTabPane.Lesson) LessData.get(i);
            allChour += up.getChour();
            lessons.add(new oneLesson(up.getName(), up.getChour()));
        }
        if (allChour > 50) {
            CommonDialog.WarningDialog("课程过多, 请查看学分设置是否出错 !");
        }
        //（1）	两节课连上；
        //（2）	优先白天排课；
        //（3）	优先上半学期排课；
        //（4）	课程间隔48小时；
        //（5）	课程分布较为平均分散。
        else {
            oneLesson less;
            int chour, frequency;
            String name;
            // 一次循环处理一类课
            for (int i = 0; i < lessons.size(); i++) {
                less = lessons.get(i);
                name = less.getName();
                chour = less.getChour();
                if (chour % 2 != 0)
                    frequency = (int) chour / 2 + 1;
                else
                    frequency = (int) chour / 2;
                // 一次循环出处理一天
                boolean b;
                for (int j = 0; j < 5 && frequency > 0; j++) {
//                    if (allChour < 20 && (j==2 || j==4)){
//                        int max=5,min=0;
//                        j = (int) (Math.random()*(max-min)+min);
//                    }else if  (allChour < 20 && (i!=0 && j==0)){
//                        int max=5,min=1;
//                        j = (int) (Math.random()*(max-min)+min);
//                    }
                    oneday = LessTime.get(j);
                    b = false;
                    if (oneday.getLess1().equals("") && oneday.getLess2().equals("")) {
                        oneday.setLess1(name);
                        oneday.setLess2(name);
                        frequency--;
                        b = true;
                        less.setChour(less.getChour()-1);

                    } else if (oneday.getLess3().equals("") && oneday.getLess4().equals("")) {
                        oneday.setLess3(name);
                        oneday.setLess4(name);
                        frequency--;
                        b = true;
                        less.setChour(less.getChour()-1);

                    } else if (oneday.getLess5().equals("") && oneday.getLess6().equals("")) {
                        oneday.setLess5(name);
                        oneday.setLess6(name);
                        frequency--;
                        b = true;
                        less.setChour(less.getChour()-1);

                    } else if (oneday.getLess7().equals("") && oneday.getLess8().equals("")) {
                        oneday.setLess7(name);
                        oneday.setLess8(name);
                        frequency--;
                        b = true;
                        less.setChour(less.getChour()-1);

                    }
                    if (frequency!=0 && b){
                        j++;
                    }

                }

                // 晚课
                if (frequency != 0){
                    for (int j = 0; j < 5 && frequency > 0; j++) {
                        oneday = LessTime.get(j);
                        b = false;
                        if (oneday.getLess9().equals("") && oneday.getLess10().equals("")) {
                            oneday.setLess9(name);
                            oneday.setLess10(name);
                            frequency--;
                            b = true;
                            less.setChour(less.getChour()-1);

                        }
                        if (frequency!=0 && b){
                            j++;
                        }
                    }
                }

            }

            for (int i = 0; i < lessons.size(); i++) {
                less = lessons.get(i);
                name = less.getName();
                chour = less.getChour();
                frequency = chour;

                // 一次循环出处理一天
                boolean b;
                for (int j = 0; j < 5 && frequency > 0; j++) {
//                    if (allChour < 20 && (j==2 || j==4)){
//                        int max=5,min=0;
//                        j = (int) (Math.random()*(max-min)+min);
//                    }else if  (allChour < 20 && (i!=0 && j==0)){
//                        int max=5,min=1;
//                        j = (int) (Math.random()*(max-min)+min);
//                    }
                    oneday = LessTimeLast.get(j);
                    b = false;
                    if (oneday.getLess1().equals("") && oneday.getLess2().equals("")) {
                        oneday.setLess1(name);
                        oneday.setLess2(name);
                        frequency--;
                        b = true;
                        less.setChour(less.getChour()-1);

                    } else if (oneday.getLess3().equals("") && oneday.getLess4().equals("")) {
                        oneday.setLess3(name);
                        oneday.setLess4(name);
                        frequency--;
                        b = true;
                        less.setChour(less.getChour()-1);

                    } else if (oneday.getLess5().equals("") && oneday.getLess6().equals("")) {
                        oneday.setLess5(name);
                        oneday.setLess6(name);
                        frequency--;
                        b = true;
                        less.setChour(less.getChour()-1);

                    } else if (oneday.getLess7().equals("") && oneday.getLess8().equals("")) {
                        oneday.setLess7(name);
                        oneday.setLess8(name);
                        frequency--;
                        b = true;
                        less.setChour(less.getChour()-1);

                    }
                    if (frequency!=0 && b){
                        j++;
                    }

                }

                // 晚课
                if (frequency != 0){
                    for (int j = 0; j < 5 && frequency > 0; j++) {
                        oneday = LessTimeLast.get(j);
                        b = false;
                        if (oneday.getLess9().equals("") && oneday.getLess10().equals("")) {
                            oneday.setLess9(name);
                            oneday.setLess10(name);
                            frequency--;
                            b = true;
                            less.setChour(less.getChour()-1);

                        }
                        if (frequency!=0 && b){
                            j++;
                        }
                    }
                }


            }

            try {
                String clear = "DELETE * FROM lessonTime";
                stmt.executeUpdate(clear);
                clear = "DELETE * FROM showLessonTime";
                stmt.executeUpdate(clear);
                clear = "DELETE * FROM showLessonTimeLast";
                stmt.executeUpdate(clear);
                clear = "DELETE * FROM lessonTimeLast";
                stmt.executeUpdate(clear);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            d1 = LessTime.get(0).getDayList();
            d2 = LessTime.get(1).getDayList();
            d3 = LessTime.get(2).getDayList();
            d4 = LessTime.get(3).getDayList();
            d5 = LessTime.get(4).getDayList();
            dl1 = LessTimeLast.get(0).getDayList();
            dl2 = LessTimeLast.get(1).getDayList();
            dl3 = LessTimeLast.get(2).getDayList();
            dl4 = LessTimeLast.get(3).getDayList();
            dl5 = LessTimeLast.get(4).getDayList();
            for (int i = 0; i < 10; i++) {
                less1 = d1[i].get();
                less2 = d2[i].get();
                less3 = d3[i].get();
                less4 = d4[i].get();
                less5 = d5[i].get();
                String insertSql = "INSERT INTO showLessonTime(Order, Mon,Tue,Wed,Thu,Fri) VALUES('" +(i+1)
                        + "','" + less1 + "','" + less2 + "','" + less3 + "','" + less4 + "','" + less5 + "')";
                int count = stmt.executeUpdate(insertSql);
                System.out.println(insertSql + "    添加 " + count + " 条记录到 lessonTime 表中");


                less1 = dl1[i].get();
                less2 = dl2[i].get();
                less3 = dl3[i].get();
                less4 = dl4[i].get();
                less5 = dl5[i].get();
                insertSql = "INSERT INTO showLessonTimeLast(Order, Mon,Tue,Wed,Thu,Fri) VALUES('" +(i+1)
                        + "','" + less1 + "','" + less2 + "','" + less3 + "','" + less4 + "','" + less5 + "')";
                count = stmt.executeUpdate(insertSql);
                System.out.println(insertSql + "    添加 " + count + " 条记录到 lessonTime 表中");
            }

            for (int i = 0; i < LessTime.size(); i++) {
                oneday = LessTime.get(i);
                day = oneday.getDay();
                less1 = oneday.getLess1();
                less2 = oneday.getLess2();
                less3 = oneday.getLess3();
                less4 = oneday.getLess4();
                less5 = oneday.getLess5();
                less6 = oneday.getLess6();
                less7 = oneday.getLess7();
                less8 = oneday.getLess8();
                less9 = oneday.getLess9();
                less10 = oneday.getLess10();

                String insertSql = "INSERT INTO lessonTime(tday,l1,l2,l3,l4,l5,l6,l7,l8,l9,l10) VALUES('"
                        + day + "','" + less1 + "','" + less2 + "','" + less3 + "','" + less4 + "','" + less5
                        + "','" + less6 + "','" + less7 + "','" + less8 + "','" + less9 + "','" + less10 + "')";
                int count = stmt.executeUpdate(insertSql);
                System.out.println(insertSql + "    添加 " + count + " 条记录到 lessonTime 表中");

                oneday = LessTimeLast.get(i);
                day = oneday.getDay();
                less1 = oneday.getLess1();
                less2 = oneday.getLess2();
                less3 = oneday.getLess3();
                less4 = oneday.getLess4();
                less5 = oneday.getLess5();
                less6 = oneday.getLess6();
                less7 = oneday.getLess7();
                less8 = oneday.getLess8();
                less9 = oneday.getLess9();
                less10 = oneday.getLess10();

                insertSql = "INSERT INTO lessonTimeLast(tday,l1,l2,l3,l4,l5,l6,l7,l8,l9,l10) VALUES('"
                        + day + "','" + less1 + "','" + less2 + "','" + less3 + "','" + less4 + "','" + less5
                        + "','" + less6 + "','" + less7 + "','" + less8 + "','" + less9 + "','" + less10 + "')";
                count = stmt.executeUpdate(insertSql);
                System.out.println(insertSql + "    添加 " + count + " 条记录到 lessonTime 表中");
            }

            CommonDialog.InformationDialog("更新成功 !");
        }


    }
    public static class ShowTime
    {
        private final SimpleStringProperty order;
        private final SimpleStringProperty less1;
        private final SimpleStringProperty less2;
        private final SimpleStringProperty less3;
        private final SimpleStringProperty less4;
        private final SimpleStringProperty less5;

        ShowTime(String order,String less1, String less2, String less3, String less4, String less5)
        {
            this.order = new SimpleStringProperty(order);
            this.less1 = new SimpleStringProperty(less1);
            this.less2 = new SimpleStringProperty(less2);
            this.less3 = new SimpleStringProperty(less3);
            this.less4 = new SimpleStringProperty(less4);
            this.less5 = new SimpleStringProperty(less5);

        }

        public String getOrder()
        {   return order.get();
        }

        public void setOrder(String less)
        {   order.set(less);
        }

        public String getLess1()
        {   return less1.get();
        }

        public void setLess1(String less)
        {   less1.set(less);
        }

        public String getLess2()
        {   return less2.get();
        }

        public void setLess2(String less)
        {   less2.set(less);
        }

        public void setLess3(String less)
        {   less3.set(less);
        }

        public String getLess3()
        {   return less3.get();
        }

        public void setLess4(String less)
        {   less4.set(less);
        }

        public String getLess4()
        {   return less4.get();
        }

        public void setLess5(String less)
        {   less5.set(less);
        }

        public String getLess5()
        {   return less5.get();
        }
    }

}
