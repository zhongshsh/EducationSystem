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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.converter.IntegerStringConverter;
import java.util.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;

class ScoreInputDialog extends Stage
{   private final TableView<LessonScore> scoreTable = new TableView<>();

    private final ObservableList<LessonScore> scoreData = FXCollections.observableArrayList();
    private final ObservableList<String> lessData = FXCollections.observableArrayList();
    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    String scoreID, scoreName;
    ComboBox<String> lessCB;
    String selectedLessonID = ""; 
    boolean inputFlag = false;
    RadioButton editRB;
    private final Button exitButton;
        
    public ScoreInputDialog(String uID, String uName) 
    {
        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);
        scoreID = uID; scoreName = uName;
        loadTeacherLesson();    //  从数据库获取当前老师上课列表
                
        scoreTable.setEditable(false);
        
        TableColumn<LessonScore, String> scoNumCol = new TableColumn<>("学号");
        scoNumCol.setMaxWidth(80);
        scoNumCol.setCellValueFactory(new PropertyValueFactory<LessonScore, String>("uid"));
                        
        TableColumn<LessonScore, String> scoNameCol = new TableColumn<>("姓名");
        scoNameCol.setMaxWidth(80);
        scoNameCol.setCellValueFactory(new PropertyValueFactory<LessonScore, String>("name"));
        
        TableColumn<LessonScore, String> scoSexCol = new TableColumn<>("性别");
        scoSexCol.setMaxWidth(50);
        scoSexCol.setCellValueFactory(new PropertyValueFactory<LessonScore, String>("sex"));
                               
        TableColumn<LessonScore, String> scoDepCol = new TableColumn<>("隶属院系");
        scoDepCol.setMaxWidth(180); scoDepCol.setMinWidth(180);
        scoDepCol.setCellValueFactory(new PropertyValueFactory<LessonScore, String>("department"));
                 
        TableColumn<LessonScore, Integer> scoScoreCol = new TableColumn<>("成绩");
        scoScoreCol.setMaxWidth(50);
        scoScoreCol.setCellValueFactory(new PropertyValueFactory<LessonScore, Integer>("score"));
        scoScoreCol.setCellFactory(TextFieldTableCell.<LessonScore, Integer>forTableColumn(new IntegerStringConverter()));
                         
        scoreTable.setItems(scoreData);
        scoreTable.getColumns().addAll(scoNumCol, scoNameCol, scoSexCol, scoDepCol, scoScoreCol);
            
        exitButton = new Button("放弃并退出", new ImageView(new Image("image/quit.png")));        
        exitButton.setOnAction(e->{
        	updateLessonScore();
            close();  // 关闭选课窗口
        }); 
               
