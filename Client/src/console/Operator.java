package console;

import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Operator extends AbstractUser {
    Operator(String name, String password, String role) {
        super(name, password, role);
    }
    public void menuTopOperator(){
        System.out.println("****欢迎进入档案录入人员菜单****");
        System.out.println("    【1】上传文档");
        System.out.println("    【2】下载文档");
        System.out.println("    【3】档案列表");
        System.out.println("    【4】修改个人密码");
        System.out.println("    【5】退出登录");
        System.out.println("****************************");
        System.out.print("请输入菜单编号：");
    }

    /**
     * TODO 上传档案文件
     *
     * @param
     * @return void
     * @throws IOException
     */
    public void uploadFile() throws IOException{
        String docPath,docName,ID,description;
        System.out.print("请输入文件路径：");
        docPath= Main.scanner.next();
        System.out.print("请输入文件名：");
        docName=Main.scanner.next();
        System.out.print("请输入档案编号：");
        ID=Main.scanner.next();
        while(true){
            if(!DataProcessing.docs.containsKey(ID)){
                break;
            }else{
                System.out.print("该编号已存在，请重新输入：");
                ID=Main.scanner.next();
            }
        }
        System.out.print("请输入档案描述：");
        description=Main.scanner.next();
        byte[] buffer=new byte[1024];
        File fileReader=new File(docPath+docName);
        File fileWrite=new File(uploadpath+docName);
        try{
            FileInputStream infile=new FileInputStream(fileReader);
            BufferedInputStream in=new BufferedInputStream(infile);
            FileOutputStream outfile=new FileOutputStream(fileWrite);
            BufferedOutputStream out=new BufferedOutputStream(outfile);
            while(true){
                int byteRead=in.read(buffer);
                if(byteRead==-1){
                    break;
                }
                out.write(buffer,0,byteRead);
            }
            in.close();
            out.close();
            //将档案信息插入到哈希表中
            Timestamp timestamp = new  Timestamp(System.currentTimeMillis());
            DataProcessing.docs.put(ID,new Doc(ID,this.getName(),timestamp,description,docName));
            System.out.println("上传成功!");
        }catch(IOException e){
            throw e;
        }

    }
    @Override
    public void showMenu() {
        int num=0;
        String input;
        boolean flag=true;
        menuTopOperator();
        while(true){
            input=Main.scanner.next().trim();
            if(!(input).matches("1|2|3|3|4|5")){
                System.err.println("输入编号错误，请重新输入：");
                continue;
            }
            num=Integer.parseInt(input);
            switch(num){
                case 1:{
                    System.out.println("***上传文档***");
                    try{
                        uploadFile();
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 2:{
                    System.out.println("***下载文档***");
                    String s;
                    System.out.print("请输入文档编号：");
                    s=Main.scanner.next();
                    try{
                        downloadFile(s);
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3:{
                    System.out.println("***档案列表***");
                    try{
                        showFileList();
                    }catch(SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 4:{
                    System.out.println("***修改密码***");
                    String password;
                    System.out.print("请输入新密码：");
                    password=Main.scanner.next();
                    setPassword(password);
                    try{
                        DataProcessing.updateUser(getName(),password,getRole());
                    }catch(SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 5:{
                    flag=false;
                    System.out.println("退出登录");
                    break;
                }
            }
            System.out.println();
            if(flag){
                menuTopOperator();
            }else{
                break;
            }
        }
    }
}