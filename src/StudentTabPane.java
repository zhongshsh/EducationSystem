import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

class StudentTabPane extends BorderPane 
{   private final TableView<Student> stuTable = new TableView<>();
    private final ObservableList<Student> stuData = FXCollections.observableArrayList();
    String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
    String DBUser="", DBPassword="";

    public StudentTabPane()
    {   loadDataFromStudent(); //  �����ݿ���ȡ student ���ݸ��� data �б�

        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellStringFactory = 
        		new Callback<TableColumn<Student, String>, TableCell<Student, String>>() 
                {   public TableCell<Student, String> call(TableColumn p) 
                    {   return new EditingStringCell<Student>();  
                    }  
                };  
                
        TableColumn<Student, String> numCol = new TableColumn<>("ѧ��");
        numCol.setMinWidth(60);
        numCol.setCellValueFactory(new PropertyValueFactory<Student, String>("uid"));
        numCol.setCellFactory(cellStringFactory);

        
        TableColumn<Student, String> nameCol = new TableColumn<>("����");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        nameCol.setCellFactory(cellStringFactory);

        
        TableColumn<Student, String> sexCol = new TableColumn<>("�Ա�");
        sexCol.setMinWidth(60);
        sexCol.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
        sexCol.setCellFactory(new Callback<TableColumn <Student, String>, TableCell <Student,String>>() 
        {   public TableCell <Student,String> call(TableColumn <Student, String> param)
            {   ObservableList<String> setList = FXCollections.observableArrayList("��", "Ů");
        	    ComboBoxTableCell<Student, String> cell = new ComboBoxTableCell<>(setList);
        	    return cell;
            }
        });

                               
        Callback<TableColumn<Student, Integer>, TableCell<Student, Integer>> cellIntegerFactory = 
           		new Callback<TableColumn<Student, Integer>, TableCell<Student, Integer>>() 
                {   public TableCell<Student, Integer> call(TableColumn p) 
                    {   return new EditingIntegerCell<Student>();  
                    }  
                };  
        TableColumn<Student, Integer> ageCol = new TableColumn<>("����");
        ageCol.setMinWidth(40);
        ageCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("age"));
        ageCol.setCellFactory(cellIntegerFactory);

               
        TableColumn<Student, String> depCol = new TableColumn<>("����Ժϵ");
        depCol.setMinWidth(90);
        depCol.setCellValueFactory(new PropertyValueFactory<Student, String>("department"));
        depCol.setCellFactory(new Callback<TableColumn <Student, String>, TableCell <Student,String>>() 
                {   public TableCell <Student,String> call(TableColumn <Student, String> param)
                    {   ObservableList<String> depList = FXCollections.observableArrayList(
                    		"��Ѷ����ѧԺ", "����ѧԺѧԺ", "�����ѧԺ", "�����ѧԺ", "�Զ���ѧԺ", 
                    		"������ϵѧԺ",	"��ѧϵ", "���ʽ���ѧԺ", "���պ���ѧԺ", "����ѧԺ");
                	    ComboBoxTableCell<Student, String> cell = new ComboBoxTableCell<>(depList);
                	    return cell;
                    }
                });

