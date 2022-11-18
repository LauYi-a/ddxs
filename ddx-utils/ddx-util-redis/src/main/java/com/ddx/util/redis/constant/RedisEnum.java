package com.ddx.util.redis.constant;

/**
 * @ClassName: LockEnum
 * @Description:
 * @Author: YI.LAU
 * @Date: 2022年11月17日 17:58
 * @Version: 1.0
 */
public enum RedisEnum {

    STATUS_TRUE(RedisConstant.STATUS_TRUE,"获取锁成功"),
    STATUS_FALSE(RedisConstant.STATUS_FALSE,"获取锁失败");

    private boolean isOk;
    private String msg;

    RedisEnum(boolean isOk, String msg){
        this.isOk = isOk;
        this.msg = msg;
    }

    public boolean isOk() {
        return isOk;
    }

    public String getMsg() {
        return msg;
    }
}
