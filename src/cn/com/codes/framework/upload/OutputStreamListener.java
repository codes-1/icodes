package cn.com.codes.framework.upload;

public interface OutputStreamListener
{
    public void start();//启动监听器
    public void bytesRead(int bytesRead);//读数据
    public void error(String message);//出错处理
    public void done();//完成监听
}

