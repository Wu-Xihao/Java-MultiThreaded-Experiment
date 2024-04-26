package gui.docmanage;

import client.FileDownload;
import console.DataProcessing;
import console.Doc;
import gui.MainGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class DownloadDocFrame {
    JFrame jFrame;
    private JPanel OuterPanel;
    private JTextField idField;
    private JTextField pathField;
    private JButton ConfirmButton;
    private JButton CancelButton;
    private JPanel TitlePanel;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private JLabel TitleLabel;
    private JLabel IDLabel;
    private JLabel PathLabel;
    private JButton SelectButton;
    private File selectPath;

    public DownloadDocFrame(JFrame downloadFrame) {
        jFrame=downloadFrame;
        jFrame.setTitle("下载档案");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //设置下载默认路径
        pathField.setText("D:\\OOPExperiment\\downloadfile");
        //事件监听
        idField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id=idField.getText().trim();
                if(id.equals("")||id==null){
                    MainGUI.showMessage(jFrame,"档案编号不能为空！","提示信息");
                    idField.requestFocus();
                }else if(DataProcessing.docs.containsKey(id)){
                    MainGUI.showMessage(jFrame,"该档案编号已存在！","提示信息");
                    idField.setText("");
                    idField.requestFocus();
                }else{
                    pathField.requestFocus();
                }
            }
        });
        pathField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path=pathField.getText().trim();
                if(path.equals("")||path==null){
                    MainGUI.showMessage(jFrame,"路径不能为空！","提示信息");
                    pathField.requestFocus();
                }
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
                jFrame.dispose();
                System.out.println("取消下载");
            }
        });
        SelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser=new JFileChooser();
                fileChooser.setCurrentDirectory(new File("D:\\OOPExperiment\\downloadfile"));//设置默认路径
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只可以选择文件夹
                int state=fileChooser.showOpenDialog(jFrame);//显示窗体
                selectPath=fileChooser.getSelectedFile();
                if(selectPath!=null){
                    pathField.setText(selectPath.getPath());
                }
            }
        });
    }

    private void confirm(){
        String id=idField.getText().trim();
        try{
            Doc doc= DataProcessing.searchDoc(id);
            String fileName=doc.getFilename();
            String uploadPath="D:\\OOPExperiment\\uploadfile\\";//文档上传路径（可改）
            String path=pathField.getText().trim()+"\\";
            try{
                new FileDownload(path,fileName);
                jFrame.setVisible(false);
                JOptionPane.showConfirmDialog(null,"文档下载成功","提示信息",JOptionPane.PLAIN_MESSAGE);
                System.out.println("下载成功！");
            }catch(Exception e){
                System.out.println(e.getMessage());
                MainGUI.showMessage(jFrame,"文件下载失败！","提示信息");
            }
        }catch(Exception e){
            MainGUI.showMessage(jFrame,"未找到该档案编号！","提示信息");
        }
    }
}
