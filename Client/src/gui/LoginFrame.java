package gui;

import console.AbstractUser;
import console.DataProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame implements ActionListener{//系统登录窗口
    private JFrame jFrame;
    private JPanel panelLogin;
    private JPanel OuterPanel;
    private JPanel InnerPanel_1;
    private JPanel InnerPanel_2;
    private JPanel InnerPanel_3;
    private JTextField UserNameField;
    private JPasswordField PasswordField;
    private JButton LoginButton;
    private JButton ExitButton;
    private JLabel UserName;
    private JLabel Password;
    private JLabel Title;
    private String userName;
    private String password;

    public LoginFrame(JFrame jFrame) {
        this.jFrame=jFrame;
        this.jFrame.setTitle("系统登录");
        this.jFrame.setContentPane(getOuterPanel());//将面板加入到窗体
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口时退出程序
        this.jFrame.setResizable(false);//禁止改变窗口大小
        this.jFrame.pack();//自动根据组件匹配窗体大小
        this.jFrame.setLocationRelativeTo(null);//窗口居中显示
        this.jFrame.setVisible(true);//设置窗体可见
        //事件处理
        LoginButton.addActionListener(this);
        ExitButton.addActionListener(this);
    }

    private boolean checkInput(){
        userName= UserNameField.getText().trim();
        password=String.valueOf(PasswordField.getPassword()).trim();
        if(userName.equals("")||userName==null){
            MainGUI.showMessage(panelLogin,"用户名不能为空！","提示信息");
            UserNameField.requestFocus();
            return false;
        }else{
            PasswordField.requestFocus();
        }
        if(password.equals("")||password==null){
            MainGUI.showMessage(panelLogin,"密码不能为空！","提示信息");
            PasswordField.requestFocus();
            return false;
        }
        return true;
    }

    public JPanel getOuterPanel(){
        return OuterPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String event=e.getActionCommand();
        if(event.equals(LoginButton.getText())){
            System.out.println("登录");
            if(checkInput()){
                login();
            }
        }else if(event.equals(ExitButton.getText())){
            System.out.println("取消");
            System.exit(0);
        }
    }

    private void login(){
        try{
            AbstractUser user= DataProcessing.searchUser(userName,password);
            if(user==null){
                MainGUI.showMessage(jFrame,"用户名或密码错误！","提示信息");
            }else{
                showMainFrame(user);//显示主窗口
                jFrame.dispose();//关闭登录窗口并释放其资源
            }
        }catch(SQLException e){
            e.printStackTrace();

        }
    }
    private void showMainFrame(AbstractUser user){
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                try{
                    MainFrame mainFrame=new MainFrame(user);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
