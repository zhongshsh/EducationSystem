import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class ShowVersionDialog extends Stage
{   public ShowVersionDialog()
    {
        Image backGround = new Image("/image/logo.jpg");
        getIcons().add(backGround);
        ImageView bzImage = new ImageView(new Image("image/6.png"));
        Label sysNameLabel = new Label("Student Infomation System");
        Label verLabel = new Label("Copyright@ 2020~2030");
        Label copyRightLabel = new Label("This Program is protected by Copyright in ZSU");
	    Button quitButton = new Button("   OK   ");
	    quitButton.setOnAction(e->{ close(); });
	
	    VBox vb = new VBox();
        vb.setSpacing(10);
        
        HBox hb1 = new HBox();
        hb1.getChildren().addAll(bzImage, sysNameLabel);
        hb1.setMargin(sysNameLabel, new Insets(40,0,0,20));
                        
        vb.getChildren().addAll(hb1, verLabel, copyRightLabel);
        vb.setAlignment(Pos.CENTER);
        BorderPane bPane = new BorderPane();
        bPane.setCenter(vb);
        bPane.setBottom(quitButton);
        bPane.setPadding(new Insets(15,40,20,40));
        bPane.setAlignment(quitButton, Pos.CENTER);

        setX(330);
        setY(120);
        initModality(Modality.APPLICATION_MODAL);
        setScene(new Scene(bPane, 480, 270));
        
        setTitle("°æ±¾");
        showAndWait();
	}
}