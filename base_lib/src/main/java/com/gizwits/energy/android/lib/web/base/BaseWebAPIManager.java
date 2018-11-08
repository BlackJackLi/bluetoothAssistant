package com.gizwits.energy.android.lib.web.base;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gizwits.energy.android.lib.utils.LogUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.xutils.common.Callback;
import org.xutils.common.task.AbsTask;
import org.xutils.common.util.KeyValue;
import org.xutils.ex.HttpException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.http.annotation.HttpResponse;
import org.xutils.http.app.RequestTracker;
import org.xutils.http.app.ResponseParser;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Black on 2016/2/21 0021.
 */
public abstract class BaseWebAPIManager {

	//method
	protected static final String METHOD_GET = "GET";
	protected static final String METHOD_DELETE = "DELETE";
	protected static final String METHOD_POST = "POST";
	protected static final String METHOD_PUT = "PUT";

	protected HashSet<String> unLogParams;

	protected final String TAG = getClass().getSimpleName();

	private int connectTimeout = 30000;//默认超时30秒

	private String webLogDir = Environment.getExternalStorageDirectory().getPath() + "/log_web";

	public String getWebLogDir() {
		return webLogDir;
	}

	public void setWebLogDir(String webLogDir) {
		this.webLogDir = webLogDir;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	protected BaseWebAPIManager() {
		unLogParams = new HashSet<>();
	}

	protected void addUnLogParams(String params) {
		unLogParams.add(params);
	}

	protected void removeUnLogParams(String params) {
		unLogParams.remove(params);
	}

	/**
	 * base method for web
	 */

	/*************************************************************网络请求框架基础方法***************************************************************************/

	/**
	 * @param method
	 * @param url
	 * @param headers
	 * @param params
	 * @param webResponseHandler
	 * @param webResponseParser
	 * @param <T>
	 */

	final protected <T> WebRequestCancelHandler request(@NonNull final String method, @NonNull final String url, final Map<String, String> headers, final
	Map<String, Object> params, @NonNull final WebResponseHandler<T> webResponseHandler, final WebResponseParser<T> webResponseParser) {
		return request(method, url, headers, params, null, webResponseHandler, webResponseParser);
	}

	/**
	 * @param method
	 * @param url
	 * @param headers
	 * @param params
	 * @param filePaths
	 * @param webResponseHandler
	 * @param webResponseParser
	 * @param <T>
	 */
	final protected <T> WebRequestCancelHandler request(@NonNull final String method, @NonNull final String url, final Map<String, String> headers, final
	Map<String, Object> params, final Map<String, List<String>> filePaths, @NonNull final WebResponseHandler<T> webResponseHandler, final WebResponseParser<T>
			webResponseParser) {
		LogUtils.v(TAG, "request Url : " + url);
		webResponseHandler.onStarted();
		LogUtils.v(TAG, "create RequestParams");

		RequestParams xParams = new RequestParams(url);
		HttpMethod httpMethod;

		LogUtils.v(TAG, "set httpMethod");
		switch (method) {
			case METHOD_GET: {
				httpMethod = HttpMethod.GET;
				break;
			}
			case METHOD_POST: {
				httpMethod = HttpMethod.POST;
				break;
			}
			case METHOD_DELETE: {
				httpMethod = HttpMethod.DELETE;
				break;
			}
			case METHOD_PUT: {
				httpMethod = HttpMethod.PUT;
				break;
			}
			default:
				throw new Error("undefine http method : " + method);
		}

		xParams.setMethod(httpMethod);
		LogUtils.v(TAG, "parser headers");
		if (headers != null) {
			Set<Map.Entry<String, String>> headersEntry = headers.entrySet();
			for (Map.Entry<String, String> header : headersEntry) {
				if (!TextUtils.isEmpty(header.getKey()) && !TextUtils.isEmpty(header.getValue())) {
					xParams.addHeader(header.getKey(), header.getValue());
				}
			}
		}

		LogUtils.v(TAG, "parser params");
		if (params != null) {
			Set<Map.Entry<String, Object>> paramsEntry = params.entrySet();
			for (Map.Entry<String, Object> param : paramsEntry) {
				if (!TextUtils.isEmpty(param.getKey()) && param.getValue() != null) {
					xParams.addParameter(param.getKey(), param.getValue());
				}
			}
			if (HttpMethod.permitsRequestBody(httpMethod)) {
				xParams.setAsJsonContent(true);
//				String JsonParams = xParams.toJSONString();
//				xParams.setBodyContent(JsonParams);
			}
		}
		if (filePaths != null) {
			xParams.setMultipart(true);
			xParams.setAsJsonContent(false);
			Set<Map.Entry<String, List<String>>> filePathsEntry = filePaths.entrySet();
			for (Map.Entry<String, List<String>> entry : filePathsEntry) {
				if (!TextUtils.isEmpty(entry.getKey()) && entry.getValue() != null) {
					for (String filePath : entry.getValue()) {
						if (!TextUtils.isEmpty(filePath)) {
							File file = new File(filePath);
							xParams.addBodyParameter(entry.getKey(), file);
						}
					}
				}
			}
		}

		final WebRequestCancelHandler cancelHandler = new WebRequestCancelHandler();
		LogUtils.v(TAG, "execute request");
		Callback.Cancelable cancelable = xRequest(httpMethod, xParams, new xResponseCallback() {
			@Override
			public void onSuccess(final xResponse response) {
				WebResponse<T> webResponse = null;
				String result = response.getResult();
				if (!TextUtils.isEmpty(response.getResult())) {
					webResponse = parserResponse(result);
					if (webResponseParser != null && webResponse != null && webResponse.isHaveResultObject()) {
						try {
							T t = webResponseParser.parse(webResponse);
							webResponse.setResultObj(t);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				if (webResponse != null && BaseWebAPI.BaseErrorCode.CODE_SUCCESS.equals(webResponse.getCode())) {
					webResponseHandler.onSuccess(webResponse);
				} else {
					if (webResponse == null) {
						webResponse = new WebResponse<>();
						webResponse.setCode(BaseWebAPI.BaseErrorCode.CODE_RESULT_INVALID);
					}
					webResponseHandler.onFailure(webResponse);
				}
			}

			@Override
			public void onError(Throwable throwable, boolean isOnCallback) {
				webResponseHandler.onError(throwable);
			}

			@Override
			public void onCancelled(CancelledException e) {
				webResponseHandler.onCancelled(e);
			}

			@Override
			public void onFinished() {
				webResponseHandler.onFinished();
				cancelHandler.setFinished(true);
			}
		});

		cancelHandler.setCancelable(cancelable);
		return cancelHandler;
	}

	/**
	 * parser response
	 * <p/>
	 * 拆分code、message、result
	 */
	private <T> WebResponse<T> parserResponse(@NonNull String json) {
		try {
			WebResponse<T> webResponse = new WebResponse<>();
			JsonObject jsonObj = new JsonParser().parse(json).getAsJsonObject();
			// 业务服务器解析
			String code = jsonObj.get(BaseWebAPI.Response.CODE).getAsString();
			String message = jsonObj.get(BaseWebAPI.Response.MESSAGE).getAsString();
			//json元素
			JsonElement je = jsonObj.get(BaseWebAPI.Response.RESULT);

			webResponse.setCode(code);
			webResponse.setMessage(message);
			if (je != null) {
				webResponse.setResult(je.toString());
				webResponse.setHaveResultObject(true);
			} else {//result如果根本不存在对象
				webResponse.setResult(json);
				webResponse.setHaveResultObject(false);
			}
			return webResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ******************************************************xUtils 网络模块相关方法和类**************************************************************************
	 */

	/**
	 * 调用xUtils的http请求模块
	 *
	 * @param httpMethod
	 * @param requestParams
	 * @param callback
	 */
	private Callback.Cancelable xRequest(@NonNull HttpMethod httpMethod, @NonNull RequestParams requestParams, @NonNull Callback.CommonCallback callback) {
		requestParams.setRequestTracker(new xRequestTracker());
		requestParams.setConnectTimeout(connectTimeout);//超时时间默认30秒
//		requestParams.setUseCookie(false);
		return x.http().request(httpMethod, requestParams, callback);
	}

	/**
	 * *******************************************************xUtils 相关网络返回数据接收解释用的类****************************************************************
	 */

	/**
	 * 接收到httpUtils的返回的数据和事件所回调方法的类,为了方便所以才对ProgressCallback接口各个方法做了空实现
	 */
	private static class xResponseCallback implements Callback.ProgressCallback<xResponse> {
		@Override
		public void onWaiting() {

		}

		@Override
		public void onStarted() {

		}

		@Override
		public void onLoading(long l, long l1, boolean b) {

		}

		@Override
		public void onSuccess(xResponse s) {

		}

		@Override
		public void onError(Throwable throwable, boolean b) {

		}

		@Override
		public void onCancelled(CancelledException e) {

		}

		@Override
		public void onFinished() {

		}
	}

	/**
	 * 解释httpUtils所返回的数据的解释类
	 */
	public final static class xResponseParser implements ResponseParser {

		private int responseCode = -1;
		private String responseMessage = "";

		@Override
		public void checkResponse(UriRequest uriRequest) throws Throwable {
			responseCode = uriRequest.getResponseCode();
			responseMessage = uriRequest.getResponseMessage();
		}

		@Override
		public Object parse(Type type, Class<?> aClass, String s) throws Throwable {

			return new xResponse(responseCode, responseMessage, s);
		}
	}

	/**
	 * 承载httpUtils返回的数据解释后的容器类,使用注解的方式指定解释器
	 */
	@HttpResponse(parser = xResponseParser.class)
	private final static class xResponse {

		public xResponse() {
		}

		public xResponse(int responseCode, String responseMessage, String result) {
			this.responseCode = String.valueOf(responseCode);
			this.responseMessage = responseMessage;
			this.result = result;
		}

		private String responseCode = "";
		private String responseMessage = "";
		private String result = "";

		public String getResponseMessage() {
			return responseMessage;
		}

		public void setResponseMessage(String responseMessage) {
			this.responseMessage = responseMessage;
		}

		public String getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(String responseCode) {
			this.responseCode = responseCode;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}
	}

	/**
	 * 网络调用跟踪器
	 */
	private final class xRequestTracker implements RequestTracker {

		@Override
		public void onWaiting(RequestParams requestParams) {
			LogUtils.v(TAG, "onWaiting");
		}

		@Override
		public void onStart(RequestParams requestParams) {
			LogUtils.v(TAG, "onStarted");
			LogUtils.v(TAG, decodeRequestParams(requestParams));
		}

		@Override
		public void onRequestCreated(UriRequest uriRequest) {
			LogUtils.v(TAG, "onRequestCreated");
		}

		@Override
		public void onCache(UriRequest uriRequest, Object result) {
			LogUtils.v(TAG, "onCache");
		}

		@Override
		public void onSuccess(final UriRequest uriRequest, final Object result) {
			LogUtils.v(TAG, "onSuccess");
			if (result instanceof xResponse) {
				xResponse response = (xResponse) result;
				WebResponse webResponse = null;
				if (!TextUtils.isEmpty(response.getResult())) {
					LogUtils.i(TAG, "Content: " + response.getResult());
					webResponse = parserResponse(response.getResult());
					if (webResponse != null) {
						LogUtils.i(TAG, "result: " + webResponse.getResult());
					}
				}
				if (webResponse == null || !BaseWebAPI.BaseErrorCode.CODE_SUCCESS.equals(webResponse.getCode())) {
					//服务器错误
					log(response, uriRequest, null, null);
				}
			} else {
				LogUtils.i(TAG, "Content: " + result.toString());
			}
		}

		@Override
		public void onCancelled(UriRequest uriRequest) {
			LogUtils.v(TAG, "onCancelled");
		}

		@Override
		public void onError(UriRequest uriRequest, Throwable throwable, boolean b) {
			LogUtils.v(TAG, "onError");
			if (throwable instanceof HttpException) { // 网络错误
				HttpException httpEx = (HttpException) throwable;
				log(null, uriRequest, httpEx, null);
			} else { // 其他错误
				log(null, uriRequest, null, throwable);
			}
		}

		@Override
		public void onFinished(UriRequest uriRequest) {
			LogUtils.v(TAG, "onFinished");
		}
	}
	/**************************************************************网络错误日志相关方法**************************************************************************/

	/**
	 * 打印网络错误日志
	 *
	 * @param response
	 * @param uriRequest
	 * @param httpException
	 * @param throwable
	 * @return
	 */
	private String log(xResponse response, UriRequest uriRequest, HttpException httpException, Throwable throwable) {
		StringBuilder strb = new StringBuilder();

		// 错误类别
		if (response != null) {
			strb.append("【Service Error】\n");
		} else if (httpException != null) {
			strb.append("【Network Error】\n");
		} else if (throwable != null) {
			strb.append("【Unknown Error】\n");
		}

		if (response != null) {
			strb.append("Code: ").append(response.getResponseCode()).append("\n");
			strb.append("Message: ").append(response.getResponseMessage()).append("\n");
			strb.append("Content: ").append(response.getResult()).append("\n");
		} else if (uriRequest != null) {
			//尝试从uriRequest获取返回的代码
			try {
				strb.append("Code: ").append(uriRequest.getResponseCode()).append("\n");
				strb.append("Message: ").append(uriRequest.getResponseMessage()).append("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (httpException != null) {
			Throwable t = httpException.getCause() == null ? httpException : httpException.getCause();
			strb.append("Error By: ").append(t.getClass().getName()).append(":").append(t.getMessage()).append("\n");
		}
		if (throwable != null) {
			Throwable t = throwable.getCause() == null ? throwable : throwable.getCause();
			strb.append("Error By: ").append(t.getClass().getName()).append(":").append(t.getMessage()).append("\n");
		}

		//解码请求参数
		if (uriRequest != null) {
			strb.append(decodeRequestParams(uriRequest.getParams()));
		}
		//错误详情
		if (httpException != null) {
			strb.append("Error Detail: ");
			Throwable t = httpException.getCause() == null ? httpException : httpException.getCause();
			for (StackTraceElement ste : t.getStackTrace()) {
				strb.append(ste).append("\n");
			}
		} else if (throwable != null) {
			strb.append("Error Detail: ");
			Throwable t = throwable.getCause() == null ? throwable : throwable.getCause();
			for (StackTraceElement ste : t.getStackTrace()) {
				strb.append(ste).append("\n");
			}
		}

		final String logMessage = strb.toString();

		LogUtils.e(TAG, logMessage);

		// 异步写到文件中
		x.task().start(new AbsTask<Object>() {
			@Override
			protected Object doBackground() throws Throwable {
				//todo 写到文件中
				LogUtils.log2File(webLogDir, logMessage);
				return null;
			}

			@Override
			protected void onSuccess(Object o) {
				//do not thing
			}

			@Override
			protected void onError(Throwable throwable, boolean b) {
				//do not thing
			}
		});
		return logMessage;
	}

	private String decodeRequestParams(@NonNull RequestParams params) {
		StringBuilder strb = new StringBuilder();
		// 请求方式
		strb.append("Method: ").append(params.getMethod().name()).append("\n");
		strb.append("URL: ").append(encodeUrl(params)).append("\n");

		// headers
		List<RequestParams.Header> headers = params.getHeaders();
		if (headers != null) {
			for (RequestParams.Header header : headers) {
				strb.append("Header: ").append(header.key).append(" = ");
				strb.append(header.getValueStr()).append("\n");
			}
		}

		// parser params
		strb.append(parserParams(params));

		return strb.toString();
	}

	private String encodeUrl(@NonNull RequestParams params) {
		StringBuilder paramsStrb = new StringBuilder();
		paramsStrb.append(params.getUri()).append("?");
		if (!HttpMethod.permitsRequestBody(params.getMethod())) {
			List<KeyValue> queryStringParams = params.getQueryStringParams();
			for (KeyValue keyValue : queryStringParams) {
				try {
					String valueStr = URLEncoder.encode(keyValue.getValueStr(), "UTF-8");
					paramsStrb.append(keyValue.key).append("=").append(valueStr).append("&");
				} catch (UnsupportedEncodingException use) {
					use.printStackTrace();
				}
			}
			List<KeyValue> bodyParams = params.getBodyParams();
			for (KeyValue keyValue : bodyParams) {
				try {
					String valueStr = URLEncoder.encode(keyValue.getValueStr(), "UTF-8");
					paramsStrb.append(keyValue.key).append("=").append(valueStr).append("&");
				} catch (UnsupportedEncodingException use) {
					use.printStackTrace();
				}
			}
		}
		//删除最后一个&或者?字符(多余的)
		paramsStrb.deleteCharAt(paramsStrb.length() - 1);
		return paramsStrb.toString();
	}

	private String parserParams(@NonNull RequestParams params) {
		StringBuilder strb = new StringBuilder();
		if (HttpMethod.permitsRequestBody(params.getMethod())) {
			String json = params.getBodyContent();
			if (!params.isMultipart() && params.isAsJsonContent() && !TextUtils.isEmpty(json)) {//如果是json格式的内容,需要对其进行解释
				JsonParser parser = new JsonParser();
				try {
					Object ob = parser.parse(json);
					if (ob instanceof JsonObject) {
						JsonObject jsonObject = (JsonObject) ob;
						Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
						for (Map.Entry entry : entrySet) {
							strb.append("Params: ").append(entry.getKey()).append(" = ");
							if (unLogParams.contains(entry.getKey().toString())) {
								strb.append("******\n");
							} else {
								strb.append(entry.getValue().toString()).append("\n");
							}
						}
					}
				} catch (JsonParseException jpe) {
					jpe.printStackTrace();
				}
			} else {//一般格式的bodyParams
				List<KeyValue> bodyParams = params.getBodyParams();
				for (KeyValue keyValue : bodyParams) {
					strb.append("Params: ").append(keyValue.key).append(" = ");
					if (unLogParams.contains(keyValue.key)) {
						strb.append("******\n");
					} else {
						strb.append(keyValue.getValueStr()).append("\n");
					}
				}
			}
		}

		//如果有文件参数的话(表单上传),也把文件路径写入日志
		List<KeyValue> fileParams = params.getFileParams();
		for (KeyValue keyValue : fileParams) {
			strb.append("Params: ").append(keyValue.key).append(" = ");
			if (unLogParams.contains((keyValue.key))) {
				strb.append("******\n");
			} else {
				strb.append(keyValue.getValueStr()).append("\n");
			}
		}

		return strb.toString();
	}

}
