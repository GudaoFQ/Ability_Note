package desgindemo.responsibilitychain.simpleresponsibilitychain;

/**
 * 执行链抽象类
 * Author : GuDao
 * 2020-10-16
 */
public abstract class Handler {
    private Handler nextHandler;

    /**
     * 绑定下个责任链实体方法
     *
     * @param reqHandler 请求处理程序
     * @return {@link Handler}
     */
    public void setHandler(Handler reqHandler){
        this.nextHandler = reqHandler;
    }


    /**
     * 获取下一个责任链实体方法
     *
     * @return {@link Handler}
     */
    public Handler getHandler(){
        return nextHandler;
    }

    /**
     * 处理消息
     *
     * @param message 消息
     * @return boolean
     */
    public abstract String handlerMsg(String message);

    /**
     * 指派下个责任链实体处理数据
     *
     * @param msg 信息
     */
    public String next(String msg){
        if(null == nextHandler){
            return msg;
        }
        return this.nextHandler.handlerMsg(msg);
    }
}
