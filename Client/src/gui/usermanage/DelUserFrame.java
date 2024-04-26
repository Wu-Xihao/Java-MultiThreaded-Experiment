package gui.usermanage;

import console.AbstractUser;
import console.DataProcessing;
import gui.MainGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DelUserFrame {
    JFrame jFrame;
    private JPanel OuterPanel;
    private JButton ConfirmButton;
    private JButton CancelButton;
    private JTextField textField;
    private JPasswordField passwordField;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private JLabel NameLabel;
    private JLabel PasswordLabel;
    private JTable UsersTable;
    private AbstractUser loginuser;

    public DelUserFrame(JFrame delFrame,JTable table,AbstractUser loginUser) {
        this.loginuser=loginUser;
        UsersTable=table;
        //窗体设置
        jFrame=delFrame;
        jFrame.setTitle("删除用户");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        textField.addActionListener(new ActionListener() {
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
        String name=textField.getText().trim();
        if(name.equals("")||name==null){
            MainGUI.showMessage(jFrame,"用户名不能为空！","提示信息");
            textField.requestFocus();
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
    private void checkDelUser(String name,String password){
        if(loginuser.getName().equals(name)){
            jFrame.setVisible(false);
            MainGUI.showMessage(jFrame,"无法删除操作者用户信息！","提示信息");
            jFrame.dispose();
            return;
        }
        try{
            AbstractUser user= DataProcessing.searchUser(name,password);
            if(user==null){
                MainGUI.showMessage(jFrame,"用户名或密码错误！","提示信息");
                passwordField.requestFocus();
            }else{
                //将哈希表中的该用户信息删除
                DataProcessing.deleteUser(name);
                //将用户列表中的对应行的用户信息删除
                DefaultTableModel model=(DefaultTableModel)UsersTable.getModel();
                int row=UsersTable.getRowCount();
                for(int i=0;i<row;i++){
                    if(name.equals(UsersTable.getValueAt(i,0).toString())){
                        model.removeRow(i);
                        UsersTable.setModel(model);
                        break;
                    }
                }
                jFrame.setVisible(false);
                //弹窗提示删除成功
                JOptionPane.showConfirmDialog(null,"用户删除成功","提示信息",JOptionPane.CLOSED_OPTION);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private void confirm(){
        String name=textField.getText().trim();
        String password=String.valueOf(passwordField.getPassword()).trim();
        checkDelUser(name,password);
    }
    private void cancel(){
        jFrame.dispose();
    }

}
