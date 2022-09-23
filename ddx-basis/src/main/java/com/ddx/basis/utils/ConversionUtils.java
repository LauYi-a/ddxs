package com.ddx.basis.utils;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.Iterator;
import java.util.List;

/**
 * @ClassName: TypeConversion
 * @Description: 类型转换工具类
 * @Author: YI.LAU
 * @Date: 2022年09月22日 09:02
 * @Version: 1.0
 */
public class ConversionUtils<T,F> {

    private final Class<F> fClazz;

    /**
     * 构造方法，传入目标List的Class对象
     *
     * @param fClazz
     */
    public ConversionUtils(Class<F> fClazz) {
        this.fClazz = fClazz;
    }

    /**
     * 将List<T>转换为List<F>
     * @param fromList
     * @return
     */
    public List<F> toConversionListType(List<T> fromList) {
        if (CollectionUtils.isNotEmpty(fromList)) {
            List<F> resultList = Lists.newArrayList();
            try {
                Iterator var3 = fromList.iterator();
                while (var3.hasNext()) {
                    T t = (T) var3.next();
                    F f = this.fClazz.newInstance();
                    BeanUtils.copyProperties(t, f);
                    resultList.add(f);
                }
                return resultList;
            } catch (Exception var6) {
                var6.printStackTrace();
                return Lists.newArrayList();
            }
        }
        return Lists.newArrayList();
    }
}
