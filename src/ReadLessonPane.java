import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.sql.*;

public class ReadLessonPane  extends BorderPane
{

    private final TableView<ReadLesson.ShowTime> LessTable = new TableView<>();

    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    private final ObservableList<ReadLesson.ShowTime> LessTime = FXCollections.observableArrayList();
    String sql;
    private void loadTimeFromLesson()
    {

        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();

            LessTime.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {
                String order=rs.getString("Order"); if(order==null) order="";
                String less1=rs.getString("Mon"); if(less1==null) less1="";
                String less2=rs.getString("Tue"); if(less2==null) less2="";
                String less3=rs.getString("Wed"); if(less3==null) less3="";
                String less4=rs.getString("Thu"); if(less4==null) less4="";
                String less5=rs.getString("Fri"); if(less5==null) less5="";
                LessTime.add(new ReadLesson.ShowTime(order,less1,less2,less3,less4,less5));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {   e.printStackTrace();
        }
    }

    public ReadLessonPane(String sql) throws SQLException {
        this.sql = sql;
        loadTimeFromLesson(); //  �����ݿ���ȡ Lesson ���ݸ��� data �б�


        LessTable.setEditable(false);  // ��֧���޸�

        Callback<TableColumn<ReadLesson.ShowTime, String>, TableCell<ReadLesson.ShowTime, String>> cellStringFactory =
                new Callback<TableColumn<ReadLesson.ShowTime, String>, TableCell<ReadLesson.ShowTime, String>>()
                {   public TableCell<ReadLesson.ShowTime, String> call(TableColumn p)
                {   return new EditingStringCell<ReadLesson.ShowTime>();
                }
                };

        TableColumn<ReadLesson.ShowTime, String> order = new TableColumn<>("����");
        order.setMinWidth(40);
        order.setCellValueFactory(new PropertyValueFactory<ReadLesson.ShowTime, String>("order"));
        order.setCellFactory(cellStringFactory);

        TableColumn<ReadLesson.ShowTime, String> less1 = new TableColumn<>("����һ");
        less1.setMinWidth(120);
        less1.setCellValueFactory(new PropertyValueFactory<ReadLesson.ShowTime, String>("less1"));
        less1.setCellFactory(cellStringFactory);

        TableColumn<ReadLesson.ShowTime, String> less2 = new TableColumn<>("���ڶ�");
        less2.setMinWidth(120);
        less2.setCellValueFactory(new PropertyValueFactory<ReadLesson.ShowTime, String>("less2"));
        less2.setCellFactory(cellStringFactory);

        TableColumn<ReadLesson.ShowTime, String> less3 = new TableColumn<>("������");
        less3.setMinWidth(120);
        less3.setCellValueFactory(new PropertyValueFactory<ReadLesson.ShowTime, String>("less3"));
        less3.setCellFactory(cellStringFactory);

        TableColumn<ReadLesson.ShowTime, String> less4 = new TableColumn<>("������");
        less4.setMinWidth(120);
        less4.setCellValueFactory(new PropertyValueFactory<ReadLesson.ShowTime, String>("less4"));
        less4.setCellFactory(cellStringFactory);

        TableColumn<ReadLesson.ShowTime, String> less5 = new TableColumn<>("������");
        less5.setMinWidth(120);
        less5.setCellValueFactory(new PropertyValueFactory<ReadLesson.ShowTime, String>("less5"));
        less5.setCellFactory(cellStringFactory);

        LessTable.setItems(LessTime);
        LessTable.getColumns().addAll(order, less1, less2, less3, less4, less5);

        ScrollPane spp = new ScrollPane(LessTable);
        spp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        spp.setFitToHeight(true);
        spp.setFitToWidth(true);
        setCenter(spp);
    }


}
