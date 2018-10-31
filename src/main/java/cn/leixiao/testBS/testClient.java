package cn.leixiao.testBS;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class testClient {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStream inStream = null;
        OutputStream outStream = null;
        try {
            //获取socket对象，连接80端口
            socket = new Socket("www.dygod.net",80);
            //获取输出流对象
            inStream = socket.getInputStream();
            //获取输入流对象
            outStream = socket.getOutputStream();
            //将HTTP协议的请求部分发送到服务端 /subject/about/index.html
            outStream.write("GET /html/gndy/dyzz/20130310/41720.html HTTP/1.1 \n".getBytes());
            outStream.write("HOST:www.dygod.net\n".getBytes());
            outStream.write("\n".getBytes());
            //读取来自服务端的数据打印到控制台
            int i = inStream.read();
            while (i!=-1){
                System.out.print((char) i);
                i = inStream.read();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放资源
            if(inStream != null){
                inStream.close();
                inStream = null;
            }
            if(outStream != null){
                outStream.close();
                outStream = null;
            }
            if(socket != null){
                socket.close();
                socket = null;
            }
        }

    }
}
