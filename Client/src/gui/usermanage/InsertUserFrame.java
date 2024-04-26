package gui.usermanage;

import console.DataProcessing;
import gui.MainGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

public class InsertUserFrame {
    private JFrame jFrame;
    private JPanel OuterPanel;
    private JPanel ButtonPanel;
    private JPanel MessagePanel;
    private JButton InsertButton;
    private JButton ExitButton;
    private JTextField UserNameField;
    private JPasswordField FirstPasswordField;
    private JPasswordField SecondPasswordField;
    private JComboBox RoleComboBox;
    private JLabel nameLabel;
    private JLabel passwordLabel;
    private JLabel passwordAgainLabel;
    private JLabel roleLabel;
    private JTable userTable;
    private String selectRole;


    public InsertUserFrame(JFrame addFrame,JTable jTable) {
        userTable=jTable;
        //窗体设置
        jFrame=addFrame;
        jFrame.setTitle("添加用户");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //设置角色下拉列表框
        selectRole="系统管理员";//角色选择默认值
        setComboBox();
        RoleComboBox.setEditable(false);

        UserNameField.addActionListener(new ActionListener() {
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
                if(e.SELECTED==e.getStateChange()){//若状态改变的项为被选择的项
                    RoleComboBox.setSelectedItem(e.getItem());
                    selectRole = (String) RoleComboBox.getSelectedItem();//获取角色信息
                    RoleComboBox.hidePopup();//将弹出的菜单框隐藏
                }
            }
        });
        InsertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean flag=(checkName() && checkFirstPassword() && checkSecondPassword());
                if(flag){
                    confirm();
                }
            }
        });
        ExitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                System.out.println("取消添加用户");
            }
        });
    }

    private void setComboBox(){
        String[] roles={"系统管理员","档案录入人员","档案浏览人员"};//administrator operator browser
        ComboBoxModel<String> model=new DefaultComboBoxModel<>(roles);
        RoleComboBox.setModel(model);
    }
    private Boolean checkName(){
        String name=UserNameField.getText().trim();
        if(name.equals("")||name==null){
            MainGUI.showMessage(jFrame,"用户名不能为空！","提示信息");
            UserNameField.requestFocus();
            return false;
        }else{
            FirstPasswordField.requestFocus();
            return true;
        }
    }
    private Boolean checkFirstPassword(){
        String password=String.valueOf(FirstPasswordField.getPassword()).trim();
        if(password.equals("")||password==null){
            MainGUI.showMessage(jFrame,"密码不能为空！","提示信息");
            FirstPasswordField.requestFocus();
            return false;
        }else{
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
        }else{
            RoleComboBox.requestFocus();
            return true;
        }
    }

    private void confirm(){
        String name=UserNameField.getText().trim();
        String firstPassword=String.valueOf(FirstPasswordField.getPassword()).trim();
        String secondPassword=String.valueOf(SecondPasswordField.getPassword()).trim();
        if(!firstPassword.equals(secondPassword)){
            MainGUI.showMessage(jFrame,"两次输入的密码不相同！","提示信息");
            FirstPasswordField.requestFocus();
        }else{
            try{
                Boolean sign=false;
                if(selectRole.equals("系统管理员")){
                    sign= DataProcessing.insertUser(name,firstPassword,"administrator");
                }else if(selectRole.equals("档案录入人员")){
                    sign=DataProcessing.insertUser(name,firstPassword,"operator");
                }else if(selectRole.equals("档案浏览人员")){
                    sign= DataProcessing.insertUser(name,firstPassword,"browser");
                }
                if(!sign){
                    MainGUI.showMessage(jFrame,"该用户名已被使用！","提示信息");
                    UserNameField.setText("");
                    UserNameField.requestFocus();
                }else{
                    String[] user={name,firstPassword,selectRole};
                    DefaultTableModel model=(DefaultTableModel)userTable.getModel();
                    model.addRow(user);
                    userTable.setModel(model);
                    jFrame.setVisible(false);
                    JOptionPane.showConfirmDialog(null,"用户添加成功","提示信息",JOptionPane.CLOSED_OPTION);
                    System.out.println("用户添加成功");
                }
            }catch(SQLException exception){
                exception.printStackTrace();
            }

        }
    }
}
