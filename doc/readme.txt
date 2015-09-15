blancoDbPhp (以降 blancoDb) は SQL定義書から PHPソースコードを自動生成するツールです。
SQL定義書という *.xlsファイル形式の記述内容にしたがって、データベース入出力をおこなうソースコードを自動生成することができます。
blancoDbを使えば、データベース入出力の ルーチンワーク的で しかし厄介なプログラミングを人間が担当する必要はありません。Excelなどの表計算ソフトを使って、SQL定義書 に必要項目を記入するだけでよいのです。
なお、データベース入出力には PDO (PHP Data Object) を利用するようなPHPソースコードが自動生成されます。

チュートリアルや定義書記入要領などは、下記のURLで入手することができます。
●http://hp.vector.co.jp/authors/VA027994/blanco/blancodb.html

自動生成されたソースコードは、それらが独立して動作するようになっています。ランタイムライブラリなどは必要ありません。
安全で確実で高速なデータベース入出力処理が必要な方は、ぜひ blancoDbを試してみてください。
Eclipseプラグイン形式かAntタスクで実行することが出来ます。

利用のおおまかなステップは下記のようになります。
 1.Eclipseプラグインをインストールする。
 2.PHP環境に pdoに関する設定をおこなう。
 3.blancoDbプラグインを起動する。
 4.blancoDbプラグインで SQL(*.xls)ファイルを作成する。
 5.Excelなどの表計算ソフトを使って、SQL定義書を記入する。
 6.blancoDbプラグインで ソースコードの自動生成をおこなう。
 7.自動生成されたソースコードを使って データベース入出力をするプログラムを作成する。

[方針]
・クラスのほうに、データベース上の型が表示されるので、利便性が向上します。

[ポイント]
・blancoDbのインストールに際して、他のblanco Frameworkプラグインの場合とは異なり、プラグインを解凍したうえで Eclipseプラグインとして登録してください。
  これは blancoDbがソースコードを自動生成する際に、JDBCドライバが必要になるからです。
  (自動生成されたソースコードは、実行時にはJDBCドライバは不要です)
  プラグイン内の所定のフォルダにJDBCドライバjarファイルを配置してください。

[特徴]
・ごく普通のSQL文を そのまま利用できます。
  利用しているリレーショナルデータベースのSQL文を そのまま利用できます。

[開発者]
 1.伊賀敏樹 (Tosiki Iga / いがぴょん): blancoDb Enterprise Editionから blancoDbDotNetにフォークした後の開発および維持メンテ担当
 2.山本耕司 (Y-moto) : 試験およびリリース判定担当

[ライセンス]
 1.blancoDbPhp は ライセンス として GNU Lesser General Public License を採用しています。

[依存するライブラリ]
blancoDbPhpは下記のライブラリを利用しています。
※各オープンソース・プロダクトの提供者に感謝します。
 1.JExcelApi - Java Excel API - A Java API to read, write and modify Excel spreadsheets
     http://jexcelapi.sourceforge.net/
     http://sourceforge.net/projects/jexcelapi/
     http://www.andykhan.com/jexcelapi/ 
   概要: JavaからExcelブック形式を読み書きするためのライブラリです。
   ライセンス: GNU Lesser General Public License
 2.PostgreSQL JDBC Driver
   概要: ソースコード自動生成をおこなう際に、PostgreSQL接続のために利用します。
   ライセンス: BSD License
 2.blancoCg
   概要: ソースコード生成ライブラリ
   ライセンス: GNU Lesser General Public License
 3.その他の blanco Framework
   概要: このプロダクトは それ自身が blanco Frameworkにより自動生成されています。
         このプロダクトは 実行時に blanco Framework各種プロダクトに依存して動作します。
   ライセンス: GNU Lesser General Public License
