package gui.docmanage;

import console.AbstractUser;
import console.DataProcessing;
import console.Doc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

public class DocFrame implements ActionListener {//文档管理窗口
    private JFrame jFrame;
    private AbstractUser loginUser;
    private JPanel OuterPanel;
    private JTable DocTable;
    private JButton queryButton;
    private JButton uploadButton;
    private JButton downloadButton;
    private javax.swing.JScrollPane JScrollPane;
    private JPanel ButtonPanel;
    private JLabel TitleLabel;
    private JButton restoreButton;
    AbstractUser user;

    public DocFrame(AbstractUser user){
        this.user=user;
        //窗体设置
        jFrame=new JFrame("档案管理");
        jFrame.setContentPane(OuterPanel);//将面板加入到窗体
        jFrame.setVisible(true);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//程序退出时关闭窗口
        //表格设置
        insertTable();//将文档数据添加到表格
        //权限设置
        setTabAvailable();
        //事件监听
        queryButton.addActionListener(this);
        uploadButton.addActionListener(this);
        downloadButton.addActionListener(this);
        restoreButton.addActionListener(this);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String event=e.getActionCommand();
        if(event.equals(queryButton.getText())){
            System.out.println("查询档案");
            queryDoc();
        }else if(event.equals(uploadButton.getText())){
            System.out.println("上传档案");
            uploadDoc();
        }else if(event.equals(downloadButton.getText())){
            System.out.println("下载档案");
            downloadDoc();
        }else if(event.equals(restoreButton.getText())){
            System.out.println("刷新档案列表");
            restoreDoc();
        }
    }

    private void insertTable(){
        String[] columnNames={"档案号","创建者","文件名","时间","描述"};
        String[][] DocData;
        try{
            Enumeration<Doc> docs= DataProcessing.listDoc();
            ArrayList<Doc> list=new ArrayList<>();
            while(docs.hasMoreElements()){
                Doc doc=docs.nextElement();
                list.add(doc);
            }
            int row =list.size();
            DocData=new String[row][5];
            row=0;
            for(Doc doc:list){
                DocData[row][0]=doc.getId();
                DocData[row][1]=doc.getCreator();
                DocData[row][2]=doc.getFilename();
                DocData[row][3]=doc.getTimestamp().toString();
                DocData[row][4]=doc.getDescription();
                row++;
            }
            DefaultTableModel tableModelDocs = new DefaultTableModel(DocData, columnNames);
            DocTable.setModel(tableModelDocs);//tableModelDocs为JTable的对象
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    //查询档案操作
    private void queryDoc(){
        JFrame queryJFrame = new JFrame();
        QueryDocFrame query = new QueryDocFrame(queryJFrame,DocTable);
    }
    //上传档案操作
    private void uploadDoc(){
        JFrame uploadJFrame=new JFrame();
        UploadDocFrame uploadDocFrame=new UploadDocFrame(uploadJFrame,user,DocTable);
    }
    //下载档案操作
    private void downloadDoc(){
        JFrame downloadFrame=new JFrame();
        DownloadDocFrame downloadDocFrame=new DownloadDocFrame(downloadFrame);
    }
    //刷新
    private void restoreDoc(){
        insertTable();
    }

    private void setTabAvailable(){
        String role=user.getRole();
        if(role.equals("browser")||role.equals("administrator")){
            uploadButton.setEnabled(false);
        }
    }
}
