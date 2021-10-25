import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.sql.*;

class LessonTabPane extends BorderPane 
{
    private final TableView<Lesson> LessTable = new TableView<>();

    public final static ObservableList<Lesson> LessData = FXCollections.observableArrayList();
    private ObservableList<String> teacherList = FXCollections.observableArrayList();
    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";

    public static void refreashTime() throws SQLException {
        loadDataFromLesson();
        ReadLesson r = new ReadLesson(LessData);

    }
    public LessonTabPane(TeacherTabPane ttp) throws SQLException {
        loadDataFromLesson(); //  �����ݿ���ȡ Lesson ���ݸ��� data �б�
        
        for(int i=0;i<ttp.teaData.size(); i++)  //  �ӽ�ʦ TeacherPane ����ȡ��ʦ�����б�
            teacherList.add(((TeacherTabPane.Teacher)ttp.teaData.get(i)).getName());
        
        Callback<TableColumn<Lesson, String>, TableCell<Lesson, String>> cellStringFactory = 
        		new Callback<TableColumn<Lesson, String>, TableCell<Lesson, String>>() 
                {   public TableCell<Lesson, String> call(TableColumn p) 
                    {   return new EditingStringCell<Lesson>();  
                    }  
                };  
                
        TableColumn<Lesson, String> numCol = new TableColumn<>("�γ̺�");
        numCol.setMinWidth(80);
        numCol.setCellValueFactory(new PropertyValueFactory<Lesson, String>("uid"));
        numCol.setCellFactory(cellStringFactory);

        
        TableColumn<Lesson, String> nameCol = new TableColumn<>("�γ�����");
        nameCol.setMinWidth(240);
        nameCol.setCellValueFactory(new PropertyValueFactory<Lesson, String>("name"));
        nameCol.setCellFactory(cellStringFactory);

        
               
        TableColumn<Lesson, String> teaCol = new TableColumn<>("�ο���ʦ");
        teaCol.setMinWidth(80);
        teaCol.setCellValueFactory(new PropertyValueFactory<Lesson, String>("teacher"));
        teaCol.setCellFactory(new Callback<TableColumn <Lesson, String>, TableCell <Lesson,String>>() 
        {   public TableCell <Lesson,String> call(TableColumn <Lesson, String> param)
            {   ComboBoxTableCell<Lesson, String> cell = new ComboBoxTableCell<>(teacherList);
        	    return cell;
            }
        });

        
        Callback<TableColumn<Lesson, Integer>, TableCell<Lesson, Integer>> cellIntegerFactory = 
           		new Callback<TableColumn<Lesson, Integer>, TableCell<Lesson, Integer>>() 
                {   public TableCell<Lesson, Integer> call(TableColumn p) 
                    {   return new EditingIntegerCell<Lesson>();  
                    }  
                };  
        TableColumn<Lesson, Integer> chourCol = new TableColumn<>("ѧ��");
        chourCol.setMinWidth(40);
        chourCol.setCellValueFactory(new PropertyValueFactory<Lesson, Integer>("chour"));
        chourCol.setCellFactory(cellIntegerFactory);

        
        TableColumn<Lesson, String> typeCol = new TableColumn<>("�γ�����");
        typeCol.setMinWidth(100);
        typeCol.setCellValueFactory(new PropertyValueFactory<Lesson, String>("type"));
        typeCol.setCellFactory(new Callback<TableColumn <Lesson, String>, TableCell <Lesson,String>>() 
        {   public TableCell <Lesson,String> call(TableColumn <Lesson, String> param)
            {   ObservableList<String> typeList = FXCollections.observableArrayList("רҵ����", "רҵѡ��", "��ѡ��");
                ComboBoxTableCell<Lesson, String> cell = new ComboBoxTableCell<>(typeList);
        	    return cell;
            }
        });

        
        TableColumn<Lesson, Boolean> engCol = new TableColumn<>("˫���ѧ");
        engCol.setMinWidth(60);
        engCol.setCellValueFactory(new PropertyValueFactory<Lesson, Boolean>("eng"));
        engCol.setCellFactory(CheckBoxTableCell.forTableColumn(engCol));

        
        TableColumn<Lesson, String> ksfsCol = new TableColumn<>("���Է�ʽ");
        ksfsCol.setMinWidth(100);
        ksfsCol.setCellValueFactory(new PropertyValueFactory<Lesson, String>("ksfs"));
        ksfsCol.setCellFactory(new Callback<TableColumn <Lesson, String>, TableCell <Lesson,String>>() 
        {   public TableCell <Lesson,String> call(TableColumn <Lesson, String> param)
            {   ObservableList<String> typeList = FXCollections.observableArrayList("�վ�", "����", "��Ŀ����");
                ComboBoxTableCell<Lesson, String> cell = new ComboBoxTableCell<>(typeList);
        	    return cell;
            }
        });

        
        LessTable.setItems(LessData);
        LessTable.getColumns().addAll(numCol, nameCol, teaCol, chourCol, typeCol, engCol, ksfsCol);
        LessTable.setEditable(false);
      
        final Button addButton = new Button("Add", new ImageView(new Image("image/add.png")));
        final Button editButton = new Button("Edit",new ImageView(new Image("image/edit.png")));
        final Button deleteButton = new Button("Delete",new ImageView(new Image("image/cancel.png")));

        Button updateButton = new Button("���¿γ�ʱ���", new ImageView(new Image("image/quit.png")));
        updateButton.setOnAction(e->{
            try {
                // ���¿γ�ʱ���
                LessonTabPane.refreashTime();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        editButton.setOnAction(new EventHandler<ActionEvent>(){
            public void handle(ActionEvent e)
            {	Lesson p = LessTable.getSelectionModel().getSelectedItem();
                if(p != null)
                {	EditDialog addeditD = new EditDialog(p);
                    updateLessonDataBase();
                }
                else
                    CommonDialog.WarningDialog("��û��ѡ����Ҫ�޸ĵļ�¼");
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Lesson p = LessTable.getSelectionModel().getSelectedItem();
        		int pos = LessTable.getSelectionModel().getSelectedIndex();
        		
        		if(p != null)
        	    {   String delName = p.getName();
        			if(CommonDialog.ConfirmDialog("��ܰ��ʾ", "ȷ��Ҫɾ���γ���Ϊ ��"+delName+"�� ��������"))
        			{   if(p.getDTag() != 2)  // ���������ӵļ�¼����Ҫɾ�����ݿ��еļ�¼
        			        deleteDataFromLesson(p.getUid(), p.getTeacher());
        		        LessData.remove(pos);
        		    }
        		}
        		else
        		    CommonDialog.WarningDialog("��û��ѡ����Ҫɾ���ļ�¼"); 
        	}
            
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{
                Lesson l = new Lesson();
                AddDialog ad = new AddDialog(l);
                if (l.dtag == 2) {
                    LessData.add(l);
                    LessTable.refresh();
                    LessTable.getSelectionModel().selectLast();
                    LessTable.scrollTo(LessData.size());
                    LessTable.requestFocus();
                    updateLessonDataBase();
                }
        	}
            
        });
                
        final HBox hb = new HBox();    
        hb.getChildren().addAll(addButton, editButton, deleteButton, updateButton);
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setMargin(addButton, new Insets(0, 0, 0, 10));
                                
        ScrollPane spp = new ScrollPane(LessTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setFitToHeight(true);
        spp.setFitToWidth(true);
        setCenter(spp);
        setBottom(hb);
        setMargin(hb, new Insets(5, 10, 5, 10));
    }

    private class EditDialog
    {   Stage editStage;

        Label numLB = new Label("�γ̺ţ�");
        TextField numTF = new TextField();

        Label nameLB = new Label("�γ����ƣ�");
        TextField nameTF = new TextField();

        Label rklsLB = new Label("�ο���ʦ��");
        TextField rklsTF = new TextField();

        Label chourLB = new Label("ѧ�֣�");
        TextField chourTF = new TextField();

        Label kcxzLB = new Label("�γ����ʣ�");
        ObservableList<String> kcxzList = FXCollections.observableArrayList(
                "רҵ����", "רҵѡ��", "��������", "����ѡ��");
        ComboBox<String> kcxzCB = new ComboBox<String>(kcxzList);

        Label jxLB = new Label("˫���ѧ��");
        RadioButton yesRB = new RadioButton("��");
        RadioButton noRB = new RadioButton("��");
        ToggleGroup jxTG = new ToggleGroup();

        Label testLB = new Label("���Է�ʽ��");
        ObservableList<String> testList = FXCollections.observableArrayList(
                "�վ�", "��Ŀ����");
        ComboBox<String> testCB = new ComboBox<String>(testList);

        Button EditButton;
        Lesson opp;

        public EditDialog(Lesson p)
        {
            opp = p;
            numTF.setPrefWidth(80);
            numTF.setDisable(true);
            nameTF.setPrefWidth(150);
            nameTF.setDisable(true);
            chourTF.setPrefWidth(40);
            kcxzCB.setPrefWidth(120);
            testCB.setPrefWidth(100);
            yesRB.setToggleGroup(jxTG);
            noRB.setToggleGroup(jxTG);
            rklsTF.setPrefWidth(120);

            numTF.setText(opp.getUid());
            nameTF.setText(opp.getName());
            chourTF.setText(opp.getChour()+"");
            kcxzCB.setValue(opp.getType());
            testCB.setValue(opp.getKsfs());
            rklsTF.setText(opp.getTeacher());
            if(p.getEng()) yesRB.setSelected(true);
            else noRB.setSelected(true);

            EditButton = new Button("����");
            EditButton.setStyle("-fx-text-fill:red");
            EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e) {
                    if (chourTF.getText().equals("")){
                        CommonDialog.WarningDialog("ѧ�ֲ���Ϊ�գ�");
                    }else {
                        opp.setUid(numTF.getText());
                        opp.setName(nameTF.getText());
                        opp.setChour(Integer.parseInt(chourTF.getText()));
                        opp.setType(kcxzCB.getValue());
                        opp.setEng(yesRB.isSelected());
                        opp.setKsfs(testCB.getValue());
                        opp.setTeacher(rklsTF.getText());
                        if (nullData(opp)) {
                            opp.dtag = 1;
                            LessTable.refresh();
                            editStage.close();
                        }
                    }
                }
            });

            EditButton.setPrefWidth(60);
            Button cancelButton = new Button("ȡ��");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {
                    editStage.close();
                }
            });

            HBox hb1 = new HBox();
            hb1.getChildren().addAll(numLB, numTF, nameLB, nameTF, chourLB, chourTF);
            hb1.setMargin(numLB, new Insets(3,0,0,0));
            hb1.setMargin(nameLB, new Insets(3,0,0,10));
            hb1.setMargin(chourLB, new Insets(3,0,0,10));



            HBox hb2 = new HBox();
            hb2.getChildren().addAll( kcxzLB, kcxzCB, testLB, testCB);
            hb2.setMargin(kcxzLB, new Insets(3,0,0,0));
            hb2.setMargin(testLB, new Insets(3,0,0,15));

            HBox hb3 = new HBox();
            hb3.getChildren().addAll(rklsLB, rklsTF, jxLB, yesRB, noRB);
            hb3.setMargin(rklsLB, new Insets(3,0,0,0));
            hb3.setMargin(jxLB, new Insets(3,0,0,15));
            hb3.setMargin(noRB, new Insets(0,0,0,15));

            HBox hb4 = new HBox();
            hb4.setSpacing(20);
            hb4.setAlignment(Pos.CENTER);
            hb4.getChildren().addAll(EditButton, cancelButton);

            VBox vbox = new VBox();
            vbox.setSpacing(15);
            vbox.setPadding(new Insets(30, 10, 10, 30));
            vbox.getChildren().addAll(hb1, hb2, hb3);
            vbox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 1;" + "-fx-border-insets: 10;"
                    + "-fx-border-radius: 10;" + "-fx-border-color: gray lightgray lightgray gray;");

            editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL);

            BorderPane bPane = new BorderPane();
            bPane.setCenter(vbox);
            bPane.setBottom(hb4);
            bPane.setPadding(new Insets(0,0,15,0));
            editStage.setScene(new Scene(bPane, 540, 200));
            editStage.setTitle("���ѧ������");
            editStage.showAndWait();
        }
    }

    private class AddDialog
    {   Stage editStage;

        Label numLB = new Label("�γ̺ţ�");
        TextField numTF = new TextField();

        Label nameLB = new Label("�γ����ƣ�");
        TextField nameTF = new TextField();

        Label rklsLB = new Label("�ο���ʦ��");
        TextField rklsTF = new TextField();

        Label chourLB = new Label("ѧ�֣�");
        TextField chourTF = new TextField();

        Label kcxzLB = new Label("�γ����ʣ�");
        ObservableList<String> kcxzList = FXCollections.observableArrayList(
                "רҵ����", "רҵѡ��", "��������", "����ѡ��");
        ComboBox<String> kcxzCB = new ComboBox<String>(kcxzList);

        Label jxLB = new Label("˫���ѧ��");
        RadioButton yesRB = new RadioButton("��");
        RadioButton noRB = new RadioButton("��");
        ToggleGroup jxTG = new ToggleGroup();

        Label testLB = new Label("���Է�ʽ��");
        ObservableList<String> testList = FXCollections.observableArrayList(
                "�վ�", "��Ŀ����");
        ComboBox<String> testCB = new ComboBox<String>(testList);

        Button EditButton;
        Lesson opp;

        public AddDialog(Lesson p)
        {
            opp = p;
            numTF.setPrefWidth(80);
            nameTF.setPrefWidth(150);
            chourTF.setPrefWidth(40);
            kcxzCB.setPrefWidth(120);
            testCB.setPrefWidth(100);
            yesRB.setToggleGroup(jxTG);
            noRB.setToggleGroup(jxTG);
            rklsTF.setPrefWidth(120);
            
            EditButton = new Button("���");
            EditButton.setStyle("-fx-text-fill:red");
            EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e) {
                    if (chourTF.getText().equals("")){
                        CommonDialog.WarningDialog("ѧ�ֲ���Ϊ�գ�");
                    }else {
                        opp.setUid(numTF.getText());
                        opp.setName(nameTF.getText());
                        opp.setChour(Integer.parseInt(chourTF.getText()));
                        opp.setType(kcxzCB.getValue());
                        opp.setEng(yesRB.isSelected());
                        opp.setKsfs(testCB.getValue());
                        opp.setTeacher(rklsTF.getText());
                        if (nullData(opp)) {
                            opp.dtag = 2;
                            LessTable.refresh();
                            editStage.close();
                        }
                    }
                }
            });

            EditButton.setPrefWidth(60);
            Button cancelButton = new Button("ȡ��");
            cancelButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {
                    editStage.close();
                }
            });

            HBox hb1 = new HBox();
            hb1.getChildren().addAll(numLB, numTF, nameLB, nameTF, chourLB, chourTF);
            hb1.setMargin(numLB, new Insets(3,0,0,0));
            hb1.setMargin(nameLB, new Insets(3,0,0,10));
            hb1.setMargin(chourLB, new Insets(3,0,0,10));



            HBox hb2 = new HBox();
            hb2.getChildren().addAll( kcxzLB, kcxzCB, testLB, testCB);
            hb2.setMargin(kcxzLB, new Insets(3,0,0,0));
            hb2.setMargin(testLB, new Insets(3,0,0,15));

            HBox hb3 = new HBox();
            hb3.getChildren().addAll(rklsLB, rklsTF, jxLB, yesRB, noRB);
            hb3.setMargin(rklsLB, new Insets(3,0,0,0));
            hb3.setMargin(jxLB, new Insets(3,0,0,15));
            hb3.setMargin(noRB, new Insets(0,0,0,15));

            HBox hb4 = new HBox();
            hb4.setSpacing(20);
            hb4.setAlignment(Pos.CENTER);
            hb4.getChildren().addAll(EditButton, cancelButton);

            VBox vbox = new VBox();
            vbox.setSpacing(15);
            vbox.setPadding(new Insets(30, 10, 10, 30));
            vbox.getChildren().addAll(hb1, hb2, hb3);
            vbox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 1;" + "-fx-border-insets: 10;"
                    + "-fx-border-radius: 10;" + "-fx-border-color: gray lightgray lightgray gray;");

            editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL);

            BorderPane bPane = new BorderPane();
            bPane.setCenter(vbox);
            bPane.setBottom(hb4);
            bPane.setPadding(new Insets(0,0,15,0));
            editStage.setScene(new Scene(bPane, 540, 200));
            editStage.setTitle("���ѧ������");
            editStage.showAndWait();
        }
    }


    private boolean nullData(Lesson pp)
    {   String uNum = pp.getUid().trim();
        String uName = pp.getName().trim();
       
        if(uNum.length() == 0)
    	{   CommonDialog.WarningDialog("�γ̣� "+uName+"�����Ϊ�գ�");
    	    return false;
    	}

    	if(uName.length() == 0)
    	{   CommonDialog.WarningDialog("�γ̣�"+uNum+"������Ϊ�գ�");
    	    return false;
    	}
    	if(pp.getTeacher().trim().length() == 0)
    	{   CommonDialog.WarningDialog("�γ̣�"+uNum+" - "+uName+"��û��д�ο���ʦ��");
	        return false;
	    }
	    if(pp.getChour() == 0) {
            CommonDialog.WarningDialog("�γ̣�" + uNum + " - " + uName + "��û��дѧ�֣�");
            return false;
        }else if (pp.getChour() > 6 || pp.getChour() < 0) {
            CommonDialog.WarningDialog("�γ̣�" + uNum + " - " + uName + "����ѧ������дΪ1~6 ��");
            return false;
        }
        if(pp.getType().trim().length() == 0)
        {   CommonDialog.WarningDialog("�γ̣�"+uNum+" - "+uName+"��ûѡ��γ����ʣ�");
	        return false;
	    }
        if(pp.getKsfs().trim().length() == 0)
        {
            CommonDialog.WarningDialog("�γ̣�"+uNum+" - "+uName+"��ûȷ�����Է�ʽ��");
            return false;
        }
        return true;
    }
    
    private void deleteDataFromLesson(String numstr, String uteacher)
    {   try
        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt = conn.createStatement();
            String deleteSql = "DELETE FROM Lesson WHERE uid='"+numstr+"'";
            int count = stmt.executeUpdate(deleteSql); 
            System.out.println("ɾ���� Lesson ��ġ�"+numstr+"��"+count+"���γ̼�¼");
            String sql="update notice set notice='1', text='�γ���Ϣ�Ѹ��£��뼰ʱ�鿴��' where id in (select uid from student )" +
                    " or id in (select uid from teacher where name='"+uteacher+"')";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public void updateLessonDataBase()
    {   Lesson up;
        String unum, uname, uteacher, utype, uksfs;
        int uchour, udtag;
        boolean ueng;
        
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            for(int i=0;i<LessData.size();i++)
            {   up = (Lesson)LessData.get(i);
                udtag = up.getDTag();
                unum = up.getUid();
                uname = up.getName();
                uteacher = up.getTeacher();
                uchour = up.getChour();
                utype = up.getType();
                ueng = up.getEng();
                uksfs = up.getKsfs();
                if(udtag == 2)  // �����Ӽ�¼
                {   String insertSql="INSERT INTO Lesson(uid,name,teacher,chour,type,eng,ksfs) "+
                                     "VALUES('"+unum+"','"+uname+"','"+uteacher+"',"+uchour+",'"+utype+"',"+ueng+",'"+uksfs+"')";
                    int count=stmt.executeUpdate(insertSql);
                    System.out.println(insertSql+"    ��� "+ count+" ����¼�� Lesson ����");

                    String sql="update notice set notice='1', text='�γ���Ϣ�Ѹ��£��뼰ʱ�鿴��' where id in (select uid from student )" +
                            " or id in (select uid from teacher where name='"+uteacher+"')";
                    stmt.executeUpdate(sql);
                }
                
                if(udtag == 1)  // ���Ĺ��ļ�¼
                {   String updateSql="UPDATE Lesson SET name='"+uname+"',teacher='"+uteacher+"',chour="+uchour+
                                     ", type='"+utype+"',eng="+ueng+",ksfs='"+uksfs+"' WHERE uid='"+unum+"'";
                    int count=stmt.executeUpdate(updateSql);
                    System.out.println(updateSql+"    �޸� "+ count+" ����¼�� Lesson ����");
                    String sql="update notice set notice='1', text='�γ���Ϣ�Ѹ��£��뼰ʱ�鿴��' where id in (select uid from student )" +
                            " or id in (select uid from teacher where name='"+uteacher+"')";
                    stmt.executeUpdate(sql);
                }
            }                 
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    private static void loadDataFromLesson()
    {   String sql="SELECT uid,name,teacher,chour,type,eng,ksfs FROM lesson";
        String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
        String DBUser="", DBPassword="";
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();

            LessData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid"); if(no==null) no="";
                String name=rs.getString("name"); if(name==null) name="";
                String teacher=rs.getString("teacher"); if(teacher==null) teacher="";
                int chour=rs.getInt("chour");
                String type=rs.getString("type"); if(type==null) type="";
                boolean eng=rs.getBoolean("eng");
                String ksfs=rs.getString("ksfs"); if(ksfs==null) ksfs="";

                LessData.add(new Lesson(no, name, teacher, new Integer(chour), type, new Boolean(eng), ksfs,0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {   e.printStackTrace();
        }
    }





    public static class Lesson
    {   private final SimpleStringProperty uid;
    	private final SimpleStringProperty name;
        private final SimpleStringProperty teacher;
        private final SimpleIntegerProperty chour;
        private final SimpleStringProperty type;
        private final SimpleBooleanProperty eng;
        private final SimpleStringProperty ksfs;
        private int dtag; // 0 �������      1 �༭���Ĺ�������   2  �����ӵļ�¼

        Lesson()
        {   this.uid = new SimpleStringProperty("");
            this.name = new SimpleStringProperty("");
            this.teacher = new SimpleStringProperty("");
            this.chour = new SimpleIntegerProperty(0);
            this.type = new SimpleStringProperty("");
            this.eng = new SimpleBooleanProperty(false);
            this.ksfs = new SimpleStringProperty("");
            dtag = 0;
        }

        Lesson(String uUid, String uName, String uTeacher, int uChour, String uType, Boolean uEng, String uKsfs, int udtag)
        {   this.uid = new SimpleStringProperty(uUid);
            this.name = new SimpleStringProperty(uName);
            this.teacher = new SimpleStringProperty(uTeacher);
            this.chour = new SimpleIntegerProperty(uChour);
            this.type = new SimpleStringProperty(uType);
            this.eng = new SimpleBooleanProperty(uEng);
            this.ksfs = new SimpleStringProperty(uKsfs);
            dtag = udtag;
        }

        private Lesson(String uUid, String uName, String uTeacher, int uChour, String uType, Boolean uEng, String uKsfs)
        {   this(uUid, uName, uTeacher, uChour, uType, uEng, uKsfs, 0);
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

        public String getTeacher() 
        {   return teacher.get();
        }

        public void setTeacher(String uTeacher) 
        {   teacher.set(uTeacher);
        }
        
        public int getChour()
        {   return chour.get();
        }

        public void setChour(int uChour) 
        {   chour.set(uChour);
        }
        
        public String getType() 
        {   return type.get();
        }

        public void setType(String uType) 
        {   type.set(uType);
        }
        
        public SimpleBooleanProperty engProperty() // �� CheckBox ����
        {   return eng;    
        }  
        
        public boolean getEng() 
        {   return eng.get();
        }

        public void setEng(boolean uEng) 
        {   eng.set(uEng);
        }
                
        public String getKsfs() 
        {   return ksfs.get();
        }

        public void setKsfs(String uKsfs) 
        {   ksfs.set(uKsfs);
        }
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
}
