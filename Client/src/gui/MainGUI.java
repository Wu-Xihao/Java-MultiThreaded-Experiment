package gui;

import console.DataProcessing;

import javax.swing.*;
import java.awt.*;

public class MainGUI {
    public static void main(String[] args){
        DataProcessing.init();//从文件读入用户和档案信息
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
                DataProcessing.disconnectFromDataBase();
                DataProcessing.beforeExit();//退出前将所有信息序列化存入文件中
                System.out.println("应用程序退出！");
            }

        });
    }

    public static void showMessage(Component component,String msg,String title){
        JOptionPane.showMessageDialog(component,msg,title,JOptionPane.YES_NO_OPTION);
    }
    public static int showConfirmMessage(Component component,String msg,String title){
        return JOptionPane.showConfirmDialog(component,msg,title,JOptionPane.OK_CANCEL_OPTION);
    }
}
