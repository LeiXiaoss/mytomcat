package cn.leixiao.mytomcat02;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class testServer {
    //定义WebContent的绝对路径
    public static String WEB_ROOT = System.getProperty("user.dir")+"\\"+"WebContent";
    //定义静态变量，用于存放请求的静态页面名称
    public static String url = "";
    //定义一个map，存储conf配置信息
    public static Map<String,String> map  = new HashMap<>();

    static {
        //静态代码块读取配置信息
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream(WEB_ROOT+"\\conf.properties"));
            Set set = properties.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                String value = properties.getProperty(key);
                map.put(key,value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(map);
        ServerSocket serverSocket = null;
        Socket socket = null;
        OutputStream outStream = null;
        InputStream inStream = null;
        try {
            //监听8080端口，等待客户端的请求
            serverSocket = new ServerSocket(8080);
            while (true){
                socket = serverSocket.accept();
                outStream = socket.getOutputStream();
                inStream = socket.getInputStream();
                //获取HTTP协议的请求部分，截取客户端要访问的资源名称，将资源名称赋值给URL
                parse(inStream);
                //判断请求的是静态页面，还是运行在Java端的servlet代码
                if(url != null){
                    if(url.indexOf(".")!=-1) {
                        //发送静态资源
                        sendStaticResources(outStream);
                    }else {
                        //发送动态相应
                        sendDynamicResources(outStream,inStream);
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(outStream != null){
                outStream.close();
                outStream = null;
            }
            if(inStream != null){
                inStream.close();
                inStream = null;
            }
            if(socket != null){
                socket.close();
                socket = null;
            }
        }
    }

    private static void sendDynamicResources(OutputStream outStream, InputStream inStream)
            throws Exception {
        //发送响应行和响应头
        outStream.write("HTTP/1.1 200 OK\n".getBytes());
        outStream.write("Content-Type:text/html;charset=utf-8\n".getBytes());
        outStream.write("Server:Apache-Coyote/1.1\n".getBytes());
        outStream.write("\n".getBytes());
        //判断静态map中是否存在这个key
        if(map.containsKey(url)){
            String value = map.get(url);
            //通过反射加载对应的类
            Class clazz = Class.forName(value);
            Servlet servlet = (Servlet) clazz.newInstance();
            servlet.init();
            servlet.service(inStream,outStream);
            servlet.destory();
        }
    }

    private static void parse(InputStream inStream) throws IOException {
        StringBuffer content = new StringBuffer(2048);
        byte[] buffer = new byte[2048];
        int i=-1;
        i = inStream.read(buffer);
        for (int j=0;j<i;j++){
            content.append((char)buffer[j]);
        }
        //System.out.println(content);

        //截取客户端请求资源的路径，赋值给URL
        parseUrl(content.toString());
    }

    private static void parseUrl(String content) {
        //GET /demo01.html HTTP/1.1
        //Host: localhost:8080
        //User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0
        //Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
        //Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2
        //Accept-Encoding: gzip, deflate
        //Connection: keep-alive
        //Upgrade-Insecure-Requests: 1

        //定义两个变量存储 请求行两个空格的位置
        int index1,index2;
        //获取第一个空格的位置
        index1 = content.indexOf(" ");
        if(index1 != -1){
            //获取第二个空格的位置
            index2 = content.indexOf(" ",index1+1);
            if(index2>index1){
                //截取自字符串获取本次请求资源的名称
                url = content.substring(index1+2,index2);
            }
        }
        System.out.println(url);
    }

    private static void sendStaticResources(OutputStream outStream) throws IOException {
        //定义字节数组，存放请求的静态资源的内容
        byte[] bytes = new byte[2048];
        //定义文件输入流，获取静态资源的内容
        FileInputStream fileInStream = null;
        try {
            //创建文件对象，代表本次请求的资源
            File file = new File(WEB_ROOT,url);
            //如果文件存在
            if(file.exists()){
                //向客户端输出HTTP协议的响应行，响应头
                outStream.write("HTTP/1.1 200 OK\n".getBytes());
                outStream.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                outStream.write("Server:Apache-Coyote/1.1\n".getBytes());
                outStream.write("\n".getBytes());
                //获取文件输入流对象
                fileInStream = new FileInputStream(file);
                //读取静态资源的内容到数组中
                int ch = fileInStream.read(bytes);
                //将资源通过输出流发送到客户端
                while (ch != -1){
                    outStream.write(bytes,0,ch);
                    ch = fileInStream.read(bytes);
                }
            }else {
                //如果文件不存在
                //向客户端响应文件不存在的消息
                outStream.write("HTTP/1.1 404 not found\n".getBytes());
                outStream.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                outStream.write("Server:Apache-Coyote/1.1\n".getBytes());
                outStream.write("\n".getBytes());
                String errorMessage = "file not found";
                outStream.write(errorMessage.getBytes());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fileInStream != null){
                fileInStream.close();
                fileInStream = null;
            }
        }

    }


}
