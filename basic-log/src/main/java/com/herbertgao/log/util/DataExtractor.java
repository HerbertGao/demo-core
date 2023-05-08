package com.herbertgao.log.util;

import com.herbertgao.common.enums.LogTypeEnums;
import com.herbertgao.log.bean.LogData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DataExtractor {

    private static final String AND_REG = "&";
    private static final String EQUALS_REG = "=";
    private static final Map<Class<?>, Marshaller> marshallerMap = new HashMap<>();


    /**
     * 获取HttpServletRequest对象
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }

        if (ObjectUtils.isNotEmpty(RpcContext.getServiceContext().getRequest())) {
            return RpcContext.getServiceContext().getRequest(HttpServletRequest.class);
        }
        return null;
    }

    /**
     * 获取HttpServletResponse对象
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getResponse();
        }

        if (ObjectUtils.isNotEmpty(RpcContext.getServiceContext().getResponse())) {
            return RpcContext.getServiceContext().getResponse(HttpServletResponse.class);
        }
        return null;
    }

    /**
     * 获取请求参数内容
     *
     * @param parameterNames 参数名称列表
     * @param args           参数列表
     * @return Gets the request parameter content
     */
    public static Object getArgs(String[] parameterNames, Object[] args) {
        Object target;
        if (args.length == 1) {
            target = args[0];
        } else {
            target = args;
        }
        if (target == null) {
            return null;
        }
        HttpServletRequest request = getRequest();
        if (request != null && request.getContentType() != null) {
            String contentType = request.getContentType();
            if (MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
                return xmlArgs(target);
            }
            if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
                return target;
            }
        }
        return appletArgs(parameterNames, args);
    }


    /**
     * 获取程序执行结果内容
     *
     * @param resp 响应对象
     * @return Gets the contents of the program execution results
     */
    public static Object getResult(Object resp) {
        if (resp == null) {
            return null;
        }
        HttpServletResponse response = getResponse();
        if (response != null && MediaType.APPLICATION_XML_VALUE.equals(response.getContentType())) {
            return xmlArgs(resp);
        } else {
            return resp;
        }
    }

    /**
     * 获取程序参数
     *
     * @param parameterNames 参数名
     * @param args           参数值
     * @return Get program parameters
     */
    public static Object appletArgs(String[] parameterNames, Object[] args) {
        if (parameterNames == null || parameterNames.length == 0 || args == null || args.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterValue = "";
            if (args[i] != null) {
                parameterValue = args[i].toString();
            }
            sb.append(parameterNames[i]).append(EQUALS_REG).append(parameterValue).append(AND_REG);
        }
        if (sb.lastIndexOf(AND_REG) != -1) {
            sb.deleteCharAt(sb.lastIndexOf(AND_REG));
        }
        return sb.toString();
    }


    /**
     * 解析XML 数据
     *
     * @param pointArgs 切点参数
     * @return Parsing XML data
     */
    public static Object xmlArgs(Object pointArgs) {
        try (StringWriter writer = new StringWriter()) {
            Marshaller marshaller = getMarshaller(pointArgs.getClass());
            if (marshaller != null) {
                marshaller.marshal(pointArgs, writer);
                return writer.toString().replace("standalone=\"yes\"", "");
            }
        } catch (JAXBException e) {
            log.warn("parse xml data exception", e.getLinkedException());
        } catch (IOException e) {
            log.warn("writer close exception", e);
        }
        return pointArgs;
    }

    private static Marshaller getMarshaller(Class<?> clz) throws JAXBException {
        if (marshallerMap.containsKey(clz)) {
            return marshallerMap.get(clz);
        } else {
            Marshaller marshaller = JAXBContext.newInstance(clz).createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshallerMap.put(clz, marshaller);
            return marshaller;
        }
    }

    /**
     * 抽取 HttpServletRequest 对象信息
     *
     * @param data    logData Object
     * @param headers headers
     */
    public static void logHttpRequest(LogData data, String[] headers) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            data.setType(LogTypeEnums.HTTP.getType());
            data.setHost(request.getLocalAddr());
            data.setPort(request.getLocalPort());
            data.setClientIp(request.getRemoteAddr());
            data.setReqUrl(request.getRequestURL().toString());
            data.setHttpMethod(request.getMethod());
            Map<String, String> headersMap = new HashMap<>(8);
            for (String header : headers) {
                if (header != null && header.trim().length() > 0) {
                    headersMap.put(header, request.getHeader(header));
                }
            }
            data.setHeaders(headersMap);
        } else {
            data.setType(LogTypeEnums.DUBBO.getType());
        }
    }

}
