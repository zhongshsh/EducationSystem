import java.sql.*;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public class CommonDialog 
{
    public static void NoticeDialog(String uID, String uName) throws SQLException {
        String DBUrl="jdbc:ucanaccess:///D:/Documents/Java/EducationSystem/out/production/EducationSystem/management.mdb";
        String DBUser="", DBPassword="";
        Connection conn=DriverManager.getConnection(DBUrl, DBUser, DBPassword);
        Statement stmt=conn.createStatement();
        String sql="SELECT * FROM notice where id='"+uID+"'";
        ResultSet rs=stmt.executeQuery(sql);
        String text="", notice="";
        while(rs.next()) {
            text = rs.getString("text");
            notice = rs.getString("notice");
        }
        if(notice != null && notice.equals("1") && text != null && text != ""){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("公告");
            alert.setContentText(uName+" 您好，请注意\n\n"+text);
            alert.initStyle(StageStyle.UTILITY);
            alert.show();
        }
        sql="update notice set notice='0' where id='"+uID+"'";
        stmt.executeUpdate(sql);

    }

    public static void WarningDialog(String text)
    {  	Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("警告");                  
        alert.setContentText(text);
        alert.initStyle(StageStyle.UTILITY);
        alert.show();
	}
	
	public static void InformationDialog(String text) 
    {  	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("提示");                  
        alert.setContentText(text);
        alert.initStyle(StageStyle.UTILITY);
        alert.show();
	}

    public static boolean ConfirmDialog(String title, String text)
    {   Alert alert = new Alert(Alert.AlertType.CONFIRMATION); 
        alert.setHeaderText(title);
        alert.setContentText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get().equals(ButtonType.OK))
        	return true;
        else
        	return false;
    }

}
