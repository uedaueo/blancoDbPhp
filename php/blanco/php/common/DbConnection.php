<?php

/**
 * Created by IntelliJ IDEA.
 * User: tueda
 * Date: 15/09/22
 * Time: 13:05
 */
class DbConnection
{
    private $connection = null;

    /**
     * オブジェクトが破棄される際に DB 接続を（必要なら）閉じます
     */
    public function __destruct() {
        $this->close();
    }

    /**
     * DB 接続を取得します．初回の場合は新たに PDO インスタンスを作成します．
     *
     * @return null|PDO
     */
    public function getConnection() {
        if ($this->connection === null) {
            $this->connection = new PDO(
                DbConfig::$DSN,
                DbConfig::$DBUSER,
                DbConfig::$DBPASSWORD,
                DbConfig::$DBOPTION
            );
        }

        return $this->connection;
    }

    /**
     * DB接続を閉じます．ただしPDOの持続的接続機能を使用している場合は何もしません．
     */
    public function close() {
        if ($this->connection !== null) {
            if (!$this->connection->getAttribute(PDO::ATTR_PERSISTENT)) {
                $this->connection = null;
            }
        }
    }
}
