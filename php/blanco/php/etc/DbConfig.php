<?php

class DbConfig {

    public static $DSN = "mysql:host=localhost; dbname=test; charset=utf8; unix_socket=/opt/local/var/run/mysql56/mysqld.sock";
    public static $DBUSER = "root";
    public static $DBPASSWORD = "root";
    public static $DBOPTION = array(
        PDO::ATTR_PERSISTENT => true,
        PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION
    );

    public static $DB_ETC = "etc";
    public static $DB_COMMON = "common";
    public static $DB_SAMPLE = "sample";

    public static $DB_ROW = "row";
    public static $DB_QUERY = "query";
    public static $DB_EXCEPTION = "exception";

}
