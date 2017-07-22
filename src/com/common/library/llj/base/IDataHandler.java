package com.common.library.llj.base;

/**
 * 数据操作类接口
 * Created by liulj on 16/5/21.
 */
public interface IDataHandler {

    /**
     * 判断跳内页还是外页面
     *
     * @param title 标题
     * @param link  链接
     */
    void gotoInnerPage(String title, String link);

    /**
     * 缓存信息到本地文件
     *
     * @param key    SharedPreferences的key
     * @param result 网络获取的信息
     */

    void saveJsonData(String key, BaseResponse result);

    /**
     * 从本地缓存文件中获取数据
     *
     * @param key      SharedPreferences的key
     * @param classOfT gson反射的对象
     * @return
     */
    <T> T getJsonData(String key, Class<T> classOfT);

    /**
     * 清除缓存
     */
    void cleanCache();

    String getFileSize();
}
