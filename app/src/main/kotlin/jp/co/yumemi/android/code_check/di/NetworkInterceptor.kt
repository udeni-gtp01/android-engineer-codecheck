package jp.co.yumemi.android.code_check.di

import jp.co.yumemi.android.code_check.constant.Constant.HEADER_TYPE
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * An OkHttp Interceptor responsible for modifying network requests and headers before they are sent.
 * This interceptor adds a custom "Accept" header to all outgoing requests.
 */
class NetworkInterceptor : Interceptor {
    /**
     * Intercepts the network request, modifies headers, and proceeds with the request.
     *
     * @param chain The OkHttp Interceptor Chain for handling the request.
     * @return The response received after processing the modified request.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
            .header("Accept", HEADER_TYPE)
            .build()
        return chain.proceed(modifiedRequest)
    }
}