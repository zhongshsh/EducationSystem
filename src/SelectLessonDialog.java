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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
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

class SelectLessonDialog extends Stage
{   private final TableView<SelectLesson> selTable = new TableView<>();

    private final ObservableList<SelectLesson> selData = FXCollections.observableArrayList();
    private final ObservableList<String> lessonData = FXCollections.observableArrayList();
    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    String selID, selName;
    ListView<String> lessListV;
    Vector deletedLessList = new Vector();
    
    public SelectLessonDialog(String uID, String uName) 
    {
        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);
        selID = uID; selName = uName;
        loadSelectLesson(); //  从数据库获取该学生选课情况列表
        loadAllLesson();    //  从数据库获取可选课程列表
                
        selTable.setEditable(false);
        selTable.setMaxWidth(560);
        
        TableColumn<SelectLesson, String> selNumCol = new TableColumn<>("课程号");
        selNumCol.setMinWidth(60);
        selNumCol.setCellValueFactory(new PropertyValueFactory<SelectLesson, String>("uid"));
                        
        TableColumn<SelectLesson, String> selNameCol = new TableColumn<>("课程名称");
        selNameCol.setMinWidth(180);
        selNameCol.setCellValueFactory(new PropertyValueFactory<SelectLesson, String>("name"));
                               
        TableColumn<SelectLesson, String> selTeaCol = new TableColumn<>("任课老师");
        selTeaCol.setMinWidth(60);
        selTeaCol.setCellValueFactory(new PropertyValueFactory<SelectLesson, String>("teacher"));
                 
        TableColumn<SelectLesson, Integer> selChourCol = new TableColumn<>("学分");
        selChourCol.setMinWidth(40);
        selChourCol.setCellValueFactory(new PropertyValueFactory<SelectLesson, Integer>("chour"));
               
        TableColumn<SelectLesson, String> selTypeCol = new TableColumn<>("课程性质");
        selTypeCol.setMinWidth(70);
        selTypeCol.setCellValueFactory(new PropertyValueFactory<SelectLesson, String>("type"));
                
        TableColumn<SelectLesson, Boolean> selEngCol = new TableColumn<>("双语教学");
        selEngCol.setMinWidth(50);
        selEngCol.setCellValueFactory(new PropertyValueFactory<SelectLesson, Boolean>("eng"));
        selEngCol.setCellFactory(CheckBoxTableCell.<SelectLesson>forTableColumn(selEngCol));
        
        TableColumn<SelectLesson, String> selKsfsCol = new TableColumn<>("考试方式");
        selKsfsCol.setMinWidth(60);
        selKsfsCol.setCellValueFactory(new PropertyValueFactory<SelectLesson, String>("ksfs"));
                
        selTable.setItems(selData);
        selTable.getColumns().addAll(selNumCol, selNameCol, selTeaCol, selChourCol, selTypeCol, selEngCol, selKsfsCol);
            
      
        final Button addButton = new Button("", new ImageView(new Image("image/leftArrow.png")));
        addButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        final Button moveButton = new Button("", new ImageView(new Image("image/rightArrow.png")));
        moveButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        final Button exitButton = new Button("更新并退出", new ImageView(new Image("image/quit.png")));        
        moveButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	SelectLesson p = selTable.getSelectionModel().getSelectedItem();
        	    int pos = selTable.getSelectionModel().getSelectedIndex();
        		        		