        final HBox topHb = new HBox();
        Label teacherLab = new Label("（教师："+scoreName+"）请选择课程：");
        teacherLab.setFont(new Font("KaiTi", 16));    
        teacherLab.setTextFill(Color.BROWN);
        lessCB = new ComboBox<String>(lessData);
        lessCB.setEditable(false);
        lessCB.setOnAction(e->{
        	String newLessonID = getLessonId(lessCB.getValue());
        	if(!newLessonID.equals(selectedLessonID))
        	{   if(selectedLessonID.length() != 0)
        	        updateLessonScore(); //  更换课程，要先保存原先课程录入数据    
        	    selectedLessonID = newLessonID;
        	    loadLessonScore();
        	}
        }); 
        topHb.getChildren().addAll(teacherLab, lessCB);
        topHb.setAlignment(Pos.CENTER_LEFT);
        topHb.setMargin(teacherLab, new Insets(10, 0, 3, 0));
        topHb.setMargin(lessCB, new Insets(8, 0, 5, 0));
        
                                
        ScrollPane spp = new ScrollPane(scoreTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setFitToHeight(true);
        spp.setFitToWidth(true);
        BorderPane seleBP = new BorderPane();
        seleBP.setTop(topHb);
        seleBP.setCenter(spp);
        final HBox bottomHb = new HBox();
        editRB = new RadioButton("浏览状态");
        editRB.setOnAction(e->{
        	if(editRB.isSelected())
        	{   editRB.setText("输入状态");
        	    exitButton.setText("保存并退出");
        	    inputFlag = true;
        	}
        	else
        	{   editRB.setText("浏览状态");
        	    exitButton.setText("放弃并退出");
        	    inputFlag = false;
        	}
        	scoreTable.setEditable(inputFlag);
        }); 

        Label hintLab = new Label("（输入成绩后回车确认）");
        hintLab.setTextFill(Color.BROWN);
        bottomHb.getChildren().addAll(editRB, exitButton, hintLab);
        bottomHb.setAlignment(Pos.CENTER);
        bottomHb.setSpacing(30);
        seleBP.setBottom(bottomHb);
        seleBP.setMargin(bottomHb, new Insets(10, 0, 10, 0));
        
        setTitle("成绩录入");
        setScene(new Scene(seleBP, 460, 500));
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
        
    private void updateLessonScore()
    {   if(!inputFlag)
            return;
        String stunum;
        int uscore;                        
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            for(int i=0;i<scoreData.size();i++)
            {   stunum = ((LessonScore)scoreData.get(i)).getUid();
                uscore = ((LessonScore)scoreData.get(i)).getScore();
                String updateSql="UPDATE score SET score="+uscore+" WHERE lesson_id='"+selectedLessonID+"' AND student_id='"+stunum+"'";
                int count=stmt.executeUpdate(updateSql);
                String sql="update notice set notice='1', text='成绩信息已更新，请及时查看！' where id='"+stunum+"'";
                stmt.executeUpdate(sql);
            }                 
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
        
    private void loadLessonScore()
    {   String sql="SELECT student.uid,student.name,student.sex,student.department,score FROM score, student WHERE lesson_id = '"+
                   selectedLessonID+"' AND student_id = student.uid";
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            scoreData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid");
                String name=rs.getString("name");
                String sex=rs.getString("sex");
                String dep=rs.getString("department");
                int score=rs.getInt("score");
                
                scoreData.add(new LessonScore(no, name, sex, dep, new Integer(score),0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    private String getLessonId(String lvstr)
    {   int endp = lvstr.indexOf(']');
        return lvstr.substring(1, endp).trim();     
    }
    
    private void loadTeacherLesson()
    {   String sql="SELECT uid, name FROM lesson WHERE teacher='"+scoreName+"'";
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            lessData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid");
                String name=rs.getString("name");
                lessData.add("["+no+"] "+name);
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public static class LessonScore
    {   private final SimpleStringProperty uid;
    	private final SimpleStringProperty name;
        private final SimpleStringProperty sex;
        private final SimpleStringProperty department;
        private final SimpleIntegerProperty score;
        private int dtag; // 0 无需更改      1 编辑更改过的数据

        private LessonScore(String uUid, String uName, String uSex, String uDepartment, int uScore, int udtag) 
        {   this.uid = new SimpleStringProperty(uUid);
            this.name = new SimpleStringProperty(uName);
            this.sex = new SimpleStringProperty(uSex);
            this.department = new SimpleStringProperty(uDepartment);
            this.score = new SimpleIntegerProperty(uScore);
            dtag = udtag;
        }
        
        private LessonScore(String uUid, String uName, String uSex, String uDepartment, int uScore) 
        {   this(uUid, uName, uSex, uDepartment, uScore, 0);
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

        public String getSex() 
        {   return sex.get();
        }

        public void setSex(String uSex) 
        {   sex.set(uSex);
        }

        public String getDepartment() 
        {   return department.get();
        }

        public void setDepartment(String uDepartment) 
        {   department.set(uDepartment);
        }
        
        public SimpleIntegerProperty scoreProperty() // 绑定属性
        {   return score;    
        }  
        
        public int getScore() 
        {   return score.get();
        }
        
        public void setScore(int uScore) 
        {   score.set(uScore);
        }
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
    
}

