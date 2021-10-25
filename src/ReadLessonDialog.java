import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.SQLException;
/*
主面板。
ReadLesson完成数据处理；
ReadLessonPane完成数据读取和展示。
 */

public class ReadLessonDialog extends Stage
{   ReadLessonPane rl;
    ReadLessonPane ll;
    public ReadLessonDialog() throws SQLException {
        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);
        BorderPane infoBP = new BorderPane();

        TabPane tabPane = new TabPane();

        Tab stuTab = new Tab();
        stuTab.setText(" 上半学期课程时间表 ");
        stuTab.setClosable(false);
        stuTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        rl = new ReadLessonPane("SELECT * FROM showLessonTime");
        stuTab.setContent(rl);

        Tab lasTab = new Tab();
        lasTab.setText(" 下半学期课程时间表 ");
        lasTab.setClosable(false);
        lasTab.setStyle("-fx-font-family:kaiti; -fx-font-size: 14;-fx-background-radius: 20");
        ll = new ReadLessonPane("SELECT * FROM showLessonTimeLast");
        lasTab.setContent(ll);

        tabPane.getTabs().addAll(stuTab, lasTab);
        tabPane.setPadding(new Insets(4, 0, 0, 0));

        infoBP.setCenter(tabPane);
        Button exitButton = new Button("退出", new ImageView(new Image("image/quit.png")));

        exitButton.setOnAction(e->{
            close();  // 关闭资料输入窗口
        });

        infoBP.setBottom(exitButton);
        infoBP.setAlignment(exitButton, Pos.CENTER_RIGHT);
        infoBP.setMargin(exitButton, new Insets(8, 20, 8, 10));
        Scene scene=new Scene(infoBP, 730, 510);
        setTitle("课程时间安排表");
        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
        showAndWait();
    }
    }
