import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;

class EditingStringCell<T> extends TableCell<T, String> 
{   private TextField textField;  

    public EditingStringCell() {  }  
    @Override  
    public void startEdit() 
    {   if (!isEmpty()) 
        {   super.startEdit();  
            createTextField();  
            setText(null);  
            setGraphic(textField);  
            textField.selectAll();  
        }  
    }  
    @Override  
    public void cancelEdit() 
    {   super.cancelEdit();  
        setText((String) getItem());  
        setGraphic(null);  
    }  
    @Override  
    public void updateItem(String item, boolean empty) 
    {   super.updateItem(item, empty);  
        if (empty) 
        {   setText(null);  
            setGraphic(null);  
        } 
        else 
        {   if (isEditing()) 
            {   if (textField != null) 
                {   textField.setText(getString());  
                }  
                setText(null);  
                setGraphic(textField);  
            } 
            else 
            {   setText(getString());  
                setGraphic(null);  
            }  
        }  
    }  

    private void createTextField() 
    {   textField = new TextField(getString());  
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);  
        textField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {   @Override  
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) 
            {   if (!arg2) 
                {   commitEdit(textField.getText());  
                }  
            }  
        });  
    }  
    
    private String getString() 
    {   return getItem() == null ? "" : getItem().toString();  
    }  
}

class EditingIntegerCell<T> extends TableCell<T, Integer> 
{   private TextField textField;  

    public EditingIntegerCell() {  }  
    @Override  
    public void startEdit() 
    {   if (!isEmpty()) 
        {   super.startEdit();  
            createTextField();  
            setText(null);  
            setGraphic(textField);  
            textField.selectAll();  
        }  
    }  
    @Override  
    public void cancelEdit() 
    {   super.cancelEdit();  
        setText((String) (""+getItem()));  
        setGraphic(null);  
    }  
    @Override  
    public void updateItem(Integer item, boolean empty) 
    {   super.updateItem(item, empty);  
        if (empty) 
        {   setText(null);  
            setGraphic(null);  
        } 
        else 
        {   if (isEditing()) 
            {   if (textField != null) 
                {   textField.setText(getString());  
                }  
                setText(null);  
                setGraphic(textField);  
            } 
            else 
            {   setText(getString());  
                setGraphic(null);  
            }  
        }  
    }  

    private void createTextField() 
    {   textField = new TextField(getString());  
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);  
        textField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {   @Override  
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) 
            {   if (!arg2) 
                {   commitEdit(Integer.parseInt(textField.getText()));  
                }  
            }  
        });  
    }  
    
    private String getString() 
    {   return getItem() == null ? "" : getItem().toString();  
    }  
}

class EditingDoubleCell<T> extends TableCell<T, Double> 
{   private TextField textField;  

    public EditingDoubleCell() {  }  
    @Override  
    public void startEdit() 
    {   if (!isEmpty()) 
        {   super.startEdit();  
            createTextField();  
            setText(null);  
            setGraphic(textField);  
            textField.selectAll();  
        }  
    }  
    @Override  
    public void cancelEdit() 
    {   super.cancelEdit();  
        setText((String) (""+getItem()));  
        setGraphic(null);  
    }  
    @Override  
    public void updateItem(Double item, boolean empty) 
    {   super.updateItem(item, empty);  
        if (empty) 
        {   setText(null);  
            setGraphic(null);  
        } 
        else 
        {   if (isEditing()) 
            {   if (textField != null) 
                {   textField.setText(getString());  
                }  
                setText(null);  
                setGraphic(textField);  
            } 
            else 
            {   setText(getString());  
                setGraphic(null);  
            }  
        }  
    }  

    private void createTextField() 
    {   textField = new TextField(getString());  
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);  
        textField.focusedProperty().addListener(new ChangeListener<Boolean>()
        {   @Override  
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) 
            {   if (!arg2) 
                {   commitEdit(Double.parseDouble(textField.getText()));  
                }  
            }  
        });  
    }  
    
    private String getString() 
    {   return getItem() == null ? "" : getItem().toString();  
    }  
}

