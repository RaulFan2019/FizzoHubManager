package cn.fizzo.hub.manager.network;

import com.alibaba.fastjson.JSON;

import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;

import java.lang.reflect.Type;

import cn.fizzo.hub.manager.entity.net.BaseRE;
import cn.fizzo.hub.manager.utils.LogU;


/**
 * Created by wyouflf on 15/11/5.
 */
public class BaseResponseParser implements ResponseParser {

    private static final String TAG = "BaseResponseParser";
    private static final boolean DEBUG = true;

    public static final int ERROR_CODE_NONE = 0;
    public static final int ERROR_CODE_JSON_EXCEPTION = 1;

    @Override
    public void checkResponse(UriRequest request) throws Throwable {
        // custom check ?
        // get headers ?
    }

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      字符串数据
     * @return
     * @throws Throwable
     */
    @Override
    public Object parse(Type resultType, Class<?> resultClass, String result) throws Throwable {
        BaseRE response = new BaseRE();
        LogU.v(DEBUG, TAG, "result:" + result);
        //是否不包含 “{”
        if (result.indexOf("{") == -1) {
            response.errorcode = ERROR_CODE_JSON_EXCEPTION;
            response.errormsg = "网络错误 缺少{ ";
            return response;
        }
        //若包含 “{”
        result = result.substring(result.indexOf("{"));
        try {
            response = JSON.parseObject(result, BaseRE.class);
        } catch (com.alibaba.fastjson.JSONException e) {
            response.errorcode = ERROR_CODE_JSON_EXCEPTION;
            response.errormsg = "网络错误 JSON解析错误";
        }
        return response;
    }
}
