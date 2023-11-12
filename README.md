# 株式会社ゆめみ Android エンジニアコードチェック課題

## 概要

This app is an application to search GitHub repositories and view detailed information about the repositories found.

## アプリ仕様

<img src="docs/EN.mp4" width="320">

<img src="docs/JP.mp4" width="320">

### 環境

- IDE：Android Studio Giraffe | 2022.3.1
- Kotlin：1.8.10
- Java：17
- Gradle：8.1.3
- minSdk：24
- targetSdk：34

### 動作

1. 何かしらのキーワードを入力
2. GitHub API（`search/repositories`）でリポジトリを検索し、結果一覧を概要（リポジトリ名）で表示
3. 特定の結果を選択したら、該当リポジトリの詳細（リポジトリ名、オーナーアイコン、プロジェクト言語、Star 数、Watcher 数、Fork 数、Issue 数）を表示

## Project Structure

- `jp.co.yumemi.android.code_check.ui.compose` : Contains the Composable functions for different screens.
- `jp.co.yumemi.android.code_check.model` : Contains the data models used in the application.
- `jp.co.yumemi.android.code_check.view_model` : Contains the ViewModel for data management.
- `jp.co.yumemi.android.code_check.navigation` : Manages the app's navigation flow.
- `jp.co.yumemi.android.code_check.repository` : Handles repository operations and data sources.
- `jp.co.yumemi.android.code_check.service` : Contains the Service interface and implementation for communicating with the GitHub API.
- `jp.co.yumemi.android.code_check.network` : Provides networking functionality for fetching GitHub repository data and dependency injection using Hilt module.

## Localization

The app supports English and Japanese.
