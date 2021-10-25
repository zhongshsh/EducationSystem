import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.sql.*;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

class OperatorTabPane extends BorderPane 
{   private final TableView<Operator> opTable = new TableView<>();
    private final ObservableList<Operator> opData = FXCollections.observableArrayList();
    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    
    public OperatorTabPane() 
    {   loadDataFromOperator(); //  从数据库提取 Operator 数据更新 data 列表
        
        Callback<TableColumn<Operator, String>, TableCell<Operator, String>> cellStringFactory = 
        		new Callback<TableColumn<Operator, String>, TableCell<Operator, String>>() 
                {   public TableCell<Operator, String> call(TableColumn p) 
                    {   return new EditingStringCell<Operator>();  
                    }  
                };  
                
        TableColumn<Operator, String> numCol = new TableColumn<>("教务号");
        numCol.setMinWidth(80);
        numCol.setCellValueFactory(new PropertyValueFactory<Operator, String>("uid"));
        numCol.setCellFactory(cellStringFactory);
        
        TableColumn<Operator, String> nameCol = new TableColumn<>("名字");
        nameCol.setMinWidth(120);
        nameCol.setCellValueFactory(new PropertyValueFactory<Operator, String>("name"));
        nameCol.setCellFactory(cellStringFactory);
        
        TableColumn<Operator, String> passwdCol = new TableColumn<>("登录密码");
        passwdCol.setMinWidth(120);
        passwdCol.setCellValueFactory(new PropertyValueFactory<Operator, String>("passwd"));
        passwdCol.setCellFactory(cellStringFactory);
        
               
        TableColumn<Operator, String> bzCol = new TableColumn<>("备注说明");
        bzCol.setMinWidth(380);
        bzCol.setCellValueFactory(new PropertyValueFactory<Operator, String>("bz"));
        bzCol.setCellFactory(cellStringFactory);
        
        opTable.setItems(opData);
        opTable.getColumns().addAll(numCol, nameCol, passwdCol, bzCol);
        opTable.setEditable(false);
      
        final Button addButton = new Button("Add", new ImageView(new Image("image/add.png")));
        final Button deleteButton = new Button("Delete",new ImageView(new Image("image/cancel.png")));
        final Button editButton = new Button("Edit", new ImageView(new Image("image/edit.png")));

        deleteButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Operator p = opTable.getSelectionModel().getSelectedItem();
        		int pos = opTable.getSelectionModel().getSelectedIndex();
        		
        		if(p != null)
        	    {   String delName = p.getName();
        			if(CommonDialog.ConfirmDialog("温馨提示", "确认要删除名字为 《"+delName+"》 的教务员数据吗？"))
        			{   if(p.getDTag() != 2)  // 不是新增加的记录，需要删除数据库中的记录
        			        deleteDataFromOperator(p.getUid());      			
        		        opData.remove(pos);
        		    }
        		}
        		else
        		    CommonDialog.WarningDialog("你没有选中需要删除的记录"); 
        	}
            
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{
        	    Operator op = new Operator();
                AddDialog ad = new AddDialog(op);
        	    if (op.dtag == 2) {
                    opData.add(op);
                    opTable.refresh();
                    opTable.getSelectionModel().selectLast();
                    opTable.scrollTo(opData.size());
                    opTable.requestFocus();
                    updateOperatorDataBase();
                }
        	}
            
        });

        editButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e)
            {	Operator p = opTable.getSelectionModel().getSelectedItem();
                if(p != null)
                {	EditDialog addeditD = new EditDialog(p);
                    updateOperatorDataBase();
                }
                else
                    CommonDialog.WarningDialog("你没有选中需要修改的记录");
            }
        });

        final HBox hb = new HBox();
        hb.getChildren().addAll(addButton, editButton, deleteButton);
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setMargin(addButton, new Insets(0, 0, 0, 10));
                                
        ScrollPane spp = new ScrollPane(opTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setFitToHeight(true);
        spp.setFitToWidth(true);
        setCenter(spp);
        setBottom(hb);
        setMargin(hb, new Insets(5, 10, 5, 10));
        
    }

    public class AddDialog
    {   Stage editStage;

        Label numLB = new Label("    教务号：");
        TextField numTF = new TextField();

        Label nameLB = new Label("       名字：");
        TextField nameTF = new TextField();

        Label pwLB = new Label("登录密码：");
        TextField pwTF = new TextField();

        Label otherLB = new Label("备注说明：");
        TextField otherTF = new TextField();



        Button EditButton;
        Operator opp;

        public AddDialog(Operator p)
        {
            opp = p;
            numTF.setPrefWidth(150);
            nameTF.setPrefWidth(150);
            pwTF.setPrefWidth(150);
            otherTF.setPrefWidth(300);

            EditButton = new Button("添加");
            EditButton.setStyle("-fx-text-fill:red");
            EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e) {

                    opp.setUid(numTF.getText());
                    opp.setName(nameTF.getText());
                    opp.setPasswd(pwTF.getText());
                    opp.setBz(otherTF.getText());
                    if (nullData(opp)) {
                        opp.dtag = 2;
                        opTable.refresh();
                        editStage.close();
                    }
                }
            });

            EditButton.setPrefWidth(60);
            Button cancelButton = new Button("取消");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {
                    editStage.close();
                }
            });

            HBox hb1 = new HBox();
            hb1.getChildren().addAll(numLB, numTF);
            hb1.setMargin(numLB, new Insets(3,0,0,0));


            HBox hb2 = new HBox();
            hb2.getChildren().addAll( nameLB, nameTF);
            hb2.setMargin(nameLB, new Insets(3,0,0,0));

            HBox hb3 = new HBox();
            hb3.getChildren().addAll(pwLB, pwTF);
            hb3.setMargin(pwLB, new Insets(3,0,0,0));

            HBox hb5 = new HBox();
            hb5.getChildren().addAll(otherLB, otherTF);
            hb5.setMargin(otherLB, new Insets(3,0,0,0));

            HBox hb4 = new HBox();
            hb4.setSpacing(20);
            hb4.setAlignment(Pos.CENTER);
            hb4.getChildren().addAll(EditButton, cancelButton);

            VBox vbox = new VBox();
            vbox.setSpacing(15);
            vbox.setPadding(new Insets(30, 10, 10, 30));
            vbox.getChildren().addAll(hb1, hb2, hb3, hb5);
            vbox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 1;" + "-fx-border-insets: 10;"
                    + "-fx-border-radius: 10;" + "-fx-border-color: gray lightgray lightgray gray;");

            editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL);

            BorderPane bPane = new BorderPane();
            bPane.setCenter(vbox);
            bPane.setBottom(hb4);
            bPane.setPadding(new Insets(0,0,15,0));
            editStage.setScene(new Scene(bPane, 540, 220));
            editStage.setTitle("添加学生资料");
            editStage.showAndWait();
        }
    }

    public class EditDialog
    {   Stage editStage;

        Label numLB = new Label("    教务号：");
        TextField numTF = new TextField();

        Label nameLB = new Label("       名字：");
        TextField nameTF = new TextField();

        Label pwLB = new Label("登录密码：");
        TextField pwTF = new TextField();

        Label otherLB = new Label("备注说明：");
        TextField otherTF = new TextField();



        Button EditButton;
        Operator opp;

        public EditDialog(Operator p)
        {
            opp = p;
            numTF.setPrefWidth(150);
            nameTF.setPrefWidth(150);
            pwTF.setPrefWidth(150);
            otherTF.setPrefWidth(300);

            numTF.setDisable(true);
            nameTF.setDisable(true);
            numTF.setText(opp.getUid());
            nameTF.setText(opp.getName());
            pwTF.setText(opp.getPasswd());
            otherTF.setText(opp.getBz());


            EditButton = new Button("更改");
            EditButton.setStyle("-fx-text-fill:red");
            EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e) {

                    opp.setUid(numTF.getText());
                    opp.setName(nameTF.getText());
                    opp.setPasswd(pwTF.getText());
                    opp.setBz(otherTF.getText());
                    if (nullData(opp)) {
                        opp.dtag = 1;
                        opTable.refresh();
                        editStage.close();
                    }
                }
            });

            EditButton.setPrefWidth(60);
            Button cancelButton = new Button("取消");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {
                    editStage.close();
                }
            });

            HBox hb1 = new HBox();
            hb1.getChildren().addAll(numLB, numTF);
            hb1.setMargin(numLB, new Insets(3,0,0,0));


            HBox hb2 = new HBox();
            hb2.getChildren().addAll( nameLB, nameTF);
            hb2.setMargin(nameLB, new Insets(3,0,0,0));

            HBox hb3 = new HBox();
            hb3.getChildren().addAll(pwLB, pwTF);
            hb3.setMargin(pwLB, new Insets(3,0,0,0));

            HBox hb5 = new HBox();
            hb5.getChildren().addAll(otherLB, otherTF);
            hb5.setMargin(otherLB, new Insets(3,0,0,0));

            HBox hb4 = new HBox();
            hb4.setSpacing(20);
            hb4.setAlignment(Pos.CENTER);
            hb4.getChildren().addAll(EditButton, cancelButton);

            VBox vbox = new VBox();
            vbox.setSpacing(15);
            vbox.setPadding(new Insets(30, 10, 10, 30));
            vbox.getChildren().addAll(hb1, hb2, hb3, hb5);
            vbox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 1;" + "-fx-border-insets: 10;"
                    + "-fx-border-radius: 10;" + "-fx-border-color: gray lightgray lightgray gray;");

            editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL);

            BorderPane bPane = new BorderPane();
            bPane.setCenter(vbox);
            bPane.setBottom(hb4);
            bPane.setPadding(new Insets(0,0,15,0));
            editStage.setScene(new Scene(bPane, 540, 220));
            editStage.setTitle("添加学生资料");
            editStage.showAndWait();
        }
    }


    private boolean nullData(Operator pp)
    {   String uNum = pp.getUid().trim();
        String uName = pp.getName().trim();
    	
    	if(uNum.length() == 0)
    	{   CommonDialog.WarningDialog("教务员（ "+uName+"）编号为空！");
    	    return false;
    	}
    	if(uName.length() == 0)
    	{   CommonDialog.WarningDialog("教务员（"+uNum+"）姓名为空！");
    	    return false;
    	}
    	if(pp.getPasswd().trim().length() == 0) {
            CommonDialog.WarningDialog("教务员（" + uNum + " - " + uName + "）密码不能为空！");
            return false;
        }
    	return true;
    }
        
    private void deleteDataFromOperator(String numstr)
    {   try
        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt = conn.createStatement();
            String deleteSql = "DELETE FROM Operator WHERE uid='"+numstr+"'";
            int count = stmt.executeUpdate(deleteSql); 
            System.out.println("删除了 Operator 表的《"+numstr+"》"+count+"条记录");
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public void updateOperatorDataBase()
    {   Operator up;
        String unum, uname, upasswd, ubz;
        int udtag;
               
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            for(int i=0;i<opData.size();i++)
            {   up = (Operator)opData.get(i);
                udtag = up.getDTag();
                unum = up.getUid();
                uname = up.getName();
                upasswd = up.getPasswd();
                ubz = up.getBz();
                if(udtag == 2)  // 新增加记录
                {   String insertSql="INSERT INTO Operator(uid,name,passwd,bz) "+
                                     "VALUES('"+unum+"','"+uname+"','"+upasswd+"','"+ubz+"')";
                    int count=stmt.executeUpdate(insertSql);
                    System.out.println("添加 "+ count+" 条记录到 Operator 表中");
                }
                
                if(udtag == 1)  // 更改过的记录
                {   String updateSql="UPDATE Operator SET name='"+uname+"',passwd='"+upasswd+"',bz='"+ubz+"' WHERE uid='"+unum+"'";
                    int count=stmt.executeUpdate(updateSql);
                    System.out.println("修改 "+ count+" 条记录到 Operator 表中");
                }
            }                 
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
        
    private void loadDataFromOperator()
    {   String sql="SELECT uid,name,passwd,bz FROM operator";
               
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            opData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid"); if(no==null) no="";
                String name=rs.getString("name"); if(name==null) name="";
                String passwd=rs.getString("passwd"); if(passwd==null) passwd="";
                String bz=rs.getString("bz"); if(bz==null) bz="";
                opData.add(new Operator(no, name, passwd, bz,0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public static class Operator
    {   private final SimpleStringProperty uid;
    	private final SimpleStringProperty name;
    	private final SimpleStringProperty passwd;
        private final SimpleStringProperty bz;
        private int dtag; // 0 无需更改      1 编辑更改过的数据   2  新增加的记录

        private Operator()
        {   this.uid = new SimpleStringProperty("");
            this.name = new SimpleStringProperty("");
            this.passwd = new SimpleStringProperty("");
            this.bz = new SimpleStringProperty("");
            dtag = 0;
        }

        private Operator(String uUid, String uName, String uPasswd, String uBz, int udtag) 
        {   this.uid = new SimpleStringProperty(uUid);
            this.name = new SimpleStringProperty(uName);
            this.passwd = new SimpleStringProperty(uPasswd);
            this.bz = new SimpleStringProperty(uBz);
            dtag = udtag;
        }
        
        private Operator(String uUid, String uName, String uPasswd, String uBz) 
        {   this(uUid, uName, uPasswd, uBz, 0);
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

        public String getPasswd() 
        {   return passwd.get();
        }

        public void setPasswd(String uPasswd) 
        {   passwd.set(uPasswd);
        }
        
        public String getBz() 
        {   return bz.get();
        }

        public void setBz(String uBz) 
        {   bz.set(uBz);
        }
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
}
