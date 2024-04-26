package console;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static final Scanner scanner=new Scanner(System.in);
    public static void main(String[] args){
        AbstractUser user = null;
        String input;
        int num;
        DataProcessing.init();
        menu();
        while(true){
            input=scanner.next().trim();
            if(!(input).matches("1|2")){
                System.err.print("编号错误，请重新输入：");
                continue;
            }
            num=Integer.parseInt(input);
            if(num==1){
                String name,password;
                System.out.print("请输入用户名与密码：");
                name=scanner.next();
                password=scanner.next();
                try{
                    user=DataProcessing.searchUser(name,password);
                }catch(SQLException e){
                    System.out.println(e.getMessage());
                }finally {
                    if(user!=null){
                        user.showMenu();
                    }
                }
            }else if(num==2){
                scanner.close();
                DataProcessing.disconnectFromDataBase();
                DataProcessing.beforeExit();
                System.out.println("系统退出, 谢谢使用 ! ");
                System.exit(0);
            }
            menu();
        }
    }
    public static void menu(){
        System.out.println("****欢迎进入档案系统****");
        System.out.println("    【1】登录");
        System.out.println("    【2】退出");
        System.out.println("*********************");
        System.out.print("请输入编号：");
    }
}
