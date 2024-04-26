package base;

import java.io.*;
import java.sql.SQLException;

/**
 * TODO 抽象用户类，为各用户子类提供模板
 *
 * @author gongjing
 * @date 2016/10/13
 */
public  abstract  class AbstractUser implements Serializable{
    static  final  double  EXCEPTION_PROBABILITY = 0.9;
    private  String name;
    private  String password;
    private  String role;
    public static final String uploadpath="D:\\OOPExperiment\\uploadfile\\";
    public static final String downloadpath="D:\\OOPExperiment\\downloadfile\\";

    AbstractUser(String name, String password, String role) {
        this .name = name;
        this .password = password;
        this .role = role;
    }


    /**
     * TODO 修改用户自身信息
     *
     * @param password 口令
     * @return boolean 修改是否成功
     * @throws SQLException
     */
    public  boolean  changeSelfInfo(String password) throws  SQLException {
        if  (DataProcessing.updateUser(name, password, role)) {
            this .password = password;
            System.out.println("修改成功");
            return  true ;
        } else  {
            return  false ;
        }
    }

    /**
     * TODO 下载档案文件
     *
     * @param ID 文件id
     * @return boolean 下载是否成功
     * @throws IOException
     */
    public boolean downloadFile(String ID) throws IOException{
        Doc doc=DataProcessing.docs.get(ID);
        if(doc==null){
            System.out.println("未找到该档案编号！");
            return false;
        }
        String docName=doc.getFilename();
        byte[] buffer=new byte[1024];
        File fileReader=new File(uploadpath+docName);
        File fileWrite=new File(downloadpath+docName);
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
            System.out.println("下载成功！");
        }catch(IOException e){
            throw e;
        }
        return true;
    }

    /**
     * TODO 展示档案文件列表
     *
     * @param
     * @return void
     * @throws SQLException
     */
    public  void  showFileList() throws  SQLException {
        /*double  ranValue = Math.random();
        if  (ranValue > EXCEPTION_PROBABILITY) {
            throw  new  SQLException("Error in accessing file DB");
        }
        System.out.println("列表... ...");*/
        Doc doc;
        for(String ID:DataProcessing.docs.keySet()){
            doc=DataProcessing.docs.get(ID);
            System.out.println("档案编号："+ID+'\t'+"文件名："+doc.getFilename()+'\t'+"创建者："+doc.getCreator()+'\t'+"创建时间："+doc.getTimestamp()+'\t'+"描述："+doc.getDescription());
        }
    }

    /**
     * TODO 展示菜单，需子类加以覆盖
     *
     * @param
     * @return void
     * @throws
     */
    public  abstract  void  showMenu();

    /**
     * TODO 退出系统
     *
     * @param
     * @return void
     * @throws
     */
    public  void  exitSystem() {
        System.out.println("系统退出, 谢谢使用 ! ");
        System.exit(0);
    }

    public  String getName() {
        return  name;
    }

    public  void  setName(String name) {
        this .name = name;
    }

    public  String getPassword() {
        return  password;
    }

    public  void  setPassword(String password) {
        this .password = password;
    }

    public  String getRole() {
        return  role;
    }

    public  void  setRole(String role) {
        this .role = role;
    }

}