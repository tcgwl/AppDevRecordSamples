package com.infrastructure.net;

import com.infrastructure.activity.BaseActivity;

import org.apache.http.client.methods.HttpUriRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestManager {
	ArrayList<HttpRequest> requestList = null;

	public RequestManager(final BaseActivity activity) {
		// 异步请求列表
		requestList = new ArrayList<>();
	}

	/**
	 * 添加Request到列表
	 */
	public void addRequest(final HttpRequest request) {
		requestList.add(request);
	}

	/**
	 * 取消网络请求
	 */
	public void cancelRequest() {
		if ((requestList != null) && (requestList.size() > 0)) {
			for (final HttpRequest httpRequest : requestList) {
				HttpUriRequest request = httpRequest.getRequest();
				if (request != null) {
					try {
						request.abort();
						requestList.remove(request);
					} catch (final UnsupportedOperationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 无参数调用
	 */
	public HttpRequest createRequest(final URLData urlData,
			final RequestCallback requestCallback) {
		final HttpRequest request = new HttpRequest(urlData, null,
				requestCallback);

		addRequest(request);
		return request;
	}

	/**
	 * 有参数调用
	 */
	public HttpRequest createRequest(final URLData urlData,
			final List<RequestParameter> params,
			final RequestCallback requestCallback) {
		final HttpRequest request = new HttpRequest(urlData, params,
				requestCallback);

		addRequest(request);
		return request;
	}
}
