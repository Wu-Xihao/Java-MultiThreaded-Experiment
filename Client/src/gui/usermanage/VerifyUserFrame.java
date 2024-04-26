package gui.usermanage;

import console.AbstractUser;
import console.DataProcessing;
import gui.MainGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class VerifyUserFrame {
    private JFrame jFrame;
    private JPanel OuterPanel;
    private JButton ConfirmButton;
    private JButton CancelButton;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private JLabel NameLabel;
    private JLabel PasswordLabel;
    private JTable UsersTable;
    private AbstractUser loginUser;

    public VerifyUserFrame(JFrame updateFrame,JTable table,AbstractUser user) {
        jFrame=updateFrame;
        UsersTable=table;
        loginUser=user;
        jFrame.setTitle("修改用户");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        nameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkName();
            }
        });
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPassword();
            }
        });
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean flag=(checkName() && checkPassword());
                if(flag){
                    confirm();
                }
            }
        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
    }

    private Boolean checkName(){
        String name=nameField.getText().trim();
        if(name.equals("")||name==null){
            MainGUI.showMessage(jFrame,"用户名不能为空！","提示信息");
            nameField.requestFocus();
            return false;
        }else{
            passwordField.requestFocus();
            return true;
        }
    }
    private Boolean checkPassword(){
        String password=String.valueOf(passwordField.getPassword()).trim();
        if(password.equals("")||password==null){
            MainGUI.showMessage(jFrame,"密码不能为空！","提示信息");
            passwordField.requestFocus();
            return false;
        }
        return true;
    }
    private void confirm(){
        String name=nameField.getText().trim();
        String password=String.valueOf(passwordField.getPassword()).trim();
        try{
            AbstractUser user= DataProcessing.searchUser(name,password);
            if(user==null){
                MainGUI.showMessage(jFrame,"用户名或密码错误！","提示信息");
                passwordField.requestFocus();
            }else if(name.equals(loginUser.getName())){
                MainGUI.showMessage(jFrame,"个人用户信息不可修改！","提示信息");
                nameField.requestFocus();
            }else{
                //此确认用户窗口隐藏，弹出修改窗口
                jFrame.setVisible(false);
                JFrame updateFrame=new JFrame();
                UpdateUserFrame updateUserFrame=new UpdateUserFrame(updateFrame,UsersTable,user,loginUser);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private void cancel(){
        jFrame.dispose();
    }
}
