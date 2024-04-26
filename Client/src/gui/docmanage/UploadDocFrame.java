package gui.docmanage;

import client.FileUpload;
import console.AbstractUser;
import console.DataProcessing;
import console.Doc;
import gui.MainGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Timestamp;

public class UploadDocFrame {
    private JFrame jFrame;
    private JPanel OuterPanel;
    private JTextField pathField;
    private JTextField fileField;
    private JTextField descirbeField;
    private JButton ConfirmButton;
    private JButton CancelButton;
    private JLabel TitleLabel;
    private JLabel pathLabel;
    private JLabel fileLabel;
    private JLabel describeLabel;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private JPanel TitlePanel;
    private JTextField idField;
    private JLabel idLabel;
    private JButton SeleteFileButton;
    private AbstractUser user;//传入正在进行操作的用户信息
    private JTable DocTable;
    private Doc newDoc;//上传的文件信息，用于在DocFrame中读取从而更新档案列表
    private File selectFile;

    public UploadDocFrame(JFrame uploadFrame, AbstractUser user,JTable docTable){
        this.user=user;
        DocTable=docTable;
        newDoc=null;
        selectFile=null;
        jFrame=uploadFrame;
        jFrame.setTitle("上传档案");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //事件监听
        idField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkID();
            }
        });
        pathField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkPath();
            }
        });
        fileField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkFile();
            }
        });
        descirbeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkDescribe();
            }
        });
        ConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Boolean flag =(checkID()&&checkPath()&&checkFile()&&checkDescribe());
                if(flag){
                    confirm();
                }
            }
        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                System.out.println("取消上传");
            }
        });

        SeleteFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path=pathField.getText().trim();
                JFileChooser fileChooser=new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//只可以选择文件
                fileChooser.setMultiSelectionEnabled(false);//设置不可多选
                if(!(path.equals("")||path==null)){
                    File filePath=new File(path);
                    if(filePath.exists()){
                        fileChooser.setSelectedFile(new File(path));
                    }
                }
                int state=fileChooser.showOpenDialog(jFrame);//显示窗体
                selectFile=fileChooser.getSelectedFile();
                if(selectFile!=null){
                    String fileName=selectFile.getName();
                    fileField.setText(fileName);
                    pathField.setText(selectFile.getPath());
                }
            }
        });
    }

    private Boolean checkID(){
        String id=idField.getText().trim();
        if(id.equals("")||id==null){
            MainGUI.showMessage(jFrame,"档案编号不能为空！","提示信息");
            idField.requestFocus();
            return false;
        }else if(DataProcessing.docs.containsKey(id)){
            MainGUI.showMessage(jFrame,"该档案编号已存在！","提示信息");
            idField.setText("");
            idField.requestFocus();
            return false;
        }else{
            pathField.requestFocus();
            return true;
        }
    }
    private boolean checkPath(){
        String path=pathField.getText().trim();
        if(path.equals("")||path==null){
            MainGUI.showMessage(jFrame,"路径不能为空！","提示信息");
            pathField.requestFocus();
            return false;
        }else{
            fileField.requestFocus();
            return true;
        }
    }
    private Boolean checkFile(){
        String file=fileField.getText().trim();
        if(file.equals("")||file==null){
            MainGUI.showMessage(jFrame,"文件名不能为空！","提示信息");
            fileField.requestFocus();
            return false;
        }else{
            describeLabel.requestFocus();
            return true;
        }
    }
    private  Boolean checkDescribe(){
        String describe=descirbeField.getText().trim();
        if(describe.equals("")||describe==null){
            MainGUI.showMessage(jFrame,"文件描述不能为空！","提示信息");
            descirbeField.requestFocus();
            return false;
        }
        return true;
    }
    private void confirm(){
        String path=pathField.getText().trim();
        String fileName=fileField.getText().trim();
        String uploadPath="D:\\OOPExperiment\\uploadfile\\";//文档上传路径（可改）
        File fileReader;
        if(selectFile!=null){
            fileReader=selectFile;
        }else {
            fileReader=new File(path);
        }
        if(!fileReader.exists()){
            MainGUI.showMessage(jFrame,"未找到该文件！","提示信息");
        }else{
            byte[] buffer=new byte[1024];
            File fileWrite;
            fileWrite=new File(uploadPath+fileName);
            try{
                //将档案信息插入到哈希表中
                String id=idField.getText().trim();
                String description=descirbeField.getText().trim();
                Timestamp timestamp = new  Timestamp(System.currentTimeMillis());
                //数据库中添加记录
                //DataProcessing.insertDoc(id,user.getName(),timestamp,description,fileName);
                //将新文档信息添加到文档列表中
                newDoc=new Doc(id,user.getName(),timestamp,description,fileName);
                new FileUpload(path,newDoc);
                resortTable();
                jFrame.setVisible(false);//上传成功后上传界面设为不可见
                //JOptionPane.showMessageDialog(component,msg,title,JOptionPane.YES_NO_OPTION);
                JOptionPane.showConfirmDialog(null,"文档上传成功","提示信息",JOptionPane.PLAIN_MESSAGE);
                System.out.println("上传成功!");
            }catch(Exception e){
                System.out.println(e.getMessage());
                MainGUI.showMessage(jFrame,"文件上传失败！","提示信息");
            }
        }
    }

    private void resortTable(){
        DefaultTableModel model=(DefaultTableModel)DocTable.getModel();
        String[] newData={newDoc.getId(),newDoc.getCreator(),newDoc.getFilename(),newDoc.getTimestamp().toString(),newDoc.getDescription()};
        model.addRow(newData);
        DocTable.setModel(model);
    }

}
