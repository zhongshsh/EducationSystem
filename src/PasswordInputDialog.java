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

class PasswordInputDialog extends Stage
{
    private final TableView<PersonPassword> passwordTable = new TableView<>();

    private final ObservableList<PersonPassword> passwordData = FXCollections.observableArrayList();
    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    RadioButton studentRB = new RadioButton("学生");
    RadioButton teacherRB = new RadioButton("教师");
    RadioButton operatorRB = new RadioButton("管理员");
    private int passLX = 0;
    private boolean inputFlag = false;
    private final Button exitButton;
        
    public PasswordInputDialog() 
    {   //loadPasswordPerson();    //  从数据库获取当前老师上课列表
        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);
        passwordTable.setEditable(true);
        
        TableColumn<PersonPassword, String> passNumCol = new TableColumn<>("编号");
        passNumCol.setMinWidth(100);
        passNumCol.setCellValueFactory(new PropertyValueFactory<PersonPassword, String>("uid"));
                        
        TableColumn<PersonPassword, String> passNameCol = new TableColumn<>("姓名");
        passNameCol.setMinWidth(150);
        passNameCol.setCellValueFactory(new PropertyValueFactory<PersonPassword, String>("name"));
        
        TableColumn<PersonPassword, String> passWordCol = new TableColumn<>("密码");
        passWordCol.setMinWidth(100);
        passWordCol.setCellValueFactory(new PropertyValueFactory<PersonPassword, String>("password"));
        passWordCol.setCellFactory(TextFieldTableCell.<PersonPassword>forTableColumn());
        passWordCol.setOnEditCommit(new EventHandler<CellEditEvent<PersonPassword, String>>() {
            @Override
            public void handle(CellEditEvent<PersonPassword, String> t) {
            	if(!t.getNewValue().equals(t.getOldValue()) && ((PersonPassword) t.getTableView().getItems().get(t.getTablePosition().getRow())).getDTag() == 0)
            	{   inputFlag = true;
            	    ((PersonPassword) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDTag(1);
                }
                ((PersonPassword) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPassword(t.getNewValue());
            }
        });
                               
        passwordTable.setItems(passwordData);
        passwordTable.getColumns().addAll(passNumCol, passNameCol, passWordCol);
            
        exitButton = new Button("保存并退出", new ImageView(new Image("image/quit.png")));        
        exitButton.setOnAction(e->{
        	if(passLX >=1 && passLX <= 3)
            {   switch(passLX)
                { case 1: updatePersonPassword("student"); break;
                  case 2: updatePersonPassword("teacher"); break;
                  case 3: updatePersonPassword("operator"); break;
                }
            }
            close();  // 关闭选课窗口
        }); 
               
        final HBox topHb = new HBox();
        Label passLab = new Label("请选择：");
        passLab.setFont(new Font("KaiTi", 16));    
        passLab.setTextFill(Color.BROWN);
        
        final ToggleGroup gro = new ToggleGroup();
        studentRB.setToggleGroup(gro);
        teacherRB.setToggleGroup(gro);
        operatorRB.setToggleGroup(gro);
        studentRB.setOnAction(new PasswdHandler());
        teacherRB.setOnAction(new PasswdHandler());
        operatorRB.setOnAction(new PasswdHandler());
        
        topHb.getChildren().addAll(passLab, studentRB, teacherRB, operatorRB);
        topHb.setAlignment(Pos.CENTER_LEFT);
        topHb.setMargin(passLab, new Insets(10, 0, 10, 10));
        topHb.setMargin(studentRB, new Insets(10, 0, 10, 10));
        topHb.setMargin(teacherRB, new Insets(10, 0, 10, 10));                                
        topHb.setMargin(operatorRB, new Insets(10, 0, 10, 10));
        
        ScrollPane spp = new ScrollPane(passwordTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setFitToHeight(true);
        spp.setFitToWidth(true);
        BorderPane seleBP = new BorderPane();
        seleBP.setTop(topHb);
        seleBP.setCenter(spp);
        seleBP.setBottom(exitButton);
        seleBP.setAlignment(exitButton, Pos.CENTER);
        seleBP.setMargin(exitButton, new Insets(15, 0, 15, 0));
        
        setTitle("设置密码");
        setScene(new Scene(seleBP, 368, 490));
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
    
    private class PasswdHandler implements EventHandler<ActionEvent>
    {   public void handle(ActionEvent e)
        {   if(passLX >=1 && passLX <= 3)
            {   switch(passLX)
                { case 1: updatePersonPassword("student"); break;
                  case 2: updatePersonPassword("teacher"); break;
                  case 3: updatePersonPassword("operator"); break;
                }
            }
            //System.out.println("Old LX: "+passLX);
            if(studentRB.isSelected())
            {   passLX = 1; 
                loadPersonPassword("student");
            }
            if(teacherRB.isSelected())
            {   passLX = 2; 
                loadPersonPassword("teacher");
            }
            if(operatorRB.isSelected())
            {   passLX = 3; 
                loadPersonPassword("operator");
            }
            //System.out.println("New LX: "+passLX);
        }
    }
        
    private void updatePersonPassword(String dbName)
    {   if(!inputFlag)
            return;
        String pwnum, pwpw;
        int pwdtag;
        
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            for(int i=0;i<passwordData.size();i++)
            {   pwnum = ((PersonPassword)passwordData.get(i)).getUid();
                pwpw = ((PersonPassword)passwordData.get(i)).getPassword();
                pwdtag = ((PersonPassword)passwordData.get(i)).getDTag();
                if(pwdtag == 1)
                {   String updateSql="UPDATE "+dbName+" SET passwd='"+pwpw+"' WHERE uid='"+pwnum+"'";
                    int count=stmt.executeUpdate(updateSql);
                    //System.out.println(pwnum+"  "+pwpw+"  "+pwdtag);               
                }
            }                 
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
        
    private void loadPersonPassword(String dbName)
    {   String sql="SELECT uid, name, passwd FROM "+dbName;
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            passwordData.clear();
            inputFlag = false;
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid");
                String name=rs.getString("name");
                String passwd=rs.getString("passwd");
                
                passwordData.add(new PersonPassword(no, name, passwd,0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public static class PersonPassword
    {   private final SimpleStringProperty uid;
    	private final SimpleStringProperty name;
        private final SimpleStringProperty password;
        private int dtag; // 0 无需更改      1 编辑更改过的数据

        private PersonPassword(String uUid, String uName, String uPassword, int udtag) 
        {   this.uid = new SimpleStringProperty(uUid);
            this.name = new SimpleStringProperty(uName);
            this.password = new SimpleStringProperty(uPassword);
            dtag = udtag;
        }
        
        private PersonPassword(String uUid, String uName, String uPassword) 
        {   this(uUid, uName, uPassword, 0);
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

        public String getPassword() 
        {   return password.get();
        }

        public void setPassword(String uPassword) 
        {   password.set(uPassword);
        }
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
    
}