        		if(p != null)
        	    {   String delName = p.getName();
        			if(CommonDialog.ConfirmDialog("温馨提示", "确认退选课程名为 《"+delName+"》 的课程吗？"))
        			{   if(p.getDTag() == 0)  // 不是新增加的记录，需要记录退选的课程号，以便统一删除数据库中的记录
        			        deletedLessList.add(p.getUid());      			
        		        selData.remove(pos);
        		    }
        		}
        		else
        		    CommonDialog.WarningDialog("你没有选中需要退选的课程记录"); 
        	}
            
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	String selp = lessListV.getSelectionModel().getSelectedItem();
        	    if(selp != null)
        	    {   String ssid = getLessonId(selp);
        	        for(int i=0;i < selData.size(); i++)
        	            if((((SelectLesson)selData.get(i)).getUid()).equals(ssid))
        	            {   CommonDialog.WarningDialog("课程《"+((SelectLesson)selData.get(i)).getName()+"》你已经选过了！"); 
        	                return;
        	            }
        	        SelectLesson op = getOneLesson(ssid);
        	        if(op != null)
        	        {   if(deletedLessList.contains(ssid))
        	            {   op.setDTag(0);
        	                deletedLessList.remove(ssid);
        	            }   
        	            selData.add(op);
   	                    selTable.refresh();
   	                    selTable.getSelectionModel().selectLast();
   	                    selTable.scrollTo(selData.size());
   	                    selTable.requestFocus();
   	                }
   	            }
   	            else
        		    CommonDialog.WarningDialog("你没有选需选修的课程记录"); 
   	            
        	}       
        });
        
        exitButton.setOnAction(e->{
        	updateSelectLesson();
            close();  // 关闭选课窗口
        }); 
               
        final HBox topHb = new HBox();
        Label selectedLab = new Label("（学号："+selID+" 姓名："+selName+"）已选的课程：");
        selectedLab.setFont(new Font(14));    
        selectedLab.setTextFill(Color.BLUE);
        Label allLab = new Label("系统中可选的课程列表：");
        allLab.setFont(new Font("KaiTi", 16));
        allLab.setTextFill(Color.RED);
        topHb.getChildren().addAll(selectedLab, allLab);
        topHb.setAlignment(Pos.CENTER_LEFT);
        topHb.setMargin(selectedLab, new Insets(5, 0, 2, 0));
        topHb.setMargin(allLab, new Insets(5, 20, 2, 380));
        
                                
        ScrollPane spp = new ScrollPane(selTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        
        BorderPane seleBP = new BorderPane();
        seleBP.setTop(topHb);
        seleBP.setMargin(topHb, new Insets(5, 10, 5, 10));
        
        seleBP.setCenter(spp);
        
        final HBox rightHb = new HBox();
        final VBox vb1 = new VBox();
        vb1.getChildren().addAll(addButton, moveButton);
        vb1.setAlignment(Pos.TOP_CENTER);
        vb1.setMargin(addButton, new Insets(60, 0, 0, 7));
        vb1.setMargin(moveButton, new Insets(60, 0, 0, 7));
        
        lessListV = new ListView<String>(lessonData);
        rightHb.getChildren().addAll(vb1, lessListV);
        rightHb.setSpacing(7);
        rightHb.setPadding(new Insets(0, 10, 0, 0));
        seleBP.setRight(rightHb);
                       
        seleBP.setBottom(exitButton);
        seleBP.setAlignment(exitButton, Pos.CENTER);
        seleBP.setMargin(exitButton, new Insets(10, 0, 10, 0));
        
        setTitle("选课窗口");
        setScene(new Scene(seleBP, 942, 500));

        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
        
    private void updateSelectLesson()
    {   String lessnum, stunum;
        int udtag;
                
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            int i;
            for(i=0;i<deletedLessList.size();i++)  // 删除退选的课程
            {   String deleteSql = "DELETE FROM score WHERE lesson_id='"+((String)deletedLessList.get(i)).trim()+"'";
                int count = stmt.executeUpdate(deleteSql);
            }
                        
            for(i=0;i<selData.size();i++)
            {   lessnum = ((SelectLesson)selData.get(i)).getUid();
                udtag = ((SelectLesson)selData.get(i)).getDTag();
                
                if(udtag != 0)  // 新增加记录
                {   String insertSql="INSERT INTO score(lesson_id, student_id) VALUES('"+lessnum+"','"+selID+"')";
                    int count=stmt.executeUpdate(insertSql);
                }
            }                 
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
        
    private void loadSelectLesson()
    {   String sql="SELECT lesson.uid,lesson.name,lesson.teacher,lesson.chour,lesson.type,lesson.eng,lesson.ksfs FROM score, lesson WHERE student_id = '"+
                   selID+"' AND lesson_id = lesson.uid";
        
               
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            selData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid");
                String name=rs.getString("name");
                String teacher=rs.getString("teacher");
                int chour=rs.getInt("chour"); 
                String type=rs.getString("type");
                boolean eng=rs.getBoolean("eng");
                String ksfs=rs.getString("ksfs");
                
                selData.add(new SelectLesson(no, name, teacher, new Integer(chour), type, new Boolean(eng), ksfs,0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    private SelectLesson getOneLesson(String uuID) // 从课程表中获取课程数据并产生 SelectLesson 对象
    {   String sql="SELECT uid,name,teacher,chour,type,eng,ksfs FROM lesson WHERE uid = '"+uuID+"'";
        SelectLesson oneSelLess = null;       
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            ResultSet rs=stmt.executeQuery(sql);
            if(rs.next())
            {   String no=rs.getString("uid");
                String name=rs.getString("name");
                String teacher=rs.getString("teacher");
                int chour=rs.getInt("chour"); 
                String type=rs.getString("type");
                boolean eng=rs.getBoolean("eng");
                String ksfs=rs.getString("ksfs");
                
                oneSelLess = new SelectLesson(no, name, teacher, new Integer(chour), type, new Boolean(eng), ksfs,1);
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
        return oneSelLess;
    }
    
    private void loadAllLesson()
    {   String sql="SELECT uid,name,teacher,chour FROM lesson";
               
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            lessonData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid");
                String name=rs.getString("name");
                String teacher=rs.getString("teacher");
                int chour=rs.getInt("chour"); 
                lessonData.add("["+no+"] "+name+" ("+teacher+" - "+chour+"学分)");
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
        return lvstr.substring(1, endp);     
    }
    
    public static class SelectLesson
    {   private final SimpleStringProperty uid;
    	private final SimpleStringProperty name;
        private final SimpleStringProperty teacher;
        private final SimpleIntegerProperty chour;
        private final SimpleStringProperty type;
        private final SimpleBooleanProperty eng;
        private final SimpleStringProperty ksfs;
        private int dtag; // 0 无需更改      1 新增加的记录

        private SelectLesson(String uUid, String uName, String uTeacher, int uChour, String uType, Boolean uEng, String uKsfs, int udtag) 
        {   this.uid = new SimpleStringProperty(uUid);
            this.name = new SimpleStringProperty(uName);
            this.teacher = new SimpleStringProperty(uTeacher);
            this.chour = new SimpleIntegerProperty(uChour);
            this.type = new SimpleStringProperty(uType);
            this.eng = new SimpleBooleanProperty(uEng);
            this.ksfs = new SimpleStringProperty(uKsfs);
            dtag = udtag;
        }
        
        private SelectLesson(String uUid, String uName, String uTeacher, int uChour, String uType, Boolean uEng, String uKsfs) 
        {   this(uUid, uName, uTeacher, uChour, uType, uEng, uKsfs, 0);
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
        
        public SimpleBooleanProperty engProperty() // 绑定 CheckBox 属性
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
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
}

