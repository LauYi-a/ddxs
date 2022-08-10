package com.ddx.common.utils;

import java.util.UUID;


/**
 * @ClassName: UUIDUtil
 * @Description: 唯一id
 * @Author: YI.LAU
 * @Date: 2022年03月28日  0028
 * @Version: 1.0
 */
public class UUIDUtil {
    /**
     * 获取一个32位的UUID
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
