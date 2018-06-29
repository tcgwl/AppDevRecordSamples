package com.youngheart.engine;

import com.alibaba.fastjson.JSON;
import com.infrastructure.activity.BaseActivity;
import com.infrastructure.net.DefaultThreadPool;
import com.infrastructure.net.HttpRequest;
import com.infrastructure.net.RequestCallback;
import com.infrastructure.net.RequestParameter;
import com.infrastructure.net.Response;
import com.infrastructure.net.URLData;
import com.infrastructure.net.UrlConfigManager;
import com.youngheart.mockdata.MockService;

import java.util.List;

public class RemoteService {
	private static RemoteService service = null;

	private RemoteService() {

	}

	public static synchronized RemoteService getInstance() {
		if (RemoteService.service == null) {
			RemoteService.service = new RemoteService();
		}
		return RemoteService.service;
	}

	public void invoke(final BaseActivity activity,
					   final String apiKey,
					   final List<RequestParameter> params,
					   final RequestCallback callBack) {
		invoke(activity, apiKey, params, callBack, false);
	}

	public void invoke(final BaseActivity activity, 
			final String apiKey,
			final List<RequestParameter> params, 
			final RequestCallback callBack,
			final boolean forceUpdate) {
		final URLData urlData = UrlConfigManager.findURL(activity, apiKey);
		if (forceUpdate) {
			// 如果强制更新，那么就把过期时间强制设置为 0
			urlData.setExpires(0);
		}

		if (urlData.getMockClass() != null) {//使用假数据
			try {
				MockService mockService = (MockService) Class.forName(
						urlData.getMockClass()).newInstance();
				String strResponse = mockService.getJsonData();

				final Response responseInJson = JSON.parseObject(strResponse,
						Response.class);
				if (callBack != null) {
					if (responseInJson.hasError()) {
						callBack.onFail(responseInJson.getErrorMessage());
					} else {
						callBack.onSuccess(responseInJson.getResult());
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			HttpRequest request = activity.getRequestManager().createRequest(
					urlData, params, callBack);
			DefaultThreadPool.getInstance().execute(request);
		}
	}
}