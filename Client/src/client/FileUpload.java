package client;

import console.Doc;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileUpload extends Socket {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 12345;
    private Socket client;
    private FileInputStream fis;
    private DataOutputStream dos;
    public FileUpload(String filePath, Doc uploadDoc) throws IOException {
        try{
            try{
                client=new Socket(SERVER_IP,SERVER_PORT);
                //向服务端发送文件
                File file=new File(filePath);
                fis=new FileInputStream(file);
                dos=new DataOutputStream(client.getOutputStream());

                dos.writeUTF("upload");
                dos.flush();
                dos.writeUTF(file.getName());//发送文件名
                dos.flush();
                dos.writeLong(file.length());//发送文件长度
                dos.flush();
                dos.writeUTF(uploadDoc.getId());//发送文件编号
                dos.flush();
                dos.writeUTF(uploadDoc.getCreator());//发送上传者
                dos.flush();
                dos.writeUTF(uploadDoc.getDescription());//发送文件描述
                dos.flush();

                //传输文件内容
                byte[] sendBytes=new byte[1024];
                int length=0;
                while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                    dos.write(sendBytes, 0, length);
                    dos.flush();
                }

            }catch(Exception e){
                throw e;
            }finally {
                if(fis!=null){
                    fis.close();
                }
                if(dos!=null){
                    dos.close();
                }
                client.close();
            }
        }catch(Exception e){
            throw e;
        }
    }
}
