package gui.docmanage;

import gui.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QueryDocFrame implements ActionListener {
    private JFrame jFrame;
    private JTable DocTable;
    private JPanel OuterPanel;
    private JTextField textField;
    private JButton ConfirmButton;
    private JButton CancelButton;
    private JPanel TextPanel;
    private JPanel ButtonPanel;
    private JPanel TitlePanel;
    private JLabel TitleLabel;

    public QueryDocFrame(JFrame queryJFrame,JTable docTable){
        DocTable=docTable;
        jFrame=queryJFrame;
        jFrame.setTitle("档案查询");
        jFrame.setContentPane(OuterPanel);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //事件监听
        ConfirmButton.addActionListener(this);
        CancelButton.addActionListener(this);
    }

    //事件监听处理过程，事件包括：文本框输入回车、点击确认按钮和取消按钮
    @Override
    public void actionPerformed(ActionEvent e) {
        String event=e.getActionCommand();
        if(event.equals(textField.getText())){
            System.out.println("确认");
            confirm();
        }else if(event.equals(ConfirmButton.getText())){
            System.out.println("确认");
            confirm();
        }else if(event.equals(CancelButton.getText())){
            System.out.println("取消");
            cancel();
        }
    }
    //确认操作
    private void confirm(){
        String str=textField.getText().trim();
        if(str.equals("")||str==null){//文本框信息检查是否为空
            MainGUI.showMessage(jFrame,"档案编号不能为空！","提示信息");
        }else{
            int row = DocTable.getRowCount();//获取列表行数
            boolean flag = false;//是否查询到的标志
            for (int i = 0; i < row; i++) {
                Object value = DocTable.getValueAt(i, 0);//获取第i行第0类的信息，即档案号信息
                if (str.equals(value.toString())) {
                    flag = true;
                    DocTable.setRowSelectionInterval(i, i);//选中从i到i的行，即找到的第i行
                    DocTable.scrollRectToVisible(DocTable.getCellRect(i, 0, true));//滚动到选中的行
                    DocTable.setSelectionBackground(Color.gray);//设置选中的行的背景颜色为浅蓝色
                    jFrame.setVisible(false);
                    break;
                }
            }
            if (!flag) {//当未查询到档案号时弹窗提示
                MainGUI.showMessage(jFrame, "未查询到该档案！", "提示信息");
            }
        }

    }
    //取消操作
    private void cancel(){
        jFrame.dispose();
    }

}
