import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.sql.*;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Modality;

class LoginDialog extends Stage
{   Label userLabel = new Label("�û���: ");
	TextField userName = new TextField();
	Label passLabel = new Label("��  ��: ");
	PasswordField userPassword = new PasswordField();
	
	Label sfLabel = new Label("��ݣ�");
	RadioButton studentRB = new RadioButton("ѧ��");
	RadioButton teacherRB = new RadioButton("��ʦ");
	RadioButton adminRB = new RadioButton("����");
	      
	Button loginButton = new Button("��¼");
	Button quitButton = new Button("�˳�");

	String userId;
	MainStudent appUser;
	
	String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
		      
    public LoginDialog(MainStudent app, int width, int height)
	{   appUser = app;
	    userName.setPrefWidth(150);
	    userPassword.setPrefWidth(150);
	    loginButton.setPrefSize(60,10);
    	loginButton.setOnAction(new EventHandler<ActionEvent>()
    	{   public void handle(ActionEvent e)
            {   loginAction();
			}
        });
    	quitButton.setPrefSize(60,10);
    	quitButton.setOnAction(new EventHandler<ActionEvent>()
    	{   public void handle(ActionEvent e)
        	{	close();   
        	    Platform.exit(); 	    
        	}
        });
    	
    	VBox vb = new VBox();
        vb.setSpacing(10);
        
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(userLabel, userName);
        hb1.setMargin(userLabel, new Insets(3,0,0,0));
        hb1.setMargin(userName, new Insets(0,0,0,3));
                
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(passLabel, userPassword);
        hb2.setMargin(passLabel, new Insets(3,0,0,0));
        hb2.setMargin(userPassword, new Insets(0,0,0,6));
                
        HBox hb3 = new HBox();
        hb3.getChildren().addAll(sfLabel, studentRB, teacherRB, adminRB);
        hb3.setSpacing(5);
        hb3.setMargin(sfLabel, new Insets(2,0,0,0));
        hb3.setMargin(studentRB, new Insets(0,0,0,5));
        
        ToggleGroup tg = new ToggleGroup();
        studentRB.setToggleGroup(tg); 
        teacherRB.setToggleGroup(tg);
        adminRB.setToggleGroup(tg);
                            
        HBox hb4 = new HBox();
        hb4.setSpacing(20);
        hb4.setAlignment(Pos.CENTER);
        hb4.getChildren().addAll(loginButton, quitButton);
    
        vb.getChildren().addAll(hb1, hb2, hb3);
        
        BorderPane bPane = new BorderPane();
        bPane.setCenter(vb);
        bPane.setBottom(hb4);
        bPane.setPadding(new Insets(15,40,20,40));

        initModality(Modality.APPLICATION_MODAL);
        setScene(new Scene(bPane));

		Image backGround = new Image("/image/logo.jpg");
		getIcons().add(backGround);

        setWidth(300);
        setHeight(200);
	    setTitle("��¼ϵͳ");
        showAndWait();
	}
	
	public void loginAction() 
	{   int sflb = 0;
	    String sfStr = "", tbName = "";
	    userId = (userName.getText()).trim();
		String pw = (userPassword.getText()).trim();
	    if(userId.length() == 0)
	    {   CommonDialog.WarningDialog("�������û�����");
	        return;
	    }
	    if(studentRB.isSelected())
	    {   sflb = 0; sfStr = "ѧ�ţ�";
	        tbName = "student";
	    }
	    if(teacherRB.isSelected())
	    {   sflb = 1; sfStr = "��ʦ�ţ�";
	        tbName = "teacher";
	    }
	    if(adminRB.isSelected())
	    {   sflb = 2; sfStr = "����ţ�";
	        tbName = "operator";
	    }
	    if(sfStr.length() == 0)
	    {   CommonDialog.WarningDialog("��ѡ���¼��������");
	        return;
	    }
	    try
	    {   Connection con = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM "+tbName+" where uid = '" + userId + "'");
	        if(rs.next()) 
	        {   String pwStr = rs.getString("passwd");
	            String nameStr = rs.getString("name").trim();
	            if(pwStr==null || pwStr.equals(pw))
	            {   appUser.sfLB = sflb;
	                appUser.sfID = userId;
	                appUser.sfName = nameStr;
	                rs.close();
	                stmt.close();
	                con.close();
	                close();
	            }
	            else
	                CommonDialog.WarningDialog("���벻��ȷ�� ���������룡");
	        }
	        else
	            CommonDialog.WarningDialog(sfStr+"��"+userId+"�������ڣ� ���������룡");

	        rs.close();
	        stmt.close();
	        con.close();
		} 
	    catch(SQLException s)  
	    {
	    	System.out.println("LoginDialog ���ݿ����ʧ�ܣ�");
	    	System.out.println(s);
		}
    }
}