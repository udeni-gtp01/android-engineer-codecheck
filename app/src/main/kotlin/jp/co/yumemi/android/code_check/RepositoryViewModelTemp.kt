/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.model.RepositoryItem
import jp.co.yumemi.android.code_check.model.ServerResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.Date

/**
 * PreviewFragment で使う
 */
class RepositoryViewModelTemp(
    val context: Context
) : ViewModel() {

    // 検索結果
    fun searchResults(inputText: String): List<RepositoryItem> = runBlocking {
        val client = HttpClient(Android)

        return@runBlocking GlobalScope.async {
            val response: HttpResponse? =
                client.get("https://api.github.com/search/repositories") {
                    header("Accept", "application/vnd.github.v3+json")
                    parameter("q", inputText)
                }
            val gson = Gson()
            val responseString = response!!.body<String>()
            val repositoryResponseList =
                gson.fromJson(responseString, ServerResponse::class.java)
            val items = repositoryResponseList
            val repositoryResponse = gson.fromJson(responseString, ServerResponse::class.java)
            lastSearchDate = Date()
            return@async repositoryResponse.items
        }.await()
    }
}