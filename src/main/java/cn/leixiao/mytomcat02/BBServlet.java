package cn.leixiao.mytomcat02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BBServlet implements Servlet {
    @Override
    public void init() {
        System.out.println("BBServlet...init");
    }

    @Override
    public void service(InputStream inStream, OutputStream outStream) throws IOException {
        System.out.println("BBServlet...service");
        outStream.write("from BBServlet".getBytes());
        outStream.flush();
    }

    @Override
    public void destory() {
        System.out.println("BBServlet...destory");
    }
}
