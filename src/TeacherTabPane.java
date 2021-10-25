import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;

class TeacherTabPane extends BorderPane 
{   private final TableView<Teacher> teaTable = new TableView<>();

    final ObservableList<Teacher> teaData = FXCollections.observableArrayList();

    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";
    public TeacherTabPane() 
    {   loadDataFromTeacher(); //  �����ݿ���ȡ teacher ���ݸ��� data �б�
        
        Callback<TableColumn<Teacher, String>, TableCell<Teacher, String>> cellStringFactory = 
        		new Callback<TableColumn<Teacher, String>, TableCell<Teacher, String>>() 
                {   public TableCell<Teacher, String> call(TableColumn p) 
                    {   return new EditingStringCell<Teacher>();  
                    }  
                };  
                
        TableColumn<Teacher, String> numCol = new TableColumn<>("�̹���");
        numCol.setMinWidth(80);
        numCol.setCellValueFactory(new PropertyValueFactory<Teacher, String>("uid"));
        numCol.setCellFactory(cellStringFactory);

        
        TableColumn<Teacher, String> nameCol = new TableColumn<>("����");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<Teacher, String>("name"));
        nameCol.setCellFactory(cellStringFactory);

               
        TableColumn<Teacher, String> zcCol = new TableColumn<>("ְ��");
        zcCol.setMinWidth(100);
        zcCol.setCellValueFactory(new PropertyValueFactory<Teacher, String>("zc"));
        zcCol.setCellFactory(new Callback<TableColumn <Teacher, String>, TableCell <Teacher,String>>() 
        {   public TableCell <Teacher,String> call(TableColumn <Teacher, String> param)
            {   ObservableList<String> zcList = FXCollections.observableArrayList("����", "������", "��ʦ", "�о�Ա");
        	    ComboBoxTableCell<Teacher, String> cell = new ComboBoxTableCell<>(zcList);
        	    return cell;
            }
        });

        
        TableColumn<Teacher, String> phoneCol = new TableColumn<>("��ϵ�绰");
        phoneCol.setMinWidth(120);
        phoneCol.setCellValueFactory(new PropertyValueFactory<Teacher, String>("phone"));
        phoneCol.setCellFactory(cellStringFactory);

        
        TableColumn<Teacher, String> yjfxCol = new TableColumn<>("�о�����");
        yjfxCol.setMinWidth(300);
        yjfxCol.setCellValueFactory(new PropertyValueFactory<Teacher, String>("yjfx"));
        yjfxCol.setCellFactory(cellStringFactory);

        
        teaTable.setItems(teaData);
        teaTable.getColumns().addAll(numCol, nameCol, zcCol, phoneCol, yjfxCol);
        teaTable.setEditable(false);

        final Button addButton = new Button("Add", new ImageView(new Image("image/add.png")));
        final Button deleteButton = new Button("Delete",new ImageView(new Image("image/cancel.png")));
        final Button editButton = new Button("Edit", new ImageView(new Image("image/edit.png")));


        editButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e)
            {	Teacher p = teaTable.getSelectionModel().getSelectedItem();
                if(p != null)
                {	EditDialog addeditD = new EditDialog(p);
                }
                else
                    CommonDialog.WarningDialog("��û��ѡ����Ҫ�޸ĵļ�¼");
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Teacher p = teaTable.getSelectionModel().getSelectedItem();
        		int pos = teaTable.getSelectionModel().getSelectedIndex();
        		
        		if(p != null)
        	    {   String delName = p.getName();
        			if(CommonDialog.ConfirmDialog("��ܰ��ʾ", "ȷ��Ҫɾ������Ϊ ��"+delName+"�� �Ľ�ʦ������"))
        			{   if(p.getDTag() != 2)  // ���������ӵļ�¼����Ҫɾ�����ݿ��еļ�¼
        			        deleteDataFromTeacher(p.getUid());      			
        		        teaData.remove(pos);
        		    }
        		}
        		else
        		    CommonDialog.WarningDialog("��û��ѡ����Ҫɾ���ļ�¼"); 
        	}
            
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{
        	    Teacher t = new Teacher();
                AddDialog addeditD = new AddDialog(t);
                if (t.dtag == 2) {
                    teaData.add(t);
                    teaTable.refresh();
                    teaTable.getSelectionModel().selectLast();
                    teaTable.scrollTo(teaData.size());
                    teaTable.requestFocus();
                }
        	}
            
        });
                
        final HBox hb = new HBox();    
        hb.getChildren().addAll(addButton, editButton, deleteButton);
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setMargin(addButton, new Insets(0, 0, 0, 10));
                                
        ScrollPane spp = new ScrollPane(teaTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setFitToHeight(true);
        spp.setFitToWidth(true);
        setCenter(spp);
        setBottom(hb);
        setMargin(hb, new Insets(5, 10, 5, 10));
        
    }

    private class AddDialog
    {   Stage editStage;

        Label numLB = new Label("�̹��ţ�");
        TextField numTF = new TextField();

        Label nameLB = new Label("������");
        TextField nameTF = new TextField();

        Label zcLB = new Label("ְ�ƣ�");
        RadioButton jsRB = new RadioButton("����");
        RadioButton fjsRB = new RadioButton("������");
        RadioButton jRB = new RadioButton("��ʦ");
        ToggleGroup sexTG = new ToggleGroup();


        Label phoneLB = new Label("��ϵ�绰��");
        TextField phoneTF = new TextField();

        Label majorLB = new Label("�о�����");
        TextField majorTF = new TextField();


        Button EditButton;
        Teacher opp;

        public AddDialog(Teacher p)
        {   opp = p;
            numTF.setPrefWidth(80);
            nameTF.setPrefWidth(100);
            jsRB.setToggleGroup(sexTG);
            fjsRB.setToggleGroup(sexTG);
            jRB.setToggleGroup(sexTG);
            majorTF.setPrefWidth(240);
            phoneTF.setPrefWidth(120);

            EditButton = new Button("����");
            EditButton.setStyle("-fx-text-fill:red");
            EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {
                    opp.setUid(numTF.getText());
                    opp.setName(nameTF.getText());
                    if(jsRB.isSelected())
                        opp.setZc("����");
                    if(fjsRB.isSelected())
                        opp.setZc("������");
                    if(jRB.isSelected())
                        opp.setZc("��ʦ");
                    opp.setPhone((String)phoneTF.getText());
                    opp.setYjfx((String)majorTF.getText());
                    if (nullData(opp)) {
                        opp.setDTag(2);
                        teaTable.refresh();
                        editStage.close();
                    }
                }
            });

            EditButton.setPrefWidth(60);
            Button cancelButton = new Button("ȡ��");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {	editStage.close();
                }
            });

            HBox hb1 = new HBox();
            hb1.getChildren().addAll(numLB, numTF, nameLB, nameTF);
            hb1.setMargin(numLB, new Insets(3,0,0,0));
            hb1.setMargin(nameLB, new Insets(3,0,0,10));


            HBox hb2 = new HBox();
            hb2.getChildren().addAll(zcLB, jsRB, fjsRB, jRB);
            hb2.setMargin(zcLB, new Insets(3,0,0,0));
            hb2.setMargin(jsRB, new Insets(3,0,0,0));
            hb2.setMargin(fjsRB, new Insets(3,0,0,5));
            hb2.setMargin(jRB, new Insets(3,0,0,5));

            HBox hb3 = new HBox();
            hb3.getChildren().addAll(phoneLB, phoneTF);
            hb3.setMargin(phoneLB, new Insets(3,0,0,0));
            hb3.setMargin(phoneTF, new Insets(3,0,0,5));

            HBox hb5 = new HBox();
            hb5.getChildren().addAll(majorLB, majorTF);
            hb5.setMargin(majorLB, new Insets(3,0,0,0));
            hb5.setMargin(majorTF, new Insets(3,0,0,5));

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
            editStage.setScene(new Scene(bPane, 440, 220));
            editStage.setTitle("�޸Ľ�ʦ����");
            editStage.showAndWait();
        }
    }

    private class EditDialog
    {   Stage editStage;

        Label numLB = new Label("�̹��ţ�");
        TextField numTF = new TextField();

        Label nameLB = new Label("������");
        TextField nameTF = new TextField();

        Label zcLB = new Label("ְ�ƣ�");
        RadioButton jsRB = new RadioButton("����");
        RadioButton fjsRB = new RadioButton("������");
        RadioButton jRB = new RadioButton("��ʦ");
        ToggleGroup sexTG = new ToggleGroup();


        Label phoneLB = new Label("��ϵ�绰��");
        TextField phoneTF = new TextField();

        Label majorLB = new Label("�о�����");
        TextField majorTF = new TextField();


        Button EditButton;
        Teacher opp;

        public EditDialog(Teacher p)
        {   opp = p;
            numTF.setPrefWidth(80);
            nameTF.setPrefWidth(100);
            jsRB.setToggleGroup(sexTG);
            fjsRB.setToggleGroup(sexTG);
            jRB.setToggleGroup(sexTG);
            majorTF.setPrefWidth(240);
            phoneTF.setPrefWidth(120);

            EditButton = new Button("����");
            EditButton.setStyle("-fx-text-fill:red");
            numTF.setText(p.getUid());
            numTF.setDisable(true);
            nameTF.setText(p.getName());
            nameTF.setDisable(true);
            if(p.getZc().trim().equals("����"))
                jsRB.setSelected(true);
            if(p.getZc().trim().equals("������"))
                fjsRB.setSelected(true);
            if(p.getZc().trim().equals("��ʦ"))
                jRB.setSelected(true);
            majorTF.setText(p.getYjfx());
            phoneTF.setText(p.getPhone());
            EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {	opp.setUid(numTF.getText());
                    opp.setName(nameTF.getText());
                    if(jsRB.isSelected())
                        opp.setZc("����");
                    if(fjsRB.isSelected())
                        opp.setZc("������");
                    if(jRB.isSelected())
                        opp.setZc("��ʦ");
                    opp.setPhone((String)phoneTF.getText());
                    opp.setYjfx((String)majorTF.getText());
                    if (nullData(opp)) {
                        opp.setDTag(1);
                        teaTable.refresh();
                        editStage.close();
                    }
                }
            });

            EditButton.setPrefWidth(60);
            Button cancelButton = new Button("ȡ��");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {	editStage.close();
                }
            });

            HBox hb1 = new HBox();
            hb1.getChildren().addAll(numLB, numTF, nameLB, nameTF);
            hb1.setMargin(numLB, new Insets(3,0,0,0));
            hb1.setMargin(nameLB, new Insets(3,0,0,10));


            HBox hb2 = new HBox();
            hb2.getChildren().addAll(zcLB, jsRB, fjsRB, jRB);
            hb2.setMargin(zcLB, new Insets(3,0,0,0));
            hb2.setMargin(jsRB, new Insets(3,0,0,0));
            hb2.setMargin(fjsRB, new Insets(3,0,0,5));
            hb2.setMargin(jRB, new Insets(3,0,0,5));

            HBox hb3 = new HBox();
            hb3.getChildren().addAll(phoneLB, phoneTF);
            hb3.setMargin(phoneLB, new Insets(3,0,0,0));
            hb3.setMargin(phoneTF, new Insets(3,0,0,5));

            HBox hb5 = new HBox();
            hb5.getChildren().addAll(majorLB, majorTF);
            hb5.setMargin(majorLB, new Insets(3,0,0,0));
            hb5.setMargin(majorTF, new Insets(3,0,0,5));

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
            editStage.setScene(new Scene(bPane, 440, 220));
            editStage.setTitle("�޸Ľ�ʦ����");
            editStage.showAndWait();
        }
    }

    private boolean nullData(Teacher pp)
    {   String uNum = pp.getUid().trim();
        String uName = pp.getName().trim();
       
        if(uNum.length() == 0)
    	{   CommonDialog.WarningDialog("��ʦ�� "+uName+"�����Ϊ�գ�");
    	    return false;
    	}
    	if(uName.length() == 0)
    	{   CommonDialog.WarningDialog("��ʦ��"+uNum+"������Ϊ�գ�");
    	    return false;
    	}
    	if(pp.getZc().trim().length() == 0)
    	{   CommonDialog.WarningDialog("��ʦ��"+uNum+" - "+uName+"��ְ�Ʋ���ȷ��");
	        return false;
	    }
        if(pp.getPhone().trim().length() == 0)
        {   CommonDialog.WarningDialog("��ʦ��"+uNum+" - "+uName+"����ϵ�绰����Ϊ�գ�");
	        return false;
	    }
        if(pp.getYjfx().trim().length() == 0) {
            CommonDialog.WarningDialog("��ʦ��" + uNum + " - " + uName + "���о�����û��д��");
            return false;
        }
        return true;
    }
    
    private void deleteDataFromTeacher(String numstr)
    {   try
        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt = conn.createStatement();
            String deleteSql = "DELETE FROM Teacher WHERE uid='"+numstr+"'";
            int count = stmt.executeUpdate(deleteSql); 
            System.out.println("ɾ���� Teacher ��ġ�"+numstr+"��"+count+"����ʦ��¼");
            String sql="DELETE FROM notice WHERE id='"+numstr+"'";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public void updateTeacherDataBase()
    {   Teacher up;
        String unum, uname, uzc, uphone, uyjfx;
        int udtag;
        
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            for(int i=0;i<teaData.size();i++)
            {   up = (Teacher)teaData.get(i);
                udtag = up.getDTag();
                unum = up.getUid();
                uname = up.getName();
                uzc = up.getZc();
                uphone = up.getPhone();
                uyjfx = up.getYjfx();
                if(udtag == 2)  // �����Ӽ�¼
                {   String insertSql="INSERT INTO Teacher(uid,name,zc,phone,yjfx) "+
                                     "VALUES('"+unum+"','"+uname+"','"+uzc+"','"+uphone+"','"+uyjfx+"')";
                    int count=stmt.executeUpdate(insertSql);
                    System.out.println("��� "+ count+" ����¼�� Teacher ����");

                    String sql="INSERT INTO notice(id) VALUES('"+unum+"')";
                    stmt.executeUpdate(sql);
                }
                
                if(udtag == 1)  // ���Ĺ��ļ�¼
                {   String updateSql="UPDATE Teacher SET name='"+uname+"',zc='"+uzc+"',phone='"+uphone+"',yjfx='"+uyjfx+"' WHERE uid='"+unum+"'";
                    int count=stmt.executeUpdate(updateSql);
                    System.out.println("�޸� "+ count+" ����¼�� Teacher ����");
                }
            }                 
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    private void loadDataFromTeacher()
    {   String sql="SELECT uid,name,zc,phone,yjfx FROM teacher";
               
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            teaData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid"); if(no==null) no="";
                String name=rs.getString("name"); if(name==null) name="";
                String zc=rs.getString("zc"); if(zc==null) zc="";
                String phone=rs.getString("phone"); if(phone==null) phone="";
                String yjfx=rs.getString("yjfx"); if(yjfx==null) yjfx="";
                
                teaData.add(new Teacher(no, name, zc, phone, yjfx,0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public static class Teacher
    {   private final SimpleStringProperty uid;
    	private final SimpleStringProperty name;
        private final SimpleStringProperty zc;
        private final SimpleStringProperty phone;
        private final SimpleStringProperty yjfx;
        private int dtag; // 0 �������      1 �༭���Ĺ�������   2  �����ӵļ�¼

        private Teacher()
        {   this.uid = new SimpleStringProperty("");
            this.name = new SimpleStringProperty("");
            this.zc = new SimpleStringProperty("");
            this.phone = new SimpleStringProperty("");
            this.yjfx = new SimpleStringProperty("");
            dtag = 0;
        }

        private Teacher(String uUid, String uName, String uZc, String uPhone, String uYjfx, int udtag) 
        {   this.uid = new SimpleStringProperty(uUid);
            this.name = new SimpleStringProperty(uName);
            this.zc = new SimpleStringProperty(uZc);
            this.phone = new SimpleStringProperty(uPhone);
            this.yjfx = new SimpleStringProperty(uYjfx);
            dtag = udtag;
        }
        
        private Teacher(String uUid, String uName, String uZc, String uPhone, String uYjfx) 
        {   this(uUid, uName, uZc, uYjfx, uPhone, 0);
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

        public String getZc() 
        {   return zc.get();
        }

        public void setZc(String uZc) 
        {   zc.set(uZc);
        }
        
        public String getPhone() 
        {   return phone.get();
        }

        public void setPhone(String uPhone) 
        {   phone.set(uPhone);
        }
        
        public String getYjfx() 
        {   return yjfx.get();
        }

        public void setYjfx(String uYjfx) 
        {   yjfx.set(uYjfx);
        }
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
}
