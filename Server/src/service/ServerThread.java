package service;

import base.DataProcessing;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ServerThread {
    private Socket client;
    private int ConnectID;
    private DataInputStream dis;//服务端输入对象
    private FileOutputStream fos;//文件输出对象（上传时向文件中写入信息）
    private DataOutputStream dos;//服务端输出对象
    private FileInputStream fis;//文件输入对象（下载时从文件中读取信息）

    public ServerThread(Socket socket,int id){
        client=socket;
        ConnectID=id;
        try{
            try{
                dis=new DataInputStream(client.getInputStream());
                dos=new DataOutputStream(client.getOutputStream());
                String operation=dis.readUTF();
                if(operation.equals("upload")){
                    //服务器从客户端上传文件操作
                    System.out.println("客户端"+ConnectID+"：文件上传");
                    uploadServer();
                }else if(operation.equals("download")){
                    //服务器向客户端发送下载文件
                    System.out.println("客户端"+ConnectID+"：文件下载");
                    downloadServer();
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }finally{
                if(dis!=null){
                    dis.close();
                }
                if(dos!=null){
                    dos.close();
                }
                if(fis!=null){
                    fis.close();
                }
                if(fos!=null){
                    fos.close();
                }
                client.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void uploadServer() throws IOException, SQLException {
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
        //向数据库中添加上传文件信息
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DataProcessing.insertDoc(fileID, fileCreator, timestamp, fileDescription, fileName);
    }
    public void downloadServer() throws Exception {
        String fileName=dis.readUTF();//获取文件名
        String filePath="D:\\OOPExperiment\\uploadfile\\"+fileName;
        File file=new File(filePath);
        if(!file.exists()){
            throw new Exception("file miss!");
        }
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
