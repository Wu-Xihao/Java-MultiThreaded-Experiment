package console;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

public class Administrator extends AbstractUser {
    Administrator(String name, String password, String role) {
        super(name, password, role);
    }

    public static void menuTopAdministrator(){
        System.out.println("****欢迎进入系统管理员菜单****");
        System.out.println("    【1】新增用户");
        System.out.println("    【2】删除用户");
        System.out.println("    【3】修改用户");
        System.out.println("    【4】用户列表");
        System.out.println("    【5】下载列表");
        System.out.println("    【6】档案列表");
        System.out.println("    【7】修改个人密码");
        System.out.println("    【8】退出登录");
        System.out.println("***************************");
        System.out.print("请输入菜单编号：");
    }
    public void addUserAdministrator() throws SQLException {
        System.out.println("【1】系统管理员");
        System.out.println("【2】档案录入人员");
        System.out.println("【3】档案浏览人员");
        System.out.print("请选择用户角色类型：");
        int roleNum;
        roleNum=Main.scanner.nextInt();
        String name,password;
        System.out.print("请输入姓名与密码：");
        name=Main.scanner.next();
        password=Main.scanner.next();
        boolean sign = false;
        if(roleNum==1){
            sign=DataProcessing.insertUser(name,password,"administrator");
        }else if(roleNum==2){
            sign=DataProcessing.insertUser(name,password,"operator");
        } else if (roleNum == 3) {
            sign=DataProcessing.insertUser(name,password,"browser");
        }
        if(!sign){
            System.out.println("该名称已被使用，请重新尝试！");
        }else{
            System.out.println("完成！");
        }
    }
    public void deleteUserAdministrator() throws SQLException {
        String name,password;
        System.out.print("请输入用户姓名与密码：");
        name=Main.scanner.next();
        password=Main.scanner.next();
        AbstractUser user=DataProcessing.searchUser(name,password);
        if(user==null){
            System.out.println("用户名或密码错误！");
        }else{
            boolean sign=DataProcessing.deleteUser(name);
            if(!sign){
                System.out.println("删除失败！");
            }else{
                System.out.println("完成！");
            }
        }
    }
    public void changeUserAdministrator() throws SQLException {
        String name,password;
        System.out.print("请输入原用户姓名与密码：");
        name=Main.scanner.next();
        password=Main.scanner.next();
        AbstractUser user=DataProcessing.searchUser(name,password);
        if(user==null) {
            System.out.println("用户名或密码错误！");
        }else{
            String input;
            System.out.print("是否修改权限(y/n)：");
            while(true) {
                input = Main.scanner.next();
                if (!(input).matches("y|n")) {
                    System.err.print("输入错误，请重新输入：");
                }else{
                    if(input.equals("n")){
                        break;
                    }
                    System.out.println("【1】系统管理员");
                    System.out.println("【2】档案录入人员");
                    System.out.println("【3】档案浏览人员");
                    System.out.print("请选择用户角色类型：");
                    while(true){
                        input=Main.scanner.next();
                        if(!(input).matches("1|2|3")){
                            System.err.print("编号错误，请重新输入：");
                        }else break;
                    }
                    int num=Integer.parseInt(input);
                    if(num==1){
                        user.setRole("administrator");
                    }else if(num==2){
                        user.setRole("operator");
                    }else{
                        user.setRole("browser");
                    }
                    break;
                }
            }
            System.out.print("是否修改用户名(y/n)：");
            while(true){
                input = Main.scanner.next();
                if (!(input).matches("y|n")) {
                    System.err.print("输入错误，请重新输入：");
                }else {
                    if (input.equals("n")) {
                        break;
                    }
                    System.out.print("请输入新用户名：");
                    name=Main.scanner.next();
                    user.setName(name);
                    break;
                }

            }
            boolean sign=changeSelfInfo(password);
        }
    }

    public void userList() throws SQLException {
        Enumeration<AbstractUser> e=DataProcessing.listUser();
        while(e.hasMoreElements()){
            AbstractUser user=e.nextElement();
            String name=user.getName();
            String password=user.getPassword();
            String role=user.getRole();
            System.out.println(name+'\t'+password+'\t'+role);
        }
    }
    @Override
    public void showMenu() {
        int num=0;
        String input;
        boolean flag=true;
        menuTopAdministrator();
        while(true){
            input=Main.scanner.next().trim();
            if(!(input).matches("1|2|3|3|4|5|6|7|8")){
                System.err.print("输入编号错误，请重新输入：");
                continue;
            }
            num=Integer.parseInt(input);
            switch (num){
                case 1:{
                    System.out.println("***新增用户***");
                    try{
                        addUserAdministrator();
                    }catch(SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 2:{
                    System.out.println("***删除用户***");
                    try{
                        deleteUserAdministrator();
                    }catch(SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 3:{
                    System.out.println("***修改用户***");
                    try{
                        changeUserAdministrator();
                    }catch(SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 4:{
                    System.out.println("***用户列表***");
                    try{
                        userList();
                    }catch(SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 5:{
                    System.out.println("***下载列表***");
                    String s;
                    System.out.print("请输入文件编号：");
                    s=Main.scanner.next();
                    try{
                        downloadFile(s);
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 6:{
                    System.out.println("***档案列表***");
                    try{
                        showFileList();
                    }catch(SQLException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 7:{
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
                case 8:{
                    flag=false;
                    System.out.println("退出登录");
                    break;
                }
            }
            System.out.println();
            if(flag){
                menuTopAdministrator();
            }else{
                break;
            }

        }
    }
}
