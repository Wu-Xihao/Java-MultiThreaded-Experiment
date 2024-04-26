package gui.usermanage;

import console.AbstractUser;
import console.DataProcessing;
import gui.MainGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

public class UpdateUserFrame {
    private JFrame jFrame;
    private JPanel OuterPanel;
    private JTextField NameField;
    private JPasswordField FirstPasswordField;
    private JPasswordField SecondPasswordField;
    private JComboBox RoleComboBox;
    private JButton ConfirmButton;
    private JButton CancelButton;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private JLabel NameLabel;
    private JLabel FirstPasswordLabel;
    private JLabel SecondPasswordLabel;
    private JLabel RoleLabel;
    private JTable UsersTable;//用户列表
    private AbstractUser updateUser;//需要修改的用户
    private AbstractUser loginUser;//操作用户

    public UpdateUserFrame(JFrame updateFrame,JTable table,AbstractUser user,AbstractUser loginUser) {
        jFrame=updateFrame;
        UsersTable=table;
        updateUser=user;
        this.loginUser=loginUser;
        jFrame.setTitle("修改用户");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //初始化文本框中为原用户信息
        NameField.setText(updateUser.getName());
        FirstPasswordField.setText(updateUser.getPassword());
        SecondPasswordField.setText(updateUser.getPassword());
        setComboBox();
        RoleComboBox.setEditable(false);
        //事件监听
        NameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkName();
            }
        });
        FirstPasswordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkFirstPassword();
            }
        });
        SecondPasswordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkSecondPassword();
            }
        });
        RoleComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.SELECTED==e.getStateChange()){
                    RoleComboBox.setSelectedItem(e.getItem());
                    RoleComboBox.hidePopup();
                }
            }
        });
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm();
                System.out.println("确认修改");
            }
        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("取消修改");
                jFrame.dispose();
            }
        });
    }

    private void setComboBox(){
        String[] roles={"系统管理员","档案录入人员","档案浏览人员"};//administrator operator browser
        ComboBoxModel<String> model=new DefaultComboBoxModel<>(roles);
        RoleComboBox.setModel(model);
        //将用户角色设定为原角色信息
        int row=RoleComboBox.getItemCount();
        String userRole=transform(updateUser.getRole());
        Object temp;
        for(int i=0;i<row;i++){
            temp=RoleComboBox.getItemAt(i);
            if(temp.toString().equals(userRole)){
                RoleComboBox.setSelectedItem(temp);
                break;
            }
        }
        RoleComboBox.hidePopup();
    }
    private String transform(String role){
        if(role.equals("administrator")){
            return "系统管理员";
        }else if(role.equals("operator")){
            return "档案录入人员";
        }else{
            return "档案浏览人员";
        }
    }
    private String originRole(String role){
        if(role.equals("系统管理员")){
            return "administrator";
        }else if(role.equals("档案录入人员")){
            return "operator";
        }else{
            return "browser";
        }
    }
    private Boolean checkName(){
        String name=NameField.getText().trim();
        if(name.equals("")||name==null){
            MainGUI.showMessage(jFrame,"用户名不能为空！","提示信息");
            NameField.requestFocus();
            return false;
        }
        return true;
    }
    private Boolean checkFirstPassword(){
        String password=String.valueOf(FirstPasswordField.getPassword()).trim();
        if(password.equals("")||password==null){
            MainGUI.showMessage(jFrame,"密码不能为空！","提示信息");
            FirstPasswordField.requestFocus();
            return false;
        }else{
            SecondPasswordField.setText("");
            SecondPasswordField.requestFocus();
            return true;
        }
    }
    private Boolean checkSecondPassword(){
        String password=String.valueOf(SecondPasswordField.getPassword()).trim();
        if(password.equals("")||password==null){
            MainGUI.showMessage(jFrame,"请再次输入密码！","提示信息");
            SecondPasswordField.requestFocus();
            return false;
        }
        return true;
    }
    private void confirm(){
        String lastName=updateUser.getName();//原用户名
        String name=NameField.getText().trim();//现用户名
        String firstPassword=String.valueOf(FirstPasswordField.getPassword()).trim();
        String secondPassword=String.valueOf(SecondPasswordField.getPassword()).trim();
        String selectRole=(String)RoleComboBox.getSelectedItem();
        if(!firstPassword.equals(secondPassword)){
            MainGUI.showMessage(jFrame,"两次输入的密码不相同！","提示信息");
            FirstPasswordField.requestFocus();
        }else{
            Boolean flag=false;
            try{
                if(name.equals(lastName)){
                    DataProcessing.updateUser(name,firstPassword,originRole(selectRole));
                    flag=true;
                }else{
                    //将哈希表中的原用户信息删除
                    DataProcessing.deleteUser(lastName);
                    //在哈希表中添加修改用户信息
                    if(selectRole.equals("系统管理员")){
                        flag= DataProcessing.insertUser(name,firstPassword,"administrator");
                    }else if(selectRole.equals("档案录入人员")){
                        flag=DataProcessing.insertUser(name,firstPassword,"operator");
                    }else if(selectRole.equals("档案浏览人员")){
                        flag= DataProcessing.insertUser(name,firstPassword,"browser");
                    }
                }
                if(!flag){
                    MainGUI.showMessage(jFrame,"该用户名已被使用！","提示信息");
                    //如果修改信息失败，需要将原用户信息重新添加到哈希表中
                    DataProcessing.insertUser(updateUser.getName(),updateUser.getPassword(),updateUser.getRole());
                    NameField.setText("");
                    NameField.requestFocus();
                }else{
                    //修改用户列表
                    String[] user={name,firstPassword,selectRole};
                    DefaultTableModel model=(DefaultTableModel)UsersTable.getModel();
                    int row=UsersTable.getRowCount();
                    for(int i=0;i<row;i++){
                        if(name.equals(UsersTable.getValueAt(i,0).toString())){
                            model.removeRow(i);//将用户列表中原用户的那一行删除
                            model.addRow(user);//将修改后的用户信息添加到类表中
                            UsersTable.setModel(model);
                            break;
                        }
                    }
                    jFrame.setVisible(false);
                    JOptionPane.showConfirmDialog(null,"用户修改成功","提示信息",JOptionPane.CLOSED_OPTION);
                    System.out.println("用户修改成功");
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
