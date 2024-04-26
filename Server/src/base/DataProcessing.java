package base;

import java.io.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * TODO 数据处理类
 *
 * @author gongjing
 * @date 2016/10/13
 */
public  class DataProcessing {
    public static  Hashtable<String, AbstractUser> users;
    public static  Hashtable<String, Doc> docs;
    private  static  boolean  connectToDB = false ;
    public static Connection connection;
    public static Statement statement;
    public static ResultSet resultSet;

    static  {
        users = new  Hashtable<String, AbstractUser>();
        docs=new Hashtable<>();
        //users.put("jack", new  Operator("jack", "123", "operator"));
        //users.put("rose", new  Browser("rose", "123", "browser"));
        //users.put("kate", new  Administrator("kate", "123", "administrator"));
        init();

        //Timestamp timestamp = new  Timestamp(System.currentTimeMillis());
        //docs = new  Hashtable<String, Doc>();
        //docs.put("0001", new  Doc("0001", "jack", timestamp, "Doc Source Java", "Doc.java"));
    }

    /**
     * TODO 初始化，连接数据库
     *
     * @param
     * @return void
     * @throws
     */
    public  static  void  init(){
        /*connectToDB = true ;
        File dataFile=new File("D:\\OOPExperiment\\data.out");
        if(!dataFile.exists()){
            users.put("jack", new  Operator("jack", "123", "operator"));
            users.put("rose", new Browser("rose", "123", "browser"));
            users.put("kate", new Administrator("kate", "123", "administrator"));
            Timestamp timestamp = new  Timestamp(System.currentTimeMillis());
            docs = new  Hashtable<String, Doc>();
            docs.put("0001", new  Doc("0001", "jack", timestamp, "Doc Source Java", "Doc.java"));
            return ;
        }
        try{
            ObjectInputStream in=new ObjectInputStream(new FileInputStream(dataFile));
            users=(Hashtable<String, AbstractUser>) in.readObject();
            docs=(Hashtable<String, Doc>) in.readObject();
            in.close();
        }catch(FileNotFoundException e){
            System.out.println("错误类型："+e.getMessage());
        }catch(IOException e){
            System.out.println("错误类型："+e.getMessage());//显示错误null
        }catch(ClassNotFoundException e){
            System.out.println("错误类型："+e.getMessage());
        }*/
        connectToDB=connectToDatabase();
        try{//将数据库中的数据写入users和docs中
            statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY );
            //String sqlSet="ALTER TABLE doc_info MODIFY Id column_definition";
            //statement.executeUpdate(sqlSet);
            String sql="select * from user_info";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String username=resultSet.getString("username");
                String pwd=resultSet.getString("password");
                String role=resultSet.getString("role");
                users.put(username,creatUser(username,pwd,role));
            }
            sql="select * from doc_info";
            resultSet= statement.executeQuery(sql);
            while(resultSet.next()){
                Doc temp=null;
                String docID=resultSet.getString("Id");
                String creator=resultSet.getString("creator");
                Timestamp timestamp=resultSet.getTimestamp("timestamp");
                String description=resultSet.getString("description");
                String filename=resultSet.getString("filename");
                temp=new Doc(docID,creator,timestamp,description,filename);
                docs.put(docID,temp);
            }
            resultSet.close();
            statement.close();
            //connection.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static AbstractUser creatUser(String name,String password,String role){
        if(role.equals("administrator")){
            return new Administrator(name, password, role);
        }else if(role.equals("operator")){
            return new Operator(name, password, role);
        }else if(role.equals("browser")){
            return new Browser(name, password, role);
        }else{
            return null;
        }
    }

    public static boolean connectToDatabase(){//建立数据库连接
        connectToDB=false;
        String driverName="com.mysql.cj.jdbc.Driver";//加载数据库驱动类
        String url="jdbc:mysql://localhost:3306/document?serverTimezone=GMT%2B8&useSSL=false";
        String user="root";//数据库用户
        String password="041001";//密码
        try{
            Class.forName(driverName);
            connection=DriverManager.getConnection(url,user,password);//建立数据库连接
            connectToDB=true;
        }catch(ClassNotFoundException e){
            System.out.println("数据库驱动类名错误");
            e.printStackTrace();
        }catch(SQLException e){
            System.out.println("数据库连接错误");
            e.printStackTrace();
        }
        return connectToDB;
    }

    /**
     * TODO 按档案编号搜索档案信息，返回null时表明未找到
     *
     * @param id
     * @return Doc
     * @throws SQLException
     */
    public  static  Doc searchDoc(String id) throws  SQLException {
        //connectToDB=connectToDatabase();
        if  (!connectToDB) {//未连接上数据库抛出异常
            throw  new  IllegalStateException("数据库未连接！");
        }
        Doc temp=null;
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        String sql="select * from doc_info where id='"+id+"'";
        try{
            resultSet=statement.executeQuery(sql);
            if(resultSet.next()){
                String docID=resultSet.getString("Id");
                String creator=resultSet.getString("creator");
                Timestamp timestamp=resultSet.getTimestamp("timestamp");
                String description=resultSet.getString("description");
                String filename=resultSet.getString("filename");
                temp=new Doc(docID,creator,timestamp,description,filename);
            }
        }catch(SQLException e){
            throw e;
        }finally {
            resultSet.close();
            statement.close();
            //connection.close();
        }
        return temp;

    }

    /**
     * TODO 列出所有档案信息
     *
     * @param
     * @return Enumeration<Doc>
     * @throws SQLException
     */
    public  static  Enumeration<Doc> listDoc() throws  SQLException {
        //connectToDB=connectToDatabase();
        if  (!connectToDB) {
            throw  new  IllegalStateException("数据库未连接！");
        }
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        String sql="select * from doc_info";
        try{
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                String docID=resultSet.getString("Id");
                String creator=resultSet.getString("creator");
                Timestamp timestamp=resultSet.getTimestamp("timestamp");
                String description=resultSet.getString("description");
                String filename=resultSet.getString("filename");
                docs.put(docID,new Doc(docID,creator,timestamp,description,filename));
            }
        }catch(SQLException e){
            throw e;
        }finally {
            resultSet.close();
            statement.close();
            //connection.close();
        }
        return docs.elements();
    }

    /**
     * TODO 插入新的档案
     *
     * @param id
     * @param creator
     * @param timestamp
     * @param description
     * @param filename
     * @return boolean
     * @throws SQLException
     */
    public  static  boolean  insertDoc(String id, String creator, Timestamp timestamp, String description, String filename) throws  SQLException {
        Doc doc;
        //connectToDB = connectToDatabase();
        if  (!connectToDB) {
            throw  new  IllegalStateException("数据库未连接！");
        }
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        String sqlSearch="select * from doc_info where id='"+id+"'";
        String sqlInsert="insert into doc_info(Id,creator,timestamp,description,filename) values (?,?,?,?,?)";
        PreparedStatement preparedStatement= connection.prepareStatement(sqlInsert);
        boolean flag=false;
        try{
            resultSet=statement.executeQuery(sqlSearch);//查询是否有重名
            if  (resultSet.next())//如果根据此id能够找到数据，则表示用户名重复，插入失败
                flag=false;
            else  {
                preparedStatement.setString(1,id);
                preparedStatement.setString(2,creator);
                preparedStatement.setTimestamp(3,timestamp);
                preparedStatement.setString(4,description);
                preparedStatement.setString(5,filename);
                int count=preparedStatement.executeUpdate();//将该条数据添加到数据库
                if(count!=0){
                    flag=true;
                }
                docs.put(id,new Doc(id,creator,timestamp,description,filename));
            }
        }catch(SQLException e){
            System.out.println("数据库错误");
            throw e;
        }finally {
            resultSet.close();
            preparedStatement.close();
            statement.close();
            //connection.close();
        }
        return flag;
    }

    /**
     * TODO 按用户名搜索用户，返回null时表明未找到符合条件的用户
     *
     * @param name 用户名
     * @return AbstractUser
     * @throws SQLException
     */
    public  static  AbstractUser searchUser(String name) throws  SQLException {
        //connectToDB = connectToDatabase();
        if  (!connectToDB) {
            throw  new  IllegalStateException("数据库未连接！");
        }
        AbstractUser user=null;
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        String sql="select * from user_info where username='"+name+"'";
        try{
            resultSet=statement.executeQuery(sql);
            if(resultSet.next()){
                String username=resultSet.getString("username");
                String pwd=resultSet.getString("password");
                String role=resultSet.getString("role");
                if(role.equals("administrator")){
                    user=new Administrator(username,pwd,role);
                }else if(role.equals("operator")){
                    user=new Operator(username,pwd,role);
                }else if(role.equals("browser")){
                    user=new Browser(username,pwd,role);
                }
            }
        }catch(SQLException e){
            throw e;
        }finally {
            resultSet.close();
            statement.close();
            //connection.close();
        }
        return  user;
    }

    /**
     * TODO 按用户名、密码搜索用户，返回null时表明未找到符合条件的用户
     *
     * @param name 用户名
     * @param password 密码
     * @return AbstractUser
     * @throws SQLException
     */
    public  static  AbstractUser searchUser(String name, String password) throws  SQLException {
        //connectToDB = connectToDatabase();
        if  (!connectToDB) {
            throw  new  IllegalStateException("数据库未连接！");
        }
        AbstractUser user=null;
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        String sql="select * from user_info where username='"+name+"'";
        try{
            resultSet=statement.executeQuery(sql);
            if(resultSet.next()){
                String username=resultSet.getString("username");
                String pwd=resultSet.getString("password");
                String role=resultSet.getString("role");
                if(pwd.equals(password)){
                    user=creatUser(username,pwd,role);
                }
            }
        }catch(SQLException e){
            throw e;
        }finally {
            resultSet.close();
            statement.close();
            //connection.close();
        }
        return user;
    }

    /**
     * TODO 取出所有的用户
     *
     * @param
     * @return Enumeration<AbstractUser>
     * @throws SQLException
     */
    public  static  Enumeration<AbstractUser> listUser() throws  SQLException {
        //connectToDB = connectToDatabase();
        if  (!connectToDB) {
            throw  new  IllegalStateException("数据库未连接！");
        }
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        String sql="select * from user_info";
        try{
            resultSet=statement.executeQuery(sql);
            while(resultSet.next()){
                AbstractUser user=null;
                String username=resultSet.getString("username");
                String pwd=resultSet.getString("password");
                String role=resultSet.getString("role");
                user=creatUser(username,pwd,role);
                users.put(username,user);
            }
        }catch(SQLException e){
            throw e;
        }finally{
            resultSet.close();
            statement.close();
            //connection.close();
        }
        Enumeration<AbstractUser> e = users.elements();
        return  e;
    }

    /**
     * TODO 修改用户信息
     *
     * @param name 用户名
     * @param password 密码
     * @param role 角色
     * @return boolean
     * @throws SQLException
     */
    public  static  boolean  updateUser(String name, String password, String role) throws  SQLException {
        //connectToDB = connectToDatabase();
        if  (!connectToDB) {
            throw  new  IllegalStateException("数据库未连接！");
        }
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        String sql="update user_info set password='"+password+"'"+",role='"+role+"'"+" where username='"+name+"'";
        boolean flag=false;
        try{
            int count=statement.executeUpdate(sql);
            if(count!=0){
                flag=true;
            }
        }catch(SQLException e){
            throw e;
        }finally {
            //resultSet.close();
            statement.close();
            //connection.close();
        }
        return flag;
    }

    /**
     * TODO 插入新用户
     *
     * @param name 用户名
     * @param password 密码
     * @param role 角色
     * @return boolean
     * @throws SQLException
     */
    public  static  boolean  insertUser(String name, String password, String role) throws  SQLException {
        //connectToDB = connectToDatabase();
        if  (!connectToDB) {
            throw  new  IllegalStateException("数据库未连接！");
        }
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        String sqlSearch="select * from user_info where username='"+name+"'";
        String sqlInsert="insert into user_info(username,password,role) values ('"+name+"','"+password+"','"+role+"')";
        boolean flag=false;
        int count;
        try{
            resultSet=statement.executeQuery(sqlSearch);//查询是否有重名
            if  (resultSet.next())
                flag=false;
            else  {
                count=statement.executeUpdate(sqlInsert);//将该条数据添加到数据库
                if(count !=0){
                    users.put(name,creatUser(name,password,role));
                    flag=true;
                }
            }
        }catch(SQLException e){
            System.out.println("数据库错误");
            throw e;
        }finally {
            resultSet.close();
            statement.close();
            //connection.close();
        }
        return flag;
    }

    /**
     * TODO 删除指定用户
     *
     * @param name 用户名
     * @return boolean
     * @throws SQLException
     */
    public  static  boolean  deleteUser(String name) throws  SQLException {
        //connectToDB = connectToDatabase();
        if  (!connectToDB) {
            throw  new  IllegalStateException("数据库未连接！");
        }
        statement=connection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY
        );
        boolean flag=false;
        String sql="delete from user_info where username='"+name+"'";
        try{
            int count = statement.executeUpdate(sql);
            if(count!=0){
                users.remove(name);
                flag=true;
            }
        }catch(SQLException e){
            throw e;
        }finally {
            //resultSet.close();
            statement.close();
            //connection.close();
        }
        return flag;
    }

    /**
     * TODO 关闭数据库连接
     *
     * @param
     * @return void
     * @throws
     */
    public  static  void  disconnectFromDataBase() {
        if  (connectToDB) {
            // close Statement and Connection
            try  {
                connection.close();
            } catch (SQLException e){
                System.out.println(e.getMessage());
                System.out.println("数据库连接断开失败！");
            }finally  {
                connectToDB = false ;
            }
        }
    }

    /**
     * TODO 退出程序前将信息序列化
     *
     * @param
     * @return void
     * @throws
     */
    public static void beforeExit(){
        File dataFile=new File("D:\\OOPExperiment\\data.out");
        try{
            ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(dataFile));
            out.writeObject(users);
            out.writeObject(docs);
            out.close();
        }catch(FileNotFoundException e){
            System.out.println("错误类型："+e.getMessage());
        }catch(IOException e){
            System.out.println("错误类型："+e.getMessage());
        }
    }

    static  enum  ROLE_ENUM {
        /**
         * administrator
         */
        administrator("administrator"),
        /**
         * operator
         */
        operator("operator"),
        /**
         * browser
         */
        browser("browser");

        private  String role;

        ROLE_ENUM(String role) {
            this .role = role;
        }

        public  String getRole() {
            return  role;
        }
    }

}
