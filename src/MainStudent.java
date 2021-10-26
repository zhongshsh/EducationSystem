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
            //  ���� JDBC-ODBC ����������
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); // ���ݿ�װ��
		}
		catch(Exception e)  
		{     System.out.println("���ݿ�װ��ʧ��: " + e.toString());			
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
        stage.setTitle("ѧ����Ϣ����ϵͳ");
        stage.focusedProperty().addListener(e->{
        	if(sfID.length() > 0)
        	{   String lxStr = "";
        	    if(sfLB == 0)
        	        lxStr = "ѧ��";
        	    if(sfLB == 1)
        	        lxStr = "��ʦ";
        	    if(sfLB == 2)
        	        lxStr = "����";
                user.setText("��ӭ"+lxStr+sfID+": "+sfName+"ʹ�ñ�ϵͳ��");
                loginOut.setText("�˳�");
        	}else {
                user.setText("���¼��ʹ�ñ�ϵͳ��");
                loginOut.setText("��¼");
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
        //ͼƬ����Ӧ���ڴ�С
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
            if(itemName.equals(" ����¼�� ")) 
            {   if(sfLB == 2) {
                try {
                    new InfoInputDialog();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
                else
                    CommonDialog.WarningDialog("ֻ�С�����Ա����������ϵͳ���� !"); 	
            }
            if(itemName.equals(" ѧ��ѡ�� "))
            {   if(sfLB == 0)
                    new SelectLessonDialog(sfID, sfName);
                else
                    CommonDialog.WarningDialog("ֻ�С�ѧ��������ѡ�� !");
            }
            if(itemName.equals(" �ɼ�¼�� "))
            {   if(sfLB == 1)
                    new ScoreInputDialog(sfID, sfName);
                else
                    CommonDialog.WarningDialog("ֻ�С���ʦ������¼��ѧ���γ̳ɼ� !");
            }
            if(itemName.equals(" ��ѯ������ "))
            {   if(sfLB == 0)
                    new QueryLessonScoreDialog(sfID, sfName);  
                else if(sfLB == 1)
                    new QueryScoreGraphicsDialog(sfID, sfName);
                else if(sfLB == 2)
                    new PasswordInputDialog();
            }
            if(itemName.equals(" �����޸� "))
            {   new UpdatePasswordDialog(sfLB, sfID);
            }
            if(itemName.equals(" �汾 "))
            {   new ShowVersionDialog();
            }
        }
    }
      
    private void CreateToolBar()  // ����������
	{
        Button studentButton = new Button("����¼��");
        Button lessonButton = new Button("ѡ��");
        Button inputScoreButton = new Button("����ɼ�");
        Button lessonAnaButton = new Button("�γ̷���");
        Button operatorButton = new Button("��������");
        Button classButton = new Button("��ѯ�α�");
        Button gradeButton = new Button("��ѯ�ɼ�");

        loginOut = new Button("��¼");
        final int TWIDTH = 80;
        final int THEIGHT = 30;
    
        studentButton.setPrefSize(TWIDTH, THEIGHT);
        studentButton.setStyle("-fx-padding: 0");
        studentButton.setStyle("-fx-background-insets: 0");//ȥ���߿���ʽ
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
                CommonDialog.WarningDialog("ֻ�С�����Ա����������ϵͳ���� !");
        });
        

        lessonButton.setPrefSize(TWIDTH, THEIGHT);
        lessonButton.setStyle("-fx-padding: 0");
        lessonButton.setStyle("-fx-background-insets: 0");
        lessonButton.setOnAction(e->{
            if(sfLB == 0)
            {    SelectLessonDialog seleID = new SelectLessonDialog(sfID, sfName);  }
            else
                CommonDialog.WarningDialog("ֻ�С�ѧ��������ѡ�� !"); 	
        });
        
        inputScoreButton.setPrefSize(TWIDTH, THEIGHT);
        inputScoreButton.setStyle("-fx-padding: 0");
        inputScoreButton.setStyle("-fx-background-insets: 0");
        inputScoreButton.setOnAction(e->{
            if(sfLB == 1)
            {    ScoreInputDialog seleID = new ScoreInputDialog(sfID, sfName);  }
            else
                CommonDialog.WarningDialog("ֻ�С���ʦ������¼��ѧ���γ̳ɼ� !"); 	
        });

        lessonAnaButton.setPrefSize(TWIDTH, THEIGHT);
        lessonAnaButton.setStyle("-fx-padding: 0");
        lessonAnaButton.setStyle("-fx-background-insets: 0");
        lessonAnaButton.setOnAction(e->{
            if(sfLB == 1)
            {    QueryScoreGraphicsDialog seleID = new QueryScoreGraphicsDialog(sfID, sfName);  }
            else
                CommonDialog.WarningDialog("ֻ�С���ʦ�����ܲ�ѯ���� !");
        });
        
        operatorButton.setPrefSize(TWIDTH, THEIGHT);
        operatorButton.setStyle("-fx-padding: 0");
        operatorButton.setStyle("-fx-background-insets: 0");
        operatorButton.setOnAction(e->{
            if(sfLB == 2)
            {    PasswordInputDialog seleID = new PasswordInputDialog();  }
            else
                CommonDialog.WarningDialog("ֻ�С�����Ա����������ѧ������ʦ������Ա�ĵ�¼���� !"); 	
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

            if (loginOut.getText().equals("��¼"))
            {
                loginDialog = new LoginDialog(this, screenWid, screenHei);
                try {
                    CommonDialog.NoticeDialog(sfID, sfName);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            else if (loginOut.getText().equals("�˳�"))
            {
                loginOut.setText("��¼");
                sfLB = -1;
                sfID = "";
                user.setText("���¼��ʹ�ñ�ϵͳ��");
            }

        });

        gradeButton.setPrefSize(TWIDTH, THEIGHT);
        gradeButton.setStyle("-fx-padding: 0");
        gradeButton.setStyle("-fx-background-insets: 0");
        gradeButton.setOnAction(e->{
            if(sfLB == 0)
            {    QueryLessonScoreDialog qryID = new QueryLessonScoreDialog(sfID, sfName);  }
            else
                CommonDialog.WarningDialog("ֻ�С�ѧ�������ܲ�ѯ���� !");
        });

        studentButton.setTooltip(new Tooltip("ѧ��������"));
        lessonButton.setTooltip(new Tooltip("�γ̱�"));
        operatorButton.setTooltip(new Tooltip("�û�����"));
        
        toolBar = new HBox(studentButton,operatorButton, inputScoreButton, lessonAnaButton,
                lessonButton, gradeButton, classButton, loginOut);
    }
}


