package com.ddx.util.redis.result;

import com.ddx.util.redis.constant.RedisEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @ClassName: LockResultData
 * @Description: 返回锁信息
 * @Author: YI.LAU
 * @Date: 2022年11月17日 09:46
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
public class LockResultData<T> {

    private boolean isOk;

    private String msg;

    private T data;

    private LockResultData(boolean isOk, String msg, T data) {
        this.isOk = isOk;
        this.msg = msg;
        this.data = data;
    }

    private LockResultData(boolean isOk, String msg) {
        this.isOk = isOk;
        this.msg = msg;
    }

    public static  <T> LockResultData result(RedisEnum resultEnum, T data) {
        return new LockResultData(resultEnum.isOk(),resultEnum.getMsg(),data);
    }

    public static  <T> LockResultData<T> result(RedisEnum resultEnum, String msg) {
        String enumMessage = resultEnum.getMsg().contains("%s")?resultEnum.getMsg():resultEnum.getMsg()+"：%s";
        return new LockResultData(resultEnum.isOk(),String.format(enumMessage,msg));
    }

    public static <T> LockResultData<T> result(RedisEnum resultEnum) {
        return new LockResultData(resultEnum.isOk(),resultEnum.getMsg());
    }

    public static boolean isSuccess(LockResultData resultData){
        return resultData.isOk();
    }
}
