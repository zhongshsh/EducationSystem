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
    {   loadDataFromStudent(); //  从数据库提取 student 数据更新 data 列表

        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellStringFactory = 
        		new Callback<TableColumn<Student, String>, TableCell<Student, String>>() 
                {   public TableCell<Student, String> call(TableColumn p) 
                    {   return new EditingStringCell<Student>();  
                    }  
                };  
                
        TableColumn<Student, String> numCol = new TableColumn<>("学号");
        numCol.setMinWidth(60);
        numCol.setCellValueFactory(new PropertyValueFactory<Student, String>("uid"));
        numCol.setCellFactory(cellStringFactory);

        
        TableColumn<Student, String> nameCol = new TableColumn<>("姓名");
        nameCol.setMinWidth(80);
        nameCol.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        nameCol.setCellFactory(cellStringFactory);

        
        TableColumn<Student, String> sexCol = new TableColumn<>("性别");
        sexCol.setMinWidth(60);
        sexCol.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
        sexCol.setCellFactory(new Callback<TableColumn <Student, String>, TableCell <Student,String>>() 
        {   public TableCell <Student,String> call(TableColumn <Student, String> param)
            {   ObservableList<String> setList = FXCollections.observableArrayList("男", "女");
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
        TableColumn<Student, Integer> ageCol = new TableColumn<>("年龄");
        ageCol.setMinWidth(40);
        ageCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("age"));
        ageCol.setCellFactory(cellIntegerFactory);

               
        TableColumn<Student, String> depCol = new TableColumn<>("隶属院系");
        depCol.setMinWidth(90);
        depCol.setCellValueFactory(new PropertyValueFactory<Student, String>("department"));
        depCol.setCellFactory(new Callback<TableColumn <Student, String>, TableCell <Student,String>>() 
                {   public TableCell <Student,String> call(TableColumn <Student, String> param)
                    {   ObservableList<String> depList = FXCollections.observableArrayList(
                    		"资讯管理学院", "岭南学院学院", "外国语学院", "计算机学院", "自动化学院", 
                    		"公共关系学院",	"数学系", "国际金融学院", "航空航天学院", "旅游学院");
                	    ComboBoxTableCell<Student, String> cell = new ComboBoxTableCell<>(depList);
                	    return cell;
                    }
                });

        TableColumn<Student, Integer> yearCol = new TableColumn<>("入学年份");
        yearCol.setMinWidth(40);
        yearCol.setCellValueFactory(new PropertyValueFactory<Student, Integer>("year"));
        yearCol.setCellFactory(cellIntegerFactory);

               
        TableColumn<Student, String> homeCol = new TableColumn<>("家庭住址");
        homeCol.setMinWidth(160);
        homeCol.setCellValueFactory(new PropertyValueFactory<Student, String>("home"));
        homeCol.setCellFactory(cellStringFactory);

        
        TableColumn<Student, String> phoneCol = new TableColumn<>("联系电话");
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
        			if(CommonDialog.ConfirmDialog("温馨提示", "确认要删除名字为 《"+delName+"》 的学生吗？"))
        			{   if(p.getDTag() != 2)  // 不是新增加的记录，需要删除数据库中的记录
        			        deleteDataFromStudent(p.getUid());      			
        		        stuData.remove(pos);
        		    }
        		}
        		else
        		    CommonDialog.WarningDialog("你没有选中需要删除的记录"); 
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
    		        CommonDialog.WarningDialog("你没有选中需要修改的记录"); 
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
    	{   CommonDialog.WarningDialog("学生（ "+uName+"）学号为空！");
    	    return false;
    	}
    	if(uName.length() == 0)
    	{   CommonDialog.WarningDialog("学生（"+uNum+"）姓名为空！");
    	    return false;
    	}
    	if(pp.getSex().trim().length() == 0)
    	{   CommonDialog.WarningDialog("学生（"+uNum+" - "+uName+"）性别不正确！");
	        return false;
	    }
        if(pp.getAge()<=10||pp.getAge()>=35)
        {   CommonDialog.WarningDialog("学生（"+uNum+" - "+uName+"）年龄不合规！");
	        return false;
	    }
        if(pp.getDepartment().equals(""))
        {   CommonDialog.WarningDialog("学生（"+uNum+" - "+uName+"）没选择院系！");
	        return false;
	    }
	    if(pp.getYear()<2000||pp.getAge()>2030)
        {   CommonDialog.WarningDialog("学生（"+uNum+" - "+uName+"）入学年份不正确！");
	        return false;
	    }
        if(pp.getHome().equals("")){
            CommonDialog.WarningDialog("学生（"+uNum+" - "+uName+"）家庭住址还没填写！");
            return false;
        }
        if(pp.getPhone().equals("")) {
            CommonDialog.WarningDialog("学生（" + uNum + " - " + uName + "）联系电话还没填写！");
            return false;
        }
        return true;
    }

    private class AddDialog
    {   Stage editStage;

        Label numLB = new Label("学号：");
        TextField numTF = new TextField();

        Label nameLB = new Label("姓名：");
        TextField nameTF = new TextField();

        Label sexLB = new Label("性别：");
        RadioButton maleRB = new RadioButton("男");
        RadioButton femaleRB = new RadioButton("女");
        ToggleGroup sexTG = new ToggleGroup();

        Label ageLB = new Label("年龄：");
        TextField ageTF = new TextField();

        Label depLB = new Label("院系：");
        ObservableList<String> depList = FXCollections.observableArrayList(
                "资讯管理学院", "岭南学院学院", "外国语学院", "计算机学院", "自动化学院",
                "公共关系学院",	"数学系", "国际金融学院", "航空航天学院", "旅游学院");
        ComboBox<String> depCB = new ComboBox<String>(depList);

        Label yearLB = new Label("入学年份：");
        TextField yearTF = new TextField();

        Label homeLB = new Label("家庭住址：");
        TextField homeTF = new TextField();

        Label phoneLB = new Label("联系电话：");
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

            EditButton = new Button("添加");
            EditButton.setStyle("-fx-text-fill:red");
            EditButton.setOnAction(new EventHandler<ActionEvent>(){
                public void handle(ActionEvent e)
                {
                    if (ageTF.getText().equals("") || yearTF.getText().equals("")){
                        CommonDialog.WarningDialog("请查看年龄或入学时间是否为空！");
                    }else {
                        opp.setUid(numTF.getText());
                        opp.setName(nameTF.getText());
                        if (maleRB.isSelected())
                            opp.setSex("男");
                        if (femaleRB.isSelected())
                            opp.setSex("女");
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
            Button cancelButton = new Button("取消");
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
            editStage.setTitle("添加学生资料");
            editStage.showAndWait();
        }
    }

    private class EditDialog
    {   Stage editStage;
    
        Label numLB = new Label("学号：");
        TextField numTF = new TextField();
    
    	Label nameLB = new Label("姓名：");
	    TextField nameTF = new TextField();
       
        Label sexLB = new Label("性别：");
        RadioButton maleRB = new RadioButton("男");
        RadioButton femaleRB = new RadioButton("女");
        ToggleGroup sexTG = new ToggleGroup();
        
        Label ageLB = new Label("年龄：");
	    TextField ageTF = new TextField();
        	    
        Label depLB = new Label("院系：");
	    ObservableList<String> depList = FXCollections.observableArrayList(
        		"资讯管理学院", "岭南学院学院", "外国语学院", "计算机学院", "自动化学院", 
        		"公共关系学院",	"数学系", "国际金融学院", "航空航天学院", "旅游学院");
    	ComboBox<String> depCB = new ComboBox<String>(depList);
	    
	    Label yearLB = new Label("入学年份：");
	    TextField yearTF = new TextField();
	    		
	    Label homeLB = new Label("家庭住址：");
	    TextField homeTF = new TextField();
	    
	    Label phoneLB = new Label("联系电话：");
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
    	   	
    	    EditButton = new Button("更改");
    	    EditButton.setStyle("-fx-text-fill:red");
    	    numTF.setText(p.getUid());
    	    numTF.setDisable(true);
    	    nameTF.setText(p.getName());
    	    nameTF.setDisable(true);
    	    if(p.getSex().trim().equals("男"))
    	        maleRB.setSelected(true);
    	    if(p.getSex().trim().equals("女"))
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
                	    opp.setSex("男");
                    if(femaleRB.isSelected())
                	    opp.setSex("女");
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
    	    Button cancelButton = new Button("取消");
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
            editStage.setTitle("修改学生资料");
            editStage.showAndWait();
	    }
    }
        
    private void deleteDataFromStudent(String numstr)
    {   try
        {   Connection conn = DriverManager.getConnection(DBUrl, DBUser, DBPassword);
            Statement stmt = conn.createStatement();
            String deleteSql = "DELETE FROM Student WHERE uid='"+numstr+"'";
            int count = stmt.executeUpdate(deleteSql); 
            System.out.println("删除了 Student 表的《"+numstr+"》"+count+"条记录");
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
                if(udtag == 2)  // 新增加记录
                {   String insertSql="INSERT INTO Student(uid,name,sex,age,department,phone,year,home) "+
                                     "VALUES('"+unum+"','"+uname+"','"+usex+"',"+uage+
                                     ",'"+udep+"','"+uphone+"',"+uyear+",'"+uhome+"')";
                    int count=stmt.executeUpdate(insertSql);
                    System.out.println("添加 "+ count+" 条记录到Student表中");

                    String sql="INSERT INTO notice(id) VALUES('"+unum+"')";
                    stmt.executeUpdate(sql);
                }
                
                if(udtag == 1)  // 更改过的记录
                {   String updateSql="UPDATE Student SET sex='"+usex+"',age="+uage+",department='"+udep+"',phone='"+uphone+"',year="+uyear+",home='"+uhome+"' WHERE uid='"+unum+"'";
                    int count=stmt.executeUpdate(updateSql);
                    System.out.println("修改 "+ count+" 条记录到Student表中");
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
        private int dtag; // 0 无需更改      1 编辑更改过的数据   2  新增加的记录


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
