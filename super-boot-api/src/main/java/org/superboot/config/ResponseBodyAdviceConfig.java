package org.superboot.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.superboot.base.BaseMessage;
import org.superboot.base.BaseResponse;
import org.superboot.base.SuperBootCode;
import org.superboot.pub.Pub_Tools;

import javax.annotation.Resource;

/**
 * <b> 统一处理消息返回信息 </b>
 * <p>
 * 功能描述:为了保证返回结果的一致性，对消息进行统一处理
 * </p>
 *
 * @author jesion
 * @date 2017/9/9
 * @time 17:53
 * @Path org.superboot.config.ResponseBodyAdviceConfig
 */
@ControllerAdvice
public class ResponseBodyAdviceConfig implements ResponseBodyAdvice {

    @Resource
    Pub_Tools Pub_Tools;

    /**
     * 统一处理消息相应后的处理
     *
     * @param obj       返回结果数据
     * @param params    方法参数
     * @param mediaType
     * @param c         类
     * @param req       请求信息
     * @param resp      响应信息
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter params, MediaType mediaType, Class c, ServerHttpRequest req, ServerHttpResponse resp) {

        //如果返回结果已经处理过，则无需处理，一般只有出现异常或者请求不存在的时候才无需处理
        if (obj instanceof BaseMessage) {
            return obj;
        }
        //处理基础响应信息
        else if (obj instanceof BaseResponse) {
            BaseResponse baseResponse = ((BaseResponse) obj);
            //独立处理返回结果
            SuperBootCode superBootCode = baseResponse.getSuperBootCode();
            //获取响应数据
            Object data = baseResponse.getData();
            if (null == data) {
                data = "";
            }
            return Pub_Tools.genOkMsg(superBootCode.getCode(), data);
        }
        //只有项目上的才需要单独处理,主要用于
        else if (params.getDeclaringClass().getName().startsWith("org.superboot")) {
            //独立处理返回结果
            return Pub_Tools.genOkMsg(obj);
        } else {
            return obj;
        }


    }

    @Override
    public boolean supports(MethodParameter params, Class c) {
        return true;
    }


}
