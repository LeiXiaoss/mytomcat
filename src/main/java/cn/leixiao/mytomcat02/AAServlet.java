package cn.leixiao.mytomcat02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AAServlet implements Servlet {
    @Override
    public void init() {
        System.out.println("AAServlet...init");
    }

    @Override
    public void service(InputStream inStream, OutputStream outStream) throws IOException {
        System.out.println("AAServlet..service");
        outStream.write("from AAServlet".getBytes());
        outStream.flush();
    }

    @Override
    public void destory() {
        System.out.println("AAServlet...destory");
    }
}
