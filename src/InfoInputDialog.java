import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Modality;

import java.sql.SQLException;

class InfoInputDialog extends Stage
{   StudentTabPane stp;
    TeacherTabPane ttp;
    LessonTabPane ltp;
    OperatorTabPane otp;

    public InfoInputDialog() throws SQLException {
        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);

    
        TabPane tabPane = new TabPane();
        
        Tab stuTab = new Tab();
        stuTab.setText(" 学生花名册 ");
        stuTab.setClosable(false);
        stuTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        stp = new StudentTabPane();
        stuTab.setContent(stp);
        BorderPane infoBP = new BorderPane();


        Tab teaTab = new Tab(" 教师资料 ");
        teaTab.setClosable(false);
        
        tabPane.setStyle("-fx-tab-min-width:90px; -fx-tab-max-width:9px; -fx-tab-min-height:30px; -fx-tab-max-height:30px;");
        tabPane.setStyle("-fx-background-color:lightgray;");
        teaTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20;");
        ttp = new TeacherTabPane();
        teaTab.setContent(ttp);
        
        
        Tab lessTab = new Tab(" 课程列表 ");
        lessTab.setClosable(false);
        lessTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20;");
        ltp = new LessonTabPane(ttp);  // 需要教师资料数据
        lessTab.setContent(ltp);
        
        Tab managTab = new Tab(" 管理员 ");
        managTab.setClosable(false);
        managTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20;");
        otp = new OperatorTabPane();
        managTab.setContent(otp);
        
        tabPane.getTabs().addAll(stuTab, teaTab, lessTab, managTab);
        tabPane.setPadding(new Insets(4, 0, 0, 0));
        
        infoBP.setCenter(tabPane);

        Button exitButton = new Button("退出", new ImageView(new Image("image/quit.png")));
        
        exitButton.setOnAction(e->{
            stp.updateStudentDataBase();	
            ttp.updateTeacherDataBase();
            ltp.updateLessonDataBase();
            otp.updateOperatorDataBase();
            close();  // 关闭资料输入窗口
        });


        HBox hb = new HBox();
        hb.getChildren().addAll(exitButton);

        hb.setAlignment(Pos.BOTTOM_RIGHT);
        hb.setMargin(exitButton, new Insets(8, 4, 4, 10));

        BorderPane bp = new BorderPane(infoBP);
        bp.setBottom(hb);
        Scene scene=new Scene(bp, 730, 510);

        setTitle("资料录入");
        setScene(scene);
//        setX(330);
//        setY(120);
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
}
