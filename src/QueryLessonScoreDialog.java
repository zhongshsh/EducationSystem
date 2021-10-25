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
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;
import java.sql.*;
import java.util.Arrays;


class QueryLessonScoreDialog extends Stage
{   private final TableView<StudentLessonScore> stdScoreTable = new TableView<>();
    private final ObservableList<StudentLessonScore> stdScoreData = FXCollections.observableArrayList();
    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    String queryLessScoreID, queryLessScoreName;
    private int averageScore;
    StudentLessonScore tmp;
       
    public QueryLessonScoreDialog(String uID, String uName) 
    {
        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);
        queryLessScoreID = uID; queryLessScoreName = uName;
        loadDataStudentLessonScore(); //  �����ݿ���ȡѧ��ѡ�޿γ̼��ɼ������� data �б�
        BorderPane infoBP = new BorderPane();

        stdScoreTable.setEditable(false);
        stdScoreTable.setMaxWidth(770);
        
        TableColumn<StudentLessonScore, String> queryNumCol = new TableColumn<>("�γ̺�");
        queryNumCol.setMinWidth(80);
        queryNumCol.setCellValueFactory(new PropertyValueFactory<StudentLessonScore, String>("uid"));
        
        TableColumn<StudentLessonScore, String> queryNameCol = new TableColumn<>("�γ�����");
        queryNameCol.setMinWidth(180);
        queryNameCol.setCellValueFactory(new PropertyValueFactory<StudentLessonScore, String>("name"));
                       
        TableColumn<StudentLessonScore, String> queryTeaCol = new TableColumn<>("�ο���ʦ");
        queryTeaCol.setMinWidth(80);
        queryTeaCol.setCellValueFactory(new PropertyValueFactory<StudentLessonScore, String>("teacher"));
         
        TableColumn<StudentLessonScore, Integer> queryChourCol = new TableColumn<>("ѧ��");
        queryChourCol.setMinWidth(40);
        queryChourCol.setCellValueFactory(new PropertyValueFactory<StudentLessonScore, Integer>("chour"));
        
        TableColumn<StudentLessonScore, String> queryTypeCol = new TableColumn<>("�γ�����");
        queryTypeCol.setMinWidth(80);
        queryTypeCol.setCellValueFactory(new PropertyValueFactory<StudentLessonScore, String>("type"));
        
        TableColumn<StudentLessonScore, Boolean> queryEngCol = new TableColumn<>("˫���ѧ");
        queryEngCol.setMinWidth(60);
        queryEngCol.setCellValueFactory(new PropertyValueFactory<StudentLessonScore, Boolean>("eng"));
        queryEngCol.setCellFactory(CheckBoxTableCell.forTableColumn(queryEngCol));
        
        TableColumn<StudentLessonScore, String> queryKsfsCol = new TableColumn<>("���Է�ʽ");
        queryKsfsCol.setMinWidth(80);
        queryKsfsCol.setCellValueFactory(new PropertyValueFactory<StudentLessonScore, String>("ksfs"));
        
        TableColumn<StudentLessonScore, Integer> queryScoreCol = new TableColumn<>("�ɼ�");
        queryScoreCol.setMinWidth(60);
        queryScoreCol.setCellValueFactory(new PropertyValueFactory<StudentLessonScore, Integer>("score"));
        
        stdScoreTable.setItems(stdScoreData);
        stdScoreTable.getColumns().addAll(queryNumCol, queryNameCol, queryTeaCol, queryChourCol, queryTypeCol, queryEngCol, queryKsfsCol, queryScoreCol);
        
        Button exitButton = new Button("�˳�", new ImageView(new Image("image/quit.png")));        
        exitButton.setOnAction(e->{ close(); }); // �ر�ѡ�δ���
               
        Label lessScoreLab = new Label("��ѧ����"+queryLessScoreName+"��ѡ�޿γ̳ɼ��б�"+"��ƽ���֣�"+averageScore+"�����㣺"+getScorePoint()+"��");
        lessScoreLab.setFont(new Font("KaiTi", 16));    
        lessScoreLab.setTextFill(Color.BROWN);
        BorderPane seleBP = new BorderPane();
        seleBP.setTop(lessScoreLab);

