package base;

import java.io.IOException;
import java.sql.SQLException;

public class Browser extends AbstractUser {
    Browser(String name, String password, String role) {
        super(name, password, role);
    }
    void menuTopBrowser(){
        System.out.println("****欢迎进入档案浏览人员菜单****");
        System.out.println("    【1】下载文档");
        System.out.println("    【2】档案列表");
        System.out.println("    【3】修改个人密码");
        System.out.println("    【4】退出登录");
        System.out.println("*****************************");
        System.out.print("请输入菜单编号：");
    }
    @Override
    public void showMenu() {
        int num=0;
        String input;
        boolean flag=true;
        menuTopBrowser();
        while(num!=4){
            input=Main.scanner.next().trim();
            if(!(input).matches("1|2|3|4")){
                System.err.println("输入编号错误，请重新输入：");
                continue;
            }
            num=Integer.parseInt(input);
            switch (num){
                case 1:{
                    System.out.println("***下载列表***");
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
                case 2:{
                    System.out.println("***档案列表***");
                    try{
                        showFileList();
                    }catch(SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3:{
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
                    System.out.println("完成！");
                    break;
                }
                case 4:{
                    flag=false;
                    System.out.println("退出登录");
                    break;
                }
            }
            System.out.println();
            if(flag){
                menuTopBrowser();
            }else{
                break;
            }
        }
    }
}
