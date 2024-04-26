package gui;

import console.AbstractUser;
import gui.docmanage.DocFrame;
import gui.selfinfo.SelfMessageFrame;
import gui.usermanage.UserFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame implements ActionListener {//主界面窗口
    private JFrame jFrame;
    private JPanel mainPanel;
    private JButton UserManageButton;
    private JButton DocManageButton;
    private JButton MessageButton;
    private JButton ExitButton;
    private JLabel Menu;
    private AbstractUser loginUser;

    public MainFrame(AbstractUser user){
        this.jFrame=new JFrame("主窗口");
        loginUser=user;
        jFrame.setContentPane(mainPanel);//将面板加入到窗体
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭窗口时退出应用程序
        jFrame.setPreferredSize(new Dimension(350,350));//设置窗口大小
        jFrame.setResizable(false);//禁止改变窗口大小
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);//窗口位置居中显示
        jFrame.setVisible(true);//窗口设置可见
        setTabAvailable();//根据用户角色设置可选功能
        //注册事件监听器
        UserManageButton.addActionListener(this);
        DocManageButton.addActionListener(this);
        MessageButton.addActionListener(this);
        ExitButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String event=e.getActionCommand();
        int operation=0;
        if(event.equals(UserManageButton.getText())){
            System.out.println("用户管理");
            operation=1;
        }else if(event.equals(DocManageButton.getText())){
            System.out.println("档案管理");
            operation=2;
        }else if(event.equals(MessageButton.getText())){
            System.out.println("个人中心");
            operation=3;
        }else if(event.equals(ExitButton.getText())){
            System.out.println("退出登录");
            operation=4;
        }
        int finalOperation = operation;
        EventQueue.invokeLater(new Runnable(){
            public void run(){
                try{
                    switch(finalOperation){
                        case 1:{
                            System.out.println("打开用户管理窗口");
                            UserFrame userFrame=new UserFrame(loginUser);
                            break;
                        }
                        case 2:{
                            System.out.println("打开档案管理窗口");
                            DocFrame docManageButton=new DocFrame(loginUser);
                            break;
                        }
                        case 3:{
                            System.out.println("打开个人中心窗口");
                            SelfMessageFrame selfMessageFrame=new SelfMessageFrame(loginUser);
                            break;
                        }
                        case 4:{
                            System.out.println("退出登录");
                            jFrame.dispose();//关闭主界面窗口并释放资源
                            //新建系统登录界面
                            EventQueue.invokeLater(new Runnable(){
                                public void run(){
                                    try{
                                        LoginFrame loginFrame=new LoginFrame(new JFrame());
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            //退出应用程序
                            Runtime.getRuntime().addShutdownHook(new Thread(){
                                public void run(){
                                    //此处添加退出程序前应该进行的操作，如：关闭网络连接、关闭数据库连接等
                                    System.out.println("应用程序退出！");
                                }

                            });
                            break;
                        }

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setTabAvailable(){
        String role=loginUser.getRole();
        if(!(role.equalsIgnoreCase("administrator"))){
            UserManageButton.setEnabled(false);
        }
    }
}
