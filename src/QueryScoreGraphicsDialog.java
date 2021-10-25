import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import java.sql.*;
import java.util.*;

class QueryScoreGraphicsDialog extends Stage
{   String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    String queryScoreID, queryScoreName;
    Vector queryLessData = new Vector();
        
    public QueryScoreGraphicsDialog(String uID, String uName) 
    {
        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);
        queryScoreID = uID; queryScoreName = uName;
        loadQueryLesson();    //  从数据库获取当前老师上课列表
        
        TabPane lessTabPane = new TabPane();
        for(int i=0;i<queryLessData.size();i++)
            lessTabPane.getTabs().add(getOneLessonTab((QueryLesson)queryLessData.get(i), i%2));  //  轮流展示饼图和柱状图
        
        Button exitButton = new Button("退出", new ImageView(new Image("image/quit.png")));        
        exitButton.setOnAction(e->{ close(); }); // 关闭选课窗口
               
        Label teacherLab = new Label("（教师："+queryScoreName+"）课程：");
        teacherLab.setFont(new Font("KaiTi", 16));    
        teacherLab.setTextFill(Color.BROWN);
        
                                   
        BorderPane seleBP = new BorderPane();
        seleBP.setTop(teacherLab);
        seleBP.setCenter(lessTabPane);
        seleBP.setBottom(exitButton);
        seleBP.setMargin(teacherLab, new Insets(5, 0, 5, 0));
        seleBP.setMargin(exitButton, new Insets(10, 0, 10, 0));
        seleBP.setAlignment(exitButton, Pos.CENTER);
        
        setTitle("课程质量分析图标");
        setScene(new Scene(seleBP, 560, 500));
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
               
    private Tab getOneLessonTab(QueryLesson ql, int lx)
    {   String lid = ql.uid.trim();
        String sql=" select sum(case when score between 90 and 100 then 1 else 0 end) as A, "+
                           "sum(case when score between 80 and 89 then 1 else 0 end) as B,"+
                           "sum(case when score between 70 and 79 then 1 else 0 end) as C,"+
                           "sum(case when score between 60 and 69 then 1 else 0 end) as D,"+
                           "sum(case when score<60 then 1 else 0 end) as E from score WHERE lesson_id = '"+lid+"'";

        int count_a = 0, count_b = 0, count_c=0, count_d=0, count_e=0;
            
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   count_a = rs.getInt("A");
                count_b = rs.getInt("B");
                count_c = rs.getInt("C");
                count_d = rs.getInt("D");
                count_e = rs.getInt("E");
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }

        Tab oneTab = new Tab();
        oneTab.setText(" "+ql.name+" ");
        oneTab.setClosable(false);
        oneTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");

        BorderPane bp = new BorderPane();
        Label lessInfo = new Label("学分："+ql.chour+"，类型："+ql.type+"，考试方式："+ql.ksfs);
        lessInfo.setTextFill(Color.BLUE);
        bp.setTop(lessInfo);
        bp.setMargin(lessInfo, new Insets(5, 0, 10, 5));
        
        if(lx == 0)  //  饼图展示
        {   ObservableList<Data> answer = FXCollections.observableArrayList();
            answer.addAll(new PieChart.Data("优秀", count_a), new PieChart.Data("良好",count_b),
                          new PieChart.Data("中等",count_c),  new PieChart.Data("及格",count_d),
                          new PieChart.Data("不及格",count_e));
            PieChart pieChart = new PieChart();
            pieChart.setData(answer);
            pieChart.setTitle("成绩分布饼图");
            pieChart.setLegendSide(Side.BOTTOM);
            pieChart.setClockwise(false);
            pieChart.setLabelsVisible(false);
            bp.setCenter(pieChart);
        }
        else      //  柱形图展示
        {   final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setCategories(FXCollections.<String> observableArrayList(Arrays.asList("不及格", "及格", "中等", "良好", "优秀"))); 
            yAxis.setLabel("人数");
            final StackedBarChart<String,Number> stackedBarChart = new StackedBarChart<String,Number>(xAxis,yAxis);
            stackedBarChart.setTitle("成绩柱状分布图");
            
            XYChart.Series<String,Number> series1 = new XYChart.Series();
            series1.getData().add(new XYChart.Data("不及格", count_e));
            series1.setName("不及格");
                
            XYChart.Series<String,Number> series2 = new XYChart.Series();
            series2.setName("及格");
            series2.getData().add(new XYChart.Data("及格", count_d));
        
            XYChart.Series<String,Number> series3 = new XYChart.Series();
            series3.setName("中等");
            series3.getData().add(new XYChart.Data("中等", count_c));
        
            XYChart.Series<String,Number> series4 = new XYChart.Series();
            series4.setName("良好");
            series4.getData().add(new XYChart.Data("良好", count_b));
        
            XYChart.Series<String,Number> series5 = new XYChart.Series();
            series5.setName("优秀");
            series5.getData().add(new XYChart.Data("优秀", count_a));   
        
            stackedBarChart.getData().addAll(series1, series2, series3, series4, series5);
            bp.setCenter(stackedBarChart);
        }   
             
        oneTab.setContent(bp);
        return oneTab;
    }
    
    private void loadQueryLesson()
    {   String sql="SELECT uid, name, chour, type, ksfs FROM lesson WHERE teacher='"+queryScoreName+"'";
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            queryLessData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid").trim();
                String name=rs.getString("name");
                int chour=rs.getInt("chour");
                String type=rs.getString("type");
                String ksfs=rs.getString("ksfs");
                queryLessData.add(new QueryLesson(no, name, new Integer(chour), type, ksfs));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public static class QueryLesson
    {   private final String uid;
    	private final String name;
        private final int chour;
        private final String type;
        private final String ksfs;
        
        public QueryLesson(String uUid, String uName, int uChour, String uType, String uKsfs) 
        {   this.uid = uUid;
            this.name = uName;
            this.chour = uChour;
            this.type = uType;
            this.ksfs = uKsfs;
        }        
    }
}


