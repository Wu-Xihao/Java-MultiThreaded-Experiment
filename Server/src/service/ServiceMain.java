package service;

import java.net.ServerSocket;
import java.net.Socket;

public class ServiceMain extends ServerSocket{
    private static final int PORT = 12345;
    private ServerSocket server;//服务端
    private int ConnectNum;
    public ServiceMain() throws Exception{
        ConnectNum=0;
        try{
            System.out.println("主服务器启动...");
            server=new ServerSocket(PORT,100);
            while(true){
                //等待客户端的连接
                Socket socket=server.accept();
                //创建一个新的线程，与客户端进行连接
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        int id=++ConnectNum;
                        System.out.println("与客户端"+id+"建立连接！");
                        //利用socket与客户端进行通信
                        new ServerThread(socket,id);
                        System.out.println("客户端"+id+"断开连接！");
                    }
                }).start();
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        try{
            new ServiceMain();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
