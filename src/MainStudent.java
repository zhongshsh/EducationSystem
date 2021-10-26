import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.stage.FileChooser;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import java.sql.*;

import javafx.scene.Group;
import javafx.scene.Scene;
//import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.stage.Modality;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MainStudent extends Application 
{   LoginDialog loginDialog = null;
    HBox toolBar;
    VBox userInfoVB;
    Label user;
    int sfLB = -1;
    String sfID = "", sfName = "";
    int screenWid, screenHei;
    Button loginOut;

    public static void main(String[] args) 
    {   launch(args);
    }


    public void start(Stage stage) 
    {

        Image backGround = new Image("/image/logo.jpg");
        stage.getIcons().add(backGround);
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        screenWid = (int)primaryScreenBounds.getWidth()*3/4;
        screenHei = (int)primaryScreenBounds.getHeight()*3/4;
        
        try 
	    {
            //  加载 JDBC-ODBC 桥驱动程序
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // 数据库装载
		}
		catch(Exception e)  
		{     System.out.println("数据库装载失败: " + e.toString());			
		}
	    VBox vb = new VBox();
	    CreateToolBar();
	    HBox hb = new HBox();
	    
	    user = new Label();
	    user.setTextFill(Color.BROWN);
        user.setFont(new Font(14));
        userInfoVB = new VBox();
        userInfoVB.getChildren().addAll(user);
        userInfoVB.setAlignment(Pos.BOTTOM_RIGHT);
        userInfoVB.setMargin(user, new Insets(0, 10, 0, 0));
        hb.setAlignment(Pos.BOTTOM_LEFT);
        hb.getChildren().addAll(toolBar, userInfoVB); 
        hb.setMargin(userInfoVB, new Insets(0, 10, 4, 50));
        vb.getChildren().addAll(hb);

        BorderPane bordPane = new BorderPane();
        bordPane.setTop(vb);
        ImageView backImage = new ImageView("image/background.jpg");

        backImage.setFitWidth(screenWid);
        backImage.setFitHeight(screenHei);
        bordPane.setCenter(backImage);
                
        Scene scene = new Scene(bordPane, screenWid, screenHei);
        stage.setTitle("学生信息管理系统");
        stage.focusedProperty().addListener(e->{
        	if(sfID.length() > 0)
        	{   String lxStr = "";
        	    if(sfLB == 0)
        	        lxStr = "学生";
        	    if(sfLB == 1)
        	        lxStr = "教师";
        	    if(sfLB == 2)
        	        lxStr = "教务";
                user.setText("欢迎"+lxStr+sfID+": "+sfName+"使用本系统！");
                loginOut.setText("退出");
        	}else {
                user.setText("请登录后使用本系统！");
                loginOut.setText("登录");
            }
        });
        stage.setScene(scene);
        stage.show();
        
        loginDialog = new LoginDialog(this, screenWid, screenHei);
        try {
            CommonDialog.NoticeDialog(sfID, sfName);
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        //图片自适应窗口大小
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                backImage.setFitHeight(newValue.doubleValue());

            }
        });
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                backImage.setFitWidth(newValue.doubleValue());
                if (stage.getWidth() > primaryScreenBounds.getWidth()) {
                    hb.setMargin(userInfoVB, new Insets(0, 10, 4, newValue.doubleValue()*1/5));
                }
                else {
                    hb.setMargin(userInfoVB, new Insets(0, 10, 4, 50));
                }
            }
        });
    }



    private class MenuHandler implements EventHandler<ActionEvent>
    {   public void handle(ActionEvent ae)
        {   String itemName = ((MenuItem)ae.getTarget()).getText();
            if(itemName.equals(" 资料录入 ")) 
            {   if(sfLB == 2) {
                try {
                    new InfoInputDialog();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
                else
                    CommonDialog.WarningDialog("只有《教务员》才能设置系统资料 !"); 	
            }
            if(itemName.equals(" 学生选课 "))
            {   if(sfLB == 0)
                    new SelectLessonDialog(sfID, sfName);
                else
                    CommonDialog.WarningDialog("只有《学生》才能选课 !");
            }
            if(itemName.equals(" 成绩录入 "))
            {   if(sfLB == 1)
                    new ScoreInputDialog(sfID, sfName);
                else
                    CommonDialog.WarningDialog("只有《教师》才能录入学生课程成绩 !");
            }
            if(itemName.equals(" 查询和设置 "))
            {   if(sfLB == 0)
                    new QueryLessonScoreDialog(sfID, sfName);  
                else if(sfLB == 1)
                    new QueryScoreGraphicsDialog(sfID, sfName);
                else if(sfLB == 2)
                    new PasswordInputDialog();
            }
            if(itemName.equals(" 密码修改 "))
            {   new UpdatePasswordDialog(sfLB, sfID);
            }
            if(itemName.equals(" 版本 "))
            {   new ShowVersionDialog();
            }
        }
    }
      
    private void CreateToolBar()  // 建立工具条
	{
        Button studentButton = new Button("资料录入");
        Button lessonButton = new Button("选课");
        Button inputScoreButton = new Button("导入成绩");
        Button lessonAnaButton = new Button("课程分析");
        Button operatorButton = new Button("设置密码");
        Button classButton = new Button("查询课表");
        Button gradeButton = new Button("查询成绩");

        loginOut = new Button("登录");
        final int TWIDTH = 80;
        final int THEIGHT = 30;
    
        studentButton.setPrefSize(TWIDTH, THEIGHT);
        studentButton.setStyle("-fx-padding: 0");
        studentButton.setStyle("-fx-background-insets: 0");//去除边框样式
        studentButton.setOnAction(e->{
            if(sfLB == 2)
            {
                try {
                    InfoInputDialog infoID = new InfoInputDialog();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else
                CommonDialog.WarningDialog("只有《管理员》才能设置系统资料 !");
        });
        

        lessonButton.setPrefSize(TWIDTH, THEIGHT);
        lessonButton.setStyle("-fx-padding: 0");
        lessonButton.setStyle("-fx-background-insets: 0");
        lessonButton.setOnAction(e->{
            if(sfLB == 0)
            {    SelectLessonDialog seleID = new SelectLessonDialog(sfID, sfName);  }
            else
                CommonDialog.WarningDialog("只有《学生》才能选课 !"); 	
        });
        
        inputScoreButton.setPrefSize(TWIDTH, THEIGHT);
        inputScoreButton.setStyle("-fx-padding: 0");
        inputScoreButton.setStyle("-fx-background-insets: 0");
        inputScoreButton.setOnAction(e->{
            if(sfLB == 1)
            {    ScoreInputDialog seleID = new ScoreInputDialog(sfID, sfName);  }
            else
                CommonDialog.WarningDialog("只有《教师》才能录入学生课程成绩 !"); 	
        });

        lessonAnaButton.setPrefSize(TWIDTH, THEIGHT);
        lessonAnaButton.setStyle("-fx-padding: 0");
        lessonAnaButton.setStyle("-fx-background-insets: 0");
        lessonAnaButton.setOnAction(e->{
            if(sfLB == 1)
            {    QueryScoreGraphicsDialog seleID = new QueryScoreGraphicsDialog(sfID, sfName);  }
            else
                CommonDialog.WarningDialog("只有《教师》才能查询数据 !");
        });
        
        operatorButton.setPrefSize(TWIDTH, THEIGHT);
        operatorButton.setStyle("-fx-padding: 0");
        operatorButton.setStyle("-fx-background-insets: 0");
        operatorButton.setOnAction(e->{
            if(sfLB == 2)
            {    PasswordInputDialog seleID = new PasswordInputDialog();  }
            else
                CommonDialog.WarningDialog("只有《管理员》才能设置学生，教师，教务员的登录密码 !"); 	
        });

        classButton.setPrefSize(TWIDTH, THEIGHT);
        classButton.setStyle("-fx-padding: 0");
        classButton.setStyle("-fx-background-insets: 0");
        classButton.setOnAction(e->{
            try {
                ReadLessonDialog seleID = new ReadLessonDialog();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });



        loginOut.setPrefSize(TWIDTH, THEIGHT);
        loginOut.setStyle("-fx-padding: 0");
        loginOut.setStyle("-fx-background-insets: 0");
        loginOut.setOnAction(e->{

            if (loginOut.getText().equals("登录"))
            {
                loginDialog = new LoginDialog(this, screenWid, screenHei);
                try {
                    CommonDialog.NoticeDialog(sfID, sfName);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            else if (loginOut.getText().equals("退出"))
            {
                loginOut.setText("登录");
                sfLB = -1;
                sfID = "";
                user.setText("请登录后使用本系统！");
            }

        });

        gradeButton.setPrefSize(TWIDTH, THEIGHT);
        gradeButton.setStyle("-fx-padding: 0");
        gradeButton.setStyle("-fx-background-insets: 0");
        gradeButton.setOnAction(e->{
            if(sfLB == 0)
            {    QueryLessonScoreDialog qryID = new QueryLessonScoreDialog(sfID, sfName);  }
            else
                CommonDialog.WarningDialog("只有《学生》才能查询数据 !");
        });

        studentButton.setTooltip(new Tooltip("学生花名册"));
        lessonButton.setTooltip(new Tooltip("课程表"));
        operatorButton.setTooltip(new Tooltip("用户设置"));
        
        toolBar = new HBox(studentButton,operatorButton, inputScoreButton, lessonAnaButton,
                lessonButton, gradeButton, classButton, loginOut);
    }
}


