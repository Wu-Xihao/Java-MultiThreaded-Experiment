package client;

import java.io.*;
import java.net.Socket;

public class FileDownload extends Socket {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private Socket client;
    private FileOutputStream fos;//文件写入对象
    private DataInputStream dis;//客户端输入对象
    private DataOutputStream dos;//客户端输出对象
    public FileDownload(String filepath,String fileName) throws IOException {
        try{
            try{
                client=new Socket(SERVER_IP,SERVER_PORT);
                dos=new DataOutputStream(client.getOutputStream());
                dis=new DataInputStream(client.getInputStream());
                dos.writeUTF("download");
                dos.flush();
                dos.writeUTF(fileName);
                dos.flush();
                File file=new File(filepath+fileName);
                fos=new FileOutputStream(file);
                Long fileLength= dis.readLong();
                byte[] sendBytes=new byte[1024];
                Long transLength=0L;
                System.out.println("开始接收下载文件："+fileName+" 文件大小为：："+fileLength);
                while (true){
                    int read=0;
                    read = dis.read(sendBytes);
                    if (read == -1)
                        break;
                    transLength += read;
                    System.out.println("接收文件进度" + 100 * transLength / fileLength + "%...");
                    fos.write(sendBytes, 0, read);
                    fos.flush();
                }
                System.out.println("文件下载成功");
            }catch(Exception e){
                throw e;
            }finally{
                if(fos!=null){
                    fos.close();
                }
                if(dos!=null){
                    dos.close();
                }
                if(dis!=null){
                    dis.close();
                }
                client.close();
            }
        }catch(Exception e){
            throw e;
        }

    }

}