        ScrollPane spp = new ScrollPane(stdScoreTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        seleBP.setCenter(spp);
        seleBP.setBottom(exitButton);
        seleBP.setMargin(lessScoreLab, new Insets(5, 0, 5, 0));
        seleBP.setMargin(exitButton, new Insets(10, 0, 10, 0));
        seleBP.setAlignment(exitButton, Pos.CENTER);
        Tab stuTab = new Tab();
        stuTab.setText(" �ɼ��� ");
        stuTab.setClosable(false);
        stuTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        stuTab.setContent(seleBP);


        // ����ͼ
        CategoryAxis lxAxis = new CategoryAxis();
        lxAxis.setLabel("");
        NumberAxis lyAxis = new NumberAxis(0,100,10);
        lyAxis.setLabel("�ɼ�");
        LineChart linechart = new LineChart(lxAxis, lyAxis);
        XYChart.Series<String,Number>  series = new XYChart.Series();
        series.setName("���Ƴɼ�");
        for (int i=0;i<stdScoreData.size();i++) {
            tmp = stdScoreData.get(i);
            series.getData().add(new XYChart.Data(tmp.getName(), tmp.getScore()));
        }
        linechart.getData().add(series);
        Group rootl = new Group(linechart);
        Tab lTab = new Tab();
        lTab.setText(" �ɼ�����ͼ ");
        lTab.setClosable(false);
        lTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        BorderPane lBP = new BorderPane();
        lBP.setCenter(rootl);
        lTab.setContent(lBP);


        // ��״ͼ
        CategoryAxis sxAxis = new CategoryAxis();
        NumberAxis syAxis = new NumberAxis(0,100,10);
        syAxis.setLabel("�ɼ�");
        StackedBarChart<String,Number> stackedBarChart = new StackedBarChart<String,Number>(sxAxis,syAxis);
        stackedBarChart.setTitle("�ɼ���״�ֲ�ͼ");
        for (int i=0;i<stdScoreData.size();i++) {
            tmp = stdScoreData.get(i);
            XYChart.Series<String, Number> series1 = new XYChart.Series();
            series1.getData().add(new XYChart.Data(tmp.getName(), tmp.getScore()));
            series1.setName(tmp.getName());
            stackedBarChart.getData().add(series1);
        }
        Group rootst = new Group(stackedBarChart);
        Tab sTab = new Tab();
        sTab.setText(" �ɼ���״ͼ ");
        sTab.setClosable(false);
        sTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        BorderPane sBP = new BorderPane();
        sBP.setCenter(rootst);
        sTab.setContent(sBP);


        //��ͼ
        int count_a=0,count_b=0,count_c=0,count_d=0,count_e=0;
        for (int i=0;i<stdScoreData.size();i++) {
            tmp = stdScoreData.get(i);

            if (tmp.getScore()>=90) count_a++;
            else if (tmp.getScore()>=80) count_b++;
            else if (tmp.getScore()>=70) count_c++;
            else if (tmp.getScore()>=60) count_d++;
            else count_e++;
        }
        ObservableList<PieChart.Data> answer = FXCollections.observableArrayList();
        answer.addAll(new PieChart.Data("����", count_a), new PieChart.Data("����",count_b),
                new PieChart.Data("�е�",count_c),  new PieChart.Data("����",count_d),
                new PieChart.Data("������",count_e));
        PieChart pieChart = new PieChart();
        pieChart.setData(answer);
        pieChart.setTitle("�ɼ��ֲ���ͼ");
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setClockwise(false);
        pieChart.setLabelsVisible(false);
        Group rootp = new Group(pieChart);
        Tab pTab = new Tab();
        pTab.setText(" �ɼ��ֲ���ͼ ");
        pTab.setClosable(false);
        pTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        BorderPane pBP = new BorderPane();
        pBP.setCenter(rootp);
        pTab.setContent(pBP);


        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(stuTab, lTab, sTab, pTab);
        tabPane.setPadding(new Insets(4, 0, 0, 0));
        infoBP.setCenter(tabPane);
        exitButton.setOnAction(e->{
            close();  // �ر��������봰��
        });

        infoBP.setBottom(exitButton);
        infoBP.setAlignment(exitButton, Pos.CENTER_RIGHT);
        infoBP.setMargin(exitButton, new Insets(8, 20, 8, 10));
        Scene scene=new Scene(infoBP, 730, 510);
        setTitle("�ɼ���");
        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }           
    
