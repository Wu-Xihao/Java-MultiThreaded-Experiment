package service;

import base.DataProcessing;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;

public class SocketService extends ServerSocket{
    private static final int PORT = 12345;

    private ServerSocket server;//服务端
    private Socket client;//客户端套接字
    private DataInputStream dis;//服务端输入对象
    private FileOutputStream fos;//文件输出对象（上传时向文件中写入信息）
    private DataOutputStream dos;//服务端输出对象
    private FileInputStream fis;//文件输入对象（下载时从文件中读取信息）

    public SocketService() throws Exception{
        try {
            try {
                System.out.println("主服务器启动...");
                server = new ServerSocket(PORT,100);
                while (true) {
                    client = server.accept();
                    dis = new DataInputStream(client.getInputStream());
                    //获取操作类别
                    String operation = dis.readUTF();
                    //上传文件的响应操作
                    if (operation.equals("upload")) {
                        //获取相关文件信息
                        String fileName = dis.readUTF();//文件名
                        Long fileLength = dis.readLong();//文件长度
                        String fileID = dis.readUTF();//文件编号
                        String fileCreator = dis.readUTF();//文件上传者
                        String fileDescription = dis.readUTF();//文件描述
                        //服务器上传文件操作
                        fos = new FileOutputStream(new File("D:\\OOPExperiment\\uploadfile\\" + fileName));
                        byte[] sendBytes = new byte[1024];
                        Long transLength = 0L;
                        System.out.println("开始接收上传文件：" + fileName + "  文件大小为：" + fileLength);
                        while (true) {
                            int read = 0;
                            read = dis.read(sendBytes);
                            if (read == -1) {
                                break;
                            }
                            transLength += read;
                            System.out.println("接收文件进度：" + 100 * transLength / fileLength + "%...");
                            fos.write(sendBytes, 0, read);
                            fos.flush();
                        }
                        System.out.println("接收文件成功！");
                        client.close();
                        //向数据库中添加上传文件信息
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        DataProcessing.insertDoc(fileID, fileCreator, timestamp, fileDescription, fileName);
                    }
                    //下载文件的响应操作
                    if (operation.equals("download")) {
                        System.out.println("下载文件操作...");
                        String fileName=dis.readUTF();//获取文件名
                        String filePath="D:\\OOPExperiment\\uploadfile\\"+fileName;
                        File file=new File(filePath);
                        if(!file.exists()){
                            throw new Exception("file miss!");
                        }
                        dos=new DataOutputStream(client.getOutputStream());
                        dos.writeLong(file.length());//发送文件大小
                        dos.flush();
                        //服务器下载操作
                        fis=new FileInputStream(file);
                        byte[] sendBytes = new byte[1024];
                        int length = 0;
                        while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                            dos.write(sendBytes, 0, length);
                            dos.flush();
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                if (dis != null) {
                    dis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (fis != null) {
                    fis.close();
                }
                server.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        try{
            new SocketService();
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
