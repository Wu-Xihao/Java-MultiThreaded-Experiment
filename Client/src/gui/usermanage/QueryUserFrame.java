package gui.usermanage;

import console.AbstractUser;
import gui.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueryUserFrame {
    JFrame jFrame;
    private JPanel OuterPanel;
    private JTextField TextField;
    private JButton ConfirmButton;
    private JButton CancelButton;
    private JLabel TextLabel;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private JTable UsersTable;
    private AbstractUser user;


    public QueryUserFrame(JFrame queryFrame,JTable usersTable,AbstractUser loginUser) {
        UsersTable=usersTable;
        user=loginUser;
        jFrame=queryFrame;
        jFrame.setTitle("查询用户");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        TextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm();
            }
        });
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm();
            }
        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });

    }

    private void confirm(){
        String userName=TextField.getText().trim();
        if(userName.equals("")||userName==null){//文本框信息检查是否为空
            MainGUI.showMessage(jFrame,"用户名不能为空！","提示信息");
        }else{
            int row=UsersTable.getRowCount();
            boolean flag=false;
            for(int i=0;i<row;i++){
                Object name=UsersTable.getValueAt(i,0);
                if(userName.equals(name.toString())){
                    flag=true;
                    UsersTable.setRowSelectionInterval(i,i);
                    UsersTable.scrollRectToVisible(UsersTable.getCellRect(i, 0, true));
                    UsersTable.setSelectionBackground(Color.gray);
                    jFrame.setVisible(false);
                    break;
                }
            }
            if(!flag){
                MainGUI.showMessage(jFrame, "未查询到该用户！", "提示信息");
            }
        }
    }
    private void cancel(){
        jFrame.dispose();
    }
}