        TableColumn<Student, Integer> yearCol = new TableColumn<>("��ѧ���");
        yearCol.setMinWidth(40);
        yearCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("year"));
        yearCol.setCellFactory(cellIntegerFactory);

               
        TableColumn<Student, String> homeCol = new TableColumn<>("��ͥסַ");
        homeCol.setMinWidth(160);
        homeCol.setCellValueFactory(new PropertyValueFactory<Student, String>("home"));
        homeCol.setCellFactory(cellStringFactory);

        
        TableColumn<Student, String> phoneCol = new TableColumn<>("��ϵ�绰");
        phoneCol.setMinWidth(110);
        phoneCol.setCellValueFactory(new PropertyValueFactory<Student, String>("phone"));
        phoneCol.setCellFactory(cellStringFactory);

        
        stuTable.setItems(stuData);
        stuTable.getColumns().addAll(numCol, nameCol, sexCol, ageCol, depCol, yearCol, homeCol, phoneCol);
        stuTable.setEditable(false);
      
        final Button addButton = new Button("Add", new ImageView(new Image("image/add.png")));
        final Button editButton = new Button("Edit", new ImageView(new Image("image/edit.png")));
        final Button deleteButton = new Button("Delete",new ImageView(new Image("image/cancel.png")));
                
        deleteButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Student p = stuTable.getSelectionModel().getSelectedItem();
        		int pos = stuTable.getSelectionModel().getSelectedIndex();
        		
        		if(p != null)
        	    {   String delName = p.getName();
        			if(CommonDialog.ConfirmDialog("��ܰ��ʾ", "ȷ��Ҫɾ������Ϊ ��"+delName+"�� ��ѧ����"))
        			{   if(p.getDTag() != 2)  // ���������ӵļ�¼����Ҫɾ�����ݿ��еļ�¼
        			        deleteDataFromStudent(p.getUid());      			
        		        stuData.remove(pos);
        		    }
        		}
        		else
        		    CommonDialog.WarningDialog("��û��ѡ����Ҫɾ���ļ�¼"); 
        	}
            
        });
        
        addButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{
        	    Student t = new Student();
                AddDialog addeditD = new AddDialog(t);
                if (t.dtag == 2) {
                    stuData.add(t);
                    stuTable.refresh();
                    stuTable.getSelectionModel().selectLast();
                    stuTable.scrollTo(stuData.size());
   	                stuTable.requestFocus();
                    updateStudentDataBase();
                }
        	}
            
        });
        
        editButton.setOnAction(new EventHandler<ActionEvent>(){
        	public void handle(ActionEvent e)
        	{	Student p = stuTable.getSelectionModel().getSelectedItem();
        		if(p != null)
        		{	EditDialog addeditD = new EditDialog(p);
                    updateStudentDataBase();
        		}
    		    else
    		        CommonDialog.WarningDialog("��û��ѡ����Ҫ�޸ĵļ�¼"); 
        	}
        });

        final HBox hb = new HBox();
        hb.getChildren().addAll(addButton, editButton, deleteButton);
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setMargin(addButton, new Insets(0, 0, 0, 10));
                                
        ScrollPane spp = new ScrollPane(stuTable);
        spp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setHbarPolicy(ScrollBarPolicy.ALWAYS);
        spp.setFitToHeight(true);
        spp.setFitToWidth(true);
        setCenter(spp);
        setBottom(hb);
        setMargin(hb, new Insets(5, 10, 5, 10));
        
    }
    
    private boolean nullData(Student pp)
    {   String uNum = pp.getUid().trim();
        String uName = pp.getName().trim();
    	
    	if(uNum.length() == 0)
    	{   CommonDialog.WarningDialog("ѧ���� "+uName+"��ѧ��Ϊ�գ�");
    	    return false;
    	}
    	if(uName.length() == 0)
    	{   CommonDialog.WarningDialog("ѧ����"+uNum+"������Ϊ�գ�");
    	    return false;
    	}
    	if(pp.getSex().trim().length() == 0)
    	{   CommonDialog.WarningDialog("ѧ����"+uNum+" - "+uName+"���Ա���ȷ��");
	        return false;
	    }
        if(pp.getAge()<=10||pp.getAge()>=35)
        {   CommonDialog.WarningDialog("ѧ����"+uNum+" - "+uName+"�����䲻�Ϲ棡");
	        return false;
	    }
        if(pp.getDepartment().equals(""))
        {   CommonDialog.WarningDialog("ѧ����"+uNum+" - "+uName+"��ûѡ��Ժϵ��");
	        return false;
	    }
	    if(pp.getYear()<2000||pp.getAge()>2030)
        {   CommonDialog.WarningDialog("ѧ����"+uNum+" - "+uName+"����ѧ��ݲ���ȷ��");
	        return false;
	    }
        if(pp.getHome().equals("")){
            CommonDialog.WarningDialog("ѧ����"+uNum+" - "+uName+"����ͥסַ��û��д��");
            return false;
        }
        if(pp.getPhone().equals("")) {
            CommonDialog.WarningDialog("ѧ����" + uNum + " - " + uName + "����ϵ�绰��û��д��");
            return false;
        }
        return true;
    }

    private class AddDialog
    {   Stage editStage;

        Label numLB = new Label("ѧ�ţ�");
        TextField numTF = new TextField();

        Label nameLB = new Label("������");
        TextField nameTF = new TextField();

        Label sexLB = new Label("�Ա�");
        RadioButton maleRB = new RadioButton("��");
        RadioButton femaleRB = new RadioButton("Ů");
        ToggleGroup sexTG = new ToggleGroup();

        Label ageLB = new Label("���䣺");
        TextField ageTF = new TextField();

        Label depLB = new Label("Ժϵ��");
        ObservableList<String> depList = FXCollections.observableArrayList(
                "��Ѷ����ѧԺ", "����ѧԺѧԺ", "�����ѧԺ", "�����ѧԺ", "�Զ���ѧԺ",
                "������ϵѧԺ",	"��ѧϵ", "���ʽ���ѧԺ", "���պ���ѧԺ", "����ѧԺ");
        ComboBox<String> depCB = new ComboBox<String>(depList);

        Label yearLB = new Label("��ѧ��ݣ�");
        TextField yearTF = new TextField();

        Label homeLB = new Label("��ͥסַ��");
        TextField homeTF = new TextField();

        Label phoneLB = new Label("��ϵ�绰��");
        TextField phoneTF = new TextField();


        Button EditButton;
        Student opp;

        public AddDialog(Student p)
        {
            opp = p;
            numTF.setPrefWidth(80);
            nameTF.setPrefWidth(100);
            maleRB.setToggleGroup(sexTG);
            femaleRB.setToggleGroup(sexTG);
            ageTF.setPrefWidth(40);
            depCB.setPrefWidth(120);
            yearTF.setPrefWidth(50);
            homeTF.setPrefWidth(240);
            phoneTF.setPrefWidth(120);

            EditButton = new Button("���");
            EditButton.setStyle("-fx-text-fill:red");
            EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {
                    if (ageTF.getText().equals("") || yearTF.getText().equals("")){
                        CommonDialog.WarningDialog("��鿴�������ѧʱ���Ƿ�Ϊ�գ�");
                    }else {
                        opp.setUid(numTF.getText());
                        opp.setName(nameTF.getText());
                        if (maleRB.isSelected())
                            opp.setSex("��");
                        if (femaleRB.isSelected())
                            opp.setSex("Ů");
                        opp.setAge(Integer.parseInt(ageTF.getText()));
                        opp.setDepartment(depCB.getValue());
                        opp.setYear(Integer.parseInt(yearTF.getText()));
                        opp.setHome((String) homeTF.getText());
                        opp.setPhone((String) phoneTF.getText());
                        if (nullData(opp)) {
                            opp.dtag = 2;
                            stuTable.refresh();
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
            hb1.getChildren().addAll(numLB, numTF, nameLB, nameTF);
            hb1.setMargin(numLB, new Insets(3,0,0,0));
            hb1.setMargin(nameLB, new Insets(3,0,0,10));


            HBox hb2 = new HBox();
            hb2.getChildren().addAll(sexLB, maleRB, femaleRB, ageLB, ageTF, depLB, depCB, yearLB, yearTF);
            hb2.setMargin(sexLB, new Insets(3,0,0,0));
            hb2.setMargin(maleRB, new Insets(3,0,0,0));
            hb2.setMargin(femaleRB, new Insets(3,0,0,5));
            hb2.setMargin(ageLB, new Insets(3,0,0,15));
            hb2.setMargin(depLB, new Insets(3,0,0,15));
            hb2.setMargin(yearLB, new Insets(3,0,0,15));

            HBox hb3 = new HBox();
            hb3.getChildren().addAll(homeLB, homeTF, phoneLB, phoneTF);
            hb3.setMargin(homeLB, new Insets(3,0,0,0));
            hb3.setMargin(phoneLB, new Insets(3,0,0,15));

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

    private class EditDialog
    {   Stage editStage;
    
        Label numLB = new Label("ѧ�ţ�");
        TextField numTF = new TextField();
    
    	Label nameLB = new Label("������");
	    TextField nameTF = new TextField();
       
        Label sexLB = new Label("�Ա�");
        RadioButton maleRB = new RadioButton("��");
        RadioButton femaleRB = new RadioButton("Ů");
        ToggleGroup sexTG = new ToggleGroup();
        
        Label ageLB = new Label("���䣺");
	    TextField ageTF = new TextField();
        	    
        Label depLB = new Label("Ժϵ��");
	    ObservableList<String> depList = FXCollections.observableArrayList(
        		"��Ѷ����ѧԺ", "����ѧԺѧԺ", "�����ѧԺ", "�����ѧԺ", "�Զ���ѧԺ", 
        		"������ϵѧԺ",	"��ѧϵ", "���ʽ���ѧԺ", "���պ���ѧԺ", "����ѧԺ");
    	ComboBox<String> depCB = new ComboBox<String>(depList);
	    
	    Label yearLB = new Label("��ѧ��ݣ�");
	    TextField yearTF = new TextField();
	    		
	    Label homeLB = new Label("��ͥסַ��");
	    TextField homeTF = new TextField();
	    
	    Label phoneLB = new Label("��ϵ�绰��");
	    TextField phoneTF = new TextField();
	    
		    	
	    Button EditButton;
	    Student opp;

	    public EditDialog(Student p)
	    {
	        opp = p;
	        numTF.setPrefWidth(80);
	        nameTF.setPrefWidth(100);
            maleRB.setToggleGroup(sexTG);
            femaleRB.setToggleGroup(sexTG);
            ageTF.setPrefWidth(40);
            depCB.setPrefWidth(120);
    	   	yearTF.setPrefWidth(50);
    	   	homeTF.setPrefWidth(240);
    	   	phoneTF.setPrefWidth(120);
    	   	
    	    EditButton = new Button("����");
    	    EditButton.setStyle("-fx-text-fill:red");
    	    numTF.setText(p.getUid());
    	    numTF.setDisable(true);
    	    nameTF.setText(p.getName());
    	    nameTF.setDisable(true);
    	    if(p.getSex().trim().equals("��"))
    	        maleRB.setSelected(true);
    	    if(p.getSex().trim().equals("Ů"))
    	        femaleRB.setSelected(true);
    	    ageTF.setText(""+p.getAge());
    	    depCB.setValue(p.getDepartment());
    	    yearTF.setText(""+p.getYear());
    	    homeTF.setText(p.getHome());
    	    phoneTF.setText(p.getPhone());
    	    EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {	opp.setUid(numTF.getText());
                	opp.setName(nameTF.getText());
                    if(maleRB.isSelected())
                	    opp.setSex("��");
                    if(femaleRB.isSelected())
                	    opp.setSex("Ů");
        	   	    opp.setAge(Integer.parseInt(ageTF.getText()));
        	   	    opp.setDepartment(depCB.getValue());
        	   	    opp.setYear(Integer.parseInt(yearTF.getText()));
        	   	    opp.setHome((String)homeTF.getText());
        	   	    opp.setPhone((String)phoneTF.getText());
        	   	    opp.setDTag(1);
        	   	    if (nullData(opp)) {
                        stuTable.refresh();
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
            hb2.getChildren().addAll(sexLB, maleRB, femaleRB, ageLB, ageTF, depLB, depCB, yearLB, yearTF);
            hb2.setMargin(sexLB, new Insets(3,0,0,0));
            hb2.setMargin(maleRB, new Insets(3,0,0,0));
            hb2.setMargin(femaleRB, new Insets(3,0,0,5));
            hb2.setMargin(ageLB, new Insets(3,0,0,15));
            hb2.setMargin(depLB, new Insets(3,0,0,15));
            hb2.setMargin(yearLB, new Insets(3,0,0,15));
            
            HBox hb3 = new HBox();
            hb3.getChildren().addAll(homeLB, homeTF, phoneLB, phoneTF);
            hb3.setMargin(homeLB, new Insets(3,0,0,0));
            hb3.setMargin(phoneLB, new Insets(3,0,0,15));
                                
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
//            editStage.setX(750);
//            editStage.setY(500);
            editStage.initModality(Modality.APPLICATION_MODAL);

            BorderPane bPane = new BorderPane();
            bPane.setCenter(vbox);
            bPane.setBottom(hb4);
            bPane.setPadding(new Insets(0,0,15,0));
            editStage.setScene(new Scene(bPane, 540, 200));
            editStage.setTitle("�޸�ѧ������");
            editStage.showAndWait();
	    }
    }
        
    private void deleteDataFromStudent(String numstr)
    {   try
        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt = conn.createStatement();
            String deleteSql = "DELETE FROM Student WHERE uid='"+numstr+"'";
            int count = stmt.executeUpdate(deleteSql); 
            System.out.println("ɾ���� Student ��ġ�"+numstr+"��"+count+"����¼");
            String sql="DELETE FROM notice WHERE id='"+numstr+"'";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public void updateStudentDataBase()
    {   Student up;
        String unum, uname, usex, udep, uhome, uphone;
        int uage, uyear, udtag;
                
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
                        
            for(int i=0;i<stuData.size();i++)
            {   up = (Student)stuData.get(i);
                udtag = up.getDTag();
                unum = up.getUid();
                uname = up.getName();
                usex = up.getSex();
                uage = up.getAge();
                udep = up.getDepartment();
                uyear = up.getYear();
                uhome = up.getHome();
                uphone = up.getPhone();
                if(udtag == 2)  // �����Ӽ�¼
                {   String insertSql="INSERT INTO Student(uid,name,sex,age,department,phone,year,home) "+
                                     "VALUES('"+unum+"','"+uname+"','"+usex+"',"+uage+
                                     ",'"+udep+"','"+uphone+"',"+uyear+",'"+uhome+"')";
                    int count=stmt.executeUpdate(insertSql);
                    System.out.println("��� "+ count+" ����¼��Student����");

                    String sql="INSERT INTO notice(id) VALUES('"+unum+"')";
                    stmt.executeUpdate(sql);
                }
                
                if(udtag == 1)  // ���Ĺ��ļ�¼
                {   String updateSql="UPDATE Student SET sex='"+usex+"',age="+uage+",department='"+udep+"',phone='"+uphone+"',year="+uyear+",home='"+uhome+"' WHERE uid='"+unum+"'";
                    int count=stmt.executeUpdate(updateSql);
                    System.out.println("�޸� "+ count+" ����¼��Student����");
                }
            } 
                           
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
       
    private void loadDataFromStudent()
    {   String sql="SELECT uid,name,sex,age,department,year,home,phone FROM student";
               
        try
        {   Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt=conn.createStatement();
            
            stuData.clear();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {   String no=rs.getString("uid"); if(no==null) no="";
                String name=rs.getString("name"); if(name==null) name="";
                String sex=rs.getString("sex"); if(sex==null) sex="";
                int age=rs.getInt("age"); 
                String dep=rs.getString("department"); if(dep==null) dep="";
                int year=rs.getInt("year");
                String home=rs.getString("home"); if(home==null) home="";
                String phone=rs.getString("phone"); if(phone==null) phone="";
                stuData.add(new Student(no, name, sex, new Integer(age), dep, new Integer(year), home, phone,0));
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e) 
        {   e.printStackTrace();
        }
    }
    
    public static class Student
    {   private final SimpleStringProperty uid;
    	private final SimpleStringProperty name;
        private final SimpleStringProperty sex;
        private final SimpleIntegerProperty age;
        private final SimpleStringProperty department;
        private final SimpleIntegerProperty year;
        private final SimpleStringProperty home;
        private final SimpleStringProperty phone;
        private int dtag; // 0 �������      1 �༭���Ĺ�������   2  �����ӵļ�¼


        private Student()
        {
            this.uid = new SimpleStringProperty("");
            this.name = new SimpleStringProperty("");
            this.sex = new SimpleStringProperty("");
            this.age = new SimpleIntegerProperty(0);
            this.department = new SimpleStringProperty("");
            this.year = new SimpleIntegerProperty(0);
            this.home = new SimpleStringProperty("");
            this.phone = new SimpleStringProperty("");
            dtag = 0;
        }
        private Student(String uUid, String uName, String uSex, int uAge, String uDepartment, int uYear, String uHome, String uPhone, int udtag) 
        {   this.uid = new SimpleStringProperty(uUid);
            this.name = new SimpleStringProperty(uName);
            this.sex = new SimpleStringProperty(uSex);
            this.age = new SimpleIntegerProperty(uAge);
            this.department = new SimpleStringProperty(uDepartment);
            this.year = new SimpleIntegerProperty(uYear);
            this.home = new SimpleStringProperty(uHome);
            this.phone = new SimpleStringProperty(uPhone);
            dtag = udtag;
        }
        
        private Student(String uUid, String uName, String uSex, int uAge, String uDepartment, int uYear, String uHome, String uPhone)
        {   this(uUid, uName, uSex, uAge, uDepartment, uYear, uHome, uPhone, 0);
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

        public String getSex() 
        {   return sex.get();
        }

        public void setSex(String uSex) 
        {   sex.set(uSex);
        }

        public int getAge() 
        {   return age.get();
        }
        
        public void setAge(int uAge) 
        {   age.set(uAge);
        }

        public String getDepartment() 
        {   return department.get();
        }

        public void setDepartment(String uDepartment) 
        {   department.set(uDepartment);
        }
        
        public int getYear() 
        {   return year.get();
        }
        
        public void setYear(int uYear) 
        {   year.set(uYear);
        }
        
        public String getHome() 
        {   return home.get();
        }

        public void setHome(String uHome) 
        {   home.set(uHome);
        }
        
        public String getPhone() 
        {   return phone.get();
        }

        public void setPhone(String uPhone) 
        {   phone.set(uPhone);
        }
        
        public void setDTag(int udtag) 
        {   dtag = udtag;
        }
        
        public int getDTag() 
        {   return dtag;
        }
    }
}
