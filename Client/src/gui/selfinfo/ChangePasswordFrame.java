package gui.selfinfo;

import console.AbstractUser;
import console.DataProcessing;
import gui.MainGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ChangePasswordFrame {
    private JFrame jFrame;
    private JPanel OuterPanel;
    private JPasswordField FirstPasswordField;
    private JPasswordField SecondPasswordField;
    private JButton ConfirmButton;
    private JButton CancelButton;
    private JLabel FirstPasswordLabel;
    private JLabel SecondPasswordLabel;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private AbstractUser loginUser;
    private JTextField lastPasswordFile;

    public ChangePasswordFrame(JFrame changeFrame,AbstractUser user,JTextField passwordField) {
        loginUser=user;
        lastPasswordFile=passwordField;
        jFrame=changeFrame;
        jFrame.setTitle("修改密码");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //事件监听
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
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("确认");
                Boolean flag=(checkFirstPassword()&&checkSecondPassword());
                if(flag){
                    confirm();
                }
            }
        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("取消");
                jFrame.dispose();
            }
        });
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
        }
        return true;
    }
    private void confirm(){
        //验证两次输入密码是否相同
        String firstPassword=String.valueOf(FirstPasswordField.getPassword()).trim();
        String secondPassword=String.valueOf(SecondPasswordField.getPassword()).trim();
        if(!firstPassword.equals(secondPassword)){
            MainGUI.showMessage(jFrame,"两次输入的密码不相同！","提示信息");
            FirstPasswordField.requestFocus();
        }else{
            //更新哈希表中的数据
            loginUser.setPassword(firstPassword);
            try{
                DataProcessing.updateUser(loginUser.getName(), firstPassword, loginUser.getRole());
                jFrame.setVisible(false);
                JOptionPane.showConfirmDialog(null,"密码修改成功","提示信息",JOptionPane.PLAIN_MESSAGE);
                //更新个人中心窗口中的密码
                lastPasswordFile.setText(firstPassword);
                System.out.println("密码更新成功");
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