    private void loadDataStudentLessonScore() {
        String sql="SELECT lesson.uid,lesson.name,lesson.teacher,lesson.chour,lesson.type,lesson.eng,lesson.ksfs,score FROM score, lesson "+
                    "WHERE student_id = '"+queryLessScoreID+"' AND lesson_id = lesson.uid";
               
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            stdScoreData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid");
                String name=rs.getString("name");
                String teacher=rs.getString("teacher");
                int chour=rs.getInt("chour"); 
                String type=rs.getString("type");
                boolean eng=rs.getBoolean("eng");
                String ksfs=rs.getString("ksfs");
                int score=rs.getInt("score");
                
                stdScoreData.add(new StudentLessonScore(no, name, teacher, chour, type, new Boolean(eng), ksfs, score));
            }
            
            sql="SELECT avg(score) as avg FROM score WHERE student_id = '"+queryLessScoreID+"'";
            rs=stmt.executeQuery(sql);
            int count = 0;
            double total = 0;
            if (rs.next())
                averageScore = rs.getInt("avg");
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    private double getScorePoint()
    {   double sp = 0;
        int uscore = averageScore;
        if(uscore>=90&&uscore<=100)
            sp = 4.0;
        else if(uscore>=85&&uscore<=89)
            sp = 3.7;
        else if(uscore>=82&&uscore<=84)
            sp = 3.3;
        else if(uscore>=78&&uscore<=81)
            sp = 3.0;
        else if(uscore>=75&&uscore<=77)
            sp = 2.7;
        else if(uscore>=72&&uscore<=74)
            sp = 2.3;
        else if(uscore>=68&&uscore<=71)
            sp = 2.0;
        else if(uscore>=64&&uscore<=67)
            sp = 1.5;
        else if(uscore>=60&&uscore<=63)
            sp = 1.0;
        else
            sp = 0;
        return sp;        
    }
    
    public static class StudentLessonScore
    {   private final SimpleStringProperty uid;
    	private final SimpleStringProperty name;
        private final SimpleStringProperty teacher;
        private final SimpleIntegerProperty chour;
        private final SimpleStringProperty type;
        private final SimpleBooleanProperty eng;
        private final SimpleStringProperty ksfs;
        private final SimpleIntegerProperty score;
        
        StudentLessonScore(String uUid, String uName, String uTeacher, int uChour, String uType, Boolean uEng, String uKsfs, int uScore)
        {   this.uid = new SimpleStringProperty(uUid);
            this.name = new SimpleStringProperty(uName);
            this.teacher = new SimpleStringProperty(uTeacher);
            this.chour = new SimpleIntegerProperty(uChour);
            this.type = new SimpleStringProperty(uType);
            this.eng = new SimpleBooleanProperty(uEng);
            this.ksfs = new SimpleStringProperty(uKsfs);
            this.score = new SimpleIntegerProperty(uScore);
        }
         
        public String getUid() 
        {   return uid.get();
        }
        
        public void setUid(String uUid) 
        {   uid.set(uUid);
        }
        
        public String getName() 
        {   return name.get();
        }

        public void setName(String uName) 
        {   name.set(uName);
        }

        public String getTeacher() 
        {   return teacher.get();
        }

        public void setTeacher(String uTeacher) 
        {   teacher.set(uTeacher);
        }
        
        public int getChour() 
        {   return chour.get();
        }

        public void setChour(int uChour) 
        {   chour.set(uChour);
        }
        
        public String getType() 
        {   return type.get();
        }

        public void setType(String uType) 
        {   type.set(uType);
        }
        
        public SimpleBooleanProperty engProperty() // �� CheckBox ����
        {   return eng;    
        }  
        
        public boolean getEng() 
        {   return eng.get();
        }

        public void setEng(boolean uEng) 
        {   eng.set(uEng);
        }
                
        public String getKsfs() 
        {   return ksfs.get();
        }

        public void setKsfs(String uKsfs) 
        {   ksfs.set(uKsfs);
        }
        
        public int getScore() 
        {   return score.get();
        }
        
        public void setScore(int uScore) 
        {   score.set(uScore);
        }
    }
}
