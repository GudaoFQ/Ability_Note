package desgindemo.responsibilitychain.simpleresponsibilitychain;

/**
 * 责任链中的句号执行器
 * Author : GuDao
 * 2020-10-16
 */
public class PeriodHandler extends Handler{

    @Override
    public String handlerMsg(String message) {
        message = String.valueOf(new StringBuffer(message).append("."));
        if(message.contains("fuck")){
            return "信息存在不雅内容";
        }else if(null == getHandler()){
            //执行链中没有下一个执行实体，处理完成，执行结束
            return message;
        }else {
            return next(message);
        }
    }
}
