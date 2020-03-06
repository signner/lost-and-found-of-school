package com.shiro.pojo.re;

public class MyException extends Exception {
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MyException(ExceptionType type)
    {
        String message="";
        switch (type){
           case select: message="查询失败，检查查询条件是否正确";break;
            case delete: message="删除失败，token是否过期，未过期请及时联系管理员";break;
            case update: message="更新失败，token是否过期，未过期请及时联系管理员";break;
            case add: message="发布失败，token是否过期，未过期请及时联系管理员";break;
            case user:message="服务器出现异常错误，通知管理员！";break;
            default: message="出现未知错误！！！";
        }
        this.message = message;
    }
}
