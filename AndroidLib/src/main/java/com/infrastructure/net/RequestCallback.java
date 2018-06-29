package com.infrastructure.net;

public interface RequestCallback {
	void onSuccess(String content);

	void onFail(String errorMessage);
}
