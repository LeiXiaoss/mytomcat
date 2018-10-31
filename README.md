#简单的web服务器

第一个版本mytomcatv01只能响应静态文件,第二个版本mytomcatv02可以响应静态文件和配置好的动态程序（姑且命名为servlet）。实际上就是一段实现了http协议的Java程序。

***************************************************************************************************************************************************************************************************************************

##实现方式：

1、使用ServerSocket监听本机的8080端口， 获取socket对象，实现与客户端的socket连接

2、通过socket对象，获取输入流和输出流对象

3、通过输入流获取HTTP请求行和请求头

4、对请求行和请求头做解析，获取客户端强求的资源名称（静态或者动态）

5、针对静态资源和动态资源分别作出处理

6、对于静态资源，从WebContent中，查找是否有存在所请求的资源，有的话通过输出流返回这个资源，否则404

7、对于动态资源，通过请求服务的名称，在静态的map中查找，是否存在对应的类，存在的话，通过类路径反射加载这个类到内存中，实例化这个类，并执行它的方法。