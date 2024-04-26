package gui.selfinfo;

import console.AbstractUser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelfMessageFrame {//个人中心窗口
    private JFrame jFrame;
    private AbstractUser loginUser;
    private JPanel OuterPanel;
    private JTextField NameField;
    private JTextField PasswordField;
    private JTextField RoleField;
    private JButton ChangePasswordButton;
    private JButton CancelButton;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private JLabel NameLabel;
    private JLabel PasswordLabel;
    private JLabel RoleLabel;
    private JPanel TitlePanel;
    private JLabel TitleLabel;

    public SelfMessageFrame(AbstractUser user){
        loginUser=user;
        //窗体设置
        jFrame=new JFrame("个人中心");
        jFrame.setContentPane(OuterPanel);//将面板加入到窗体
        jFrame.setVisible(true);
        jFrame.setResizable(false);//不可改变窗体界面大小
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //组件设置
        NameField.setText(loginUser.getName());
        NameField.setEditable(false);
        PasswordField.setText(loginUser.getPassword());
        PasswordField.setEditable(false);
        RoleField.setText(transform(loginUser.getRole()));
        RoleField.setEditable(false);

        ChangePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("修改密码");
                changePassword();
                JFrame changeFrame=new JFrame();
                ChangePasswordFrame changePasswordFrame=new ChangePasswordFrame(changeFrame,loginUser,PasswordField);
            }
        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("退出个人中心");
                jFrame.dispose();
            }
        });
    }

    private void changePassword(){

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
}

