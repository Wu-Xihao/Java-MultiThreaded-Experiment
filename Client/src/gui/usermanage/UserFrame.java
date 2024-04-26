package gui.usermanage;

import console.AbstractUser;
import console.DataProcessing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

public class UserFrame {//用户信息列表窗口
    private JFrame jFrame;
    private JPanel OuterPanel;//列表显示用户信息
    private JPanel ButtonPanel;
    private JButton AddButton;
    private JButton QueryButton;
    private JButton UpdateButton;
    private JScrollPane TablePanel;
    private JTable UsersTable;
    private JButton DelButton;
    private JLabel TitleLabel;
    private JPanel TitlePanel;
    private AbstractUser loginUser;
    public UserFrame(AbstractUser user){
        loginUser=user;
        //窗体设置
        jFrame=new JFrame("用户管理");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //表格设置
        insertTable();
        //事件监听
        AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });
        QueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queryUser();
            }
        });
        UpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });
        DelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delUser();
            }
        });
    }

    //将用户信息添加到列表中
    private void insertTable(){
        String[] columnNames={"用户名","密码","角色"};
        String[][] usersData;
        try{
            Enumeration<AbstractUser> users= DataProcessing.listUser();
            ArrayList<AbstractUser> list=new ArrayList<>();
            while(users.hasMoreElements()){
                AbstractUser user=users.nextElement();
                list.add(user);
            }
            int row=list.size();
            usersData=new String[row][3];
            row=0;
            for(AbstractUser user:list){
                usersData[row][0]=user.getName();
                usersData[row][1]=user.getPassword();
                usersData[row][2]=transform(user.getRole());
                row++;
            }
            DefaultTableModel model=new DefaultTableModel(usersData,columnNames);
            UsersTable.setModel(model);
        }catch(SQLException e){
            e.printStackTrace();
        }
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
    //添加用户
    private void addUser(){
        JFrame addFrame=new JFrame();
        InsertUserFrame insertUserFrame=new InsertUserFrame(addFrame,UsersTable);
    }
    //查询用户
    private void queryUser(){
        JFrame queryFrame=new JFrame();
        QueryUserFrame queryUserFrame=new QueryUserFrame(queryFrame,UsersTable,loginUser);
    }
    //修改用户
    private void updateUser(){
        JFrame verifyFrame=new JFrame();
        VerifyUserFrame verifyUserFrame=new VerifyUserFrame(verifyFrame,UsersTable,loginUser);
    }
    //删除用户
    private void delUser(){
        JFrame delFrame=new JFrame();
        DelUserFrame delUserFrame=new DelUserFrame(delFrame,UsersTable,loginUser);
    }


}
