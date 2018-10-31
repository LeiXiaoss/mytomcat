package cn.leixiao.testBS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class testServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        OutputStream outStream=null;
        try{
            //创建serversocket对象，监听本机8080端口号
            serverSocket = new ServerSocket(8080);
            while (true){
                //等待来自客户端的请求获取和客户端对应的socket对象
                socket = serverSocket.accept();
                //通过socket对象获取到输出流对象
                outStream = socket.getOutputStream();
                //通过获取的输出流对象将HTTP协议的相应部分发送到客户端
                outStream.write("HTTP/1.1 200 OK\n".getBytes());
                outStream.write("Content-Type:text/html;charset=utf-8\n".getBytes());
                outStream.write("Server:Apache-Coyote/1.1\n".getBytes());
                outStream.write("\n\n".getBytes());
                StringBuffer buf = new StringBuffer();
                buf.append("<html>");
                buf.append("<head><title>标题</title></head>");
                buf.append("<body>");
                buf.append("<h1>header1</h1>");
                buf.append("<a href='http://www.baidu.com'>baidu</a>");
                buf.append("</body>");
                buf.append("</html>");
                outStream.write(buf.toString().getBytes());
                outStream.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放资源
            if (outStream!=null){
                outStream.close();
                outStream = null;
            }
            if (socket != null){
                socket.close();
                socket = null;
            }
        }
    }
}
