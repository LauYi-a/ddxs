package com.ddx.util.es.result;

import com.ddx.util.es.annotation.EsEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ResultData
 * @Description: 响应数据体
 * @Author: YI.LAU
 * @Date: 2022年03月18日  0018
 * @Version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
public class EsResultData<T>{

    private boolean isOk;

    private String msg;

    private T data;

    private EsResultData(boolean isOk, String msg, T data) {
        this.isOk = isOk;
        this.msg = msg;
        this.data = data;
    }

    private EsResultData(boolean isOk, String msg) {
        this.isOk = isOk;
        this.msg = msg;
    }


    public static  <T> EsResultData<T> result(EsEnum.EsResult resultEnum, T data) {
        return new EsResultData(resultEnum.isOk(),resultEnum.getMsg(),data);
    }

    public static  <T> EsResultData<T> result(EsEnum.EsResult resultEnum, String msg) {
        String enumMessage = resultEnum.getMsg().contains("%s")?resultEnum.getMsg():resultEnum.getMsg()+"：%s";
        return new EsResultData(resultEnum.isOk(),String.format(enumMessage,msg));
    }

    public static <T> EsResultData<T> result(EsEnum.EsResult resultEnum) {
        return new EsResultData(resultEnum.isOk(),resultEnum.getMsg());
    }

    public static boolean isSuccess(EsResultData resultData){
        return resultData.isOk();
    }
}
