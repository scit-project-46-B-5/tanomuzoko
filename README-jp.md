<h1 style="color:#802548">tanomuzoko project</h1>

[日本語](https://github.com/scit-project-46-B-5/tanomuzoko/blob/main/README-jp.md)

<br>

[한국어](https://github.com/scit-project-46-B-5/tanomuzoko/blob/main/README.md)


## 導入
- tanomuzoko(頼む ＋ 冷蔵庫) プログラム


## 内容

1. [簡単にプロジェクト紹介](#簡単にプロジェクト紹介)
2. [プロジェクトスペック](#プロジェクトスペック)
3. [サーバースタートの上で必須条件](#サーバースタートの上で必須条件)
4. [使用した技術](#使用した技術)


## 簡単にプロジェクト紹介
- 動機
    - 日本で一人暮らしをする時、毎日のレシピの悩みをAIに任せてみよう！
    - ユーザー同士で遊べるコミュニティも作ってみよう！
- 参加人員
    - ホ·ジェウォン
        - チームリーダー、 ER図、 レシピ関連機能及びコード最適化
    - キム·ミンギ
        - 全体のUI/UX構造設計及びサーバー配布
    - キム·ジェヒョン
        - 全体掲示板
    - オセ·ジュン
        - 会員登録及びログイン
    - チェ·スミン
        - 個人情報ページ及びUI/UXの細部実装担当
    - チェ·ヒョジュン
        - メインページ及びコメント／いいね機能

- 배포 서버 URL: https://tanomuzoko.shop

## 開発環境
- java JDK 17
- Spring boot 3.4.2
- gradle
- mysql 8+

## プロジェクト実行に必要な要件
- jvm オプション
    - -Djasypt.encryptor.password
- Redis 設置
    - https://github.com/microsoftarchive/redis/releases


## 使用した技術
- backend
    - JAVA
    - SPRING BOOT
    - JPA
- frontend
    - THYMELEAF
    - HTML5
    - CSS
    - JS    
    - JQUERY
    - QUILL
    - DROPZONE
- db
    - MYSQL
    - REDIS
- server
    - AWS
- version control
    - GIT
    - GITHUB
    - GITHUB ACTION
