package cn.leixiao.mytomcat02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Servlet {
    public void init();
    public void service(InputStream inStream, OutputStream outStream) throws IOException;
    public void destory();
}
