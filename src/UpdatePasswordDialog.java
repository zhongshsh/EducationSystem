import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Modality;
import java.sql.*;

class UpdatePasswordDialog extends Stage
{   Label passLabel = new Label("新密码: ");
	PasswordField userPassword = new PasswordField();
	
	Button okButton = new Button("确认", new ImageView(new Image("image/ok.png")));
	Button cancelButton = new Button("取消", new ImageView(new Image("image/cancel.png")));
	
	String DBUrl="jdbc:ucanaccess:///./management.mdb";
    String DBUser="", DBPassword="";
    String DBName="", uPasswordID="";

    public UpdatePasswordDialog(int usf, String uPID)
	{
		Image backGround = new Image("/image/logo.jpg");
		getIcons().add(backGround);
		if(usf == 0)  DBName = "student";
    	if(usf == 1)  DBName = "teacher";
    	if(usf == 2)  DBName = "operator";
    	uPasswordID = uPID;
    	
    	okButton.setOnAction(e->{ updatePasswordHandle(); close();  });
    	cancelButton.setOnAction(e->{ close();   });
    	    
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(passLabel, userPassword);
        hb1.setMargin(passLabel, new Insets(3,0,0,0));
        hb1.setMargin(userPassword, new Insets(0,0,0,3));
        
        HBox hb2 = new HBox();
        hb2.getChildren().addAll(okButton, cancelButton);
        hb2.setMargin(cancelButton, new Insets(0,0,0,50));
        hb2.setPadding(new Insets(30,0,150,0));
        hb2.setAlignment(Pos.CENTER);
                     
        BorderPane bPane = new BorderPane();
        bPane.setCenter(hb1);
        bPane.setBottom(hb2);
        bPane.setPadding(new Insets(20,30,20,30));

		setX(330);
		setY(120);
        initModality(Modality.APPLICATION_MODAL);
        setScene(new Scene(bPane, 250, 130));
        
        setTitle("密码修改");
        showAndWait();
	}
	
	private void updatePasswordHandle()
	{   String pw = (userPassword.getText()).trim();
	    if(pw.length() == 0)
	    {   CommonDialog.WarningDialog("新密码不能为空 ！");
	        return;
	    }
	    try
	    {   Connection con = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
	        Statement stmt = con.createStatement();
	        String updateSql="UPDATE "+DBName+" SET passwd='"+pw+"' WHERE uid='"+uPasswordID+"'";
	        int count=stmt.executeUpdate(updateSql);
	        
	        stmt.close();
	        con.close();
		} 
	    catch(SQLException s)  
	    {
	    	System.out.println("数据库操作失败！");
		}
    }
}