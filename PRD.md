# プロダクト要件定義書（PRD）

## プロダクト名
礼拝ポップアップリマインダー（仮称）

## 背景・目的
多くのムスリムにとって礼拝は義務であり、日常生活において欠かさず行うべき重要な行為である。しかし、特に仕事中や就寝前などのタイミングでは、スマートフォンや日常的な習慣によって礼拝を忘れたり、先延ばしにしてしまうことがある。

本アプリは、**礼拝時間が過ぎた後にユーザーへリマインドを表示し、ユーザーが礼拝を行ったかどうかを明確に自問させることで、サボり癖を防止し、礼拝習慣を支援する**ことを目的とする。

## ターゲットユーザー
- Androidユーザーのムスリム
- 礼拝の時間を意識しつつも、日常生活の中でつい後回しにしてしまう人
- 礼拝を「やりたいが、スマホや生活リズムに流されがち」な人

## コア機能

### 1. 礼拝時間の取得（Aladhan API）
- 毎日定時にAPIを叩き、当日の礼拝時間（Fajr, Dhuhr, Asr, Maghrib, Isha）を取得
- 緯度・経度は端末の現在位置または手動設定
- 時刻データはSharedPreferencesなどにキャッシュして使用

### 2. ポップアップリマインダー
- 各礼拝時間を過ぎた後に、ポップアップで「礼拝しましたか？」というダイアログを表示
- ユーザーが「Yes」か「No」を選択
- Yesを選んだ場合：その礼拝について以後通知を行わない
- Noを選んだ場合：30分後に再リマインド（スヌーズ）

### 3. 画面ONトリガーでの補助リマインド
- スマホの画面がONになるたびに、該当礼拝が未完了であればポップアップを表示

### 4. ログ保存（任意）
- ユーザーのYes/No選択履歴をローカルに保存
- 後から礼拝実行状況を簡易的に振り返ることができる

## 非機能要件
- オフライン時も前回取得した礼拝時間で通知可能
- 通知はバッテリー・パフォーマンスに配慮して実装
- UIはシンプルで、通知とポップアップに特化したデザイン

## 将来的な拡張
- スヌーズ時間のカスタマイズ
- 礼拝毎のスキップ／自動OK条件の追加
- GoogleカレンダーやToDoアプリとの連携
- iOS対応（Flutterなどで再実装）

## 開発環境
- 言語：Kotlin（Androidネイティブ）
- IDE：Android Studio
- 外部API：Aladhan Prayer Times API
- 状態管理：SharedPreferences

## マイルストーン（初期バージョン）
1. 礼拝時間取得API連携（Aladhan）
2. ポップアップ通知の実装（Yes/No）
3. スヌーズ再通知の実装
4. 画面ON時ポップアップ表示
5. ローカルデータ保存によるステータス管理
6. 最小UI構築
7. 実機での手動インストールとデバッグ

## 成功指標
- 毎日の礼拝実施率がユーザー自身の体感で改善されること
- ユーザーが「サボりにくくなった」と感じること
- 一週間以上継続してアプリを使ったユーザーが一定数以上いること（例：初期30人中10人）
