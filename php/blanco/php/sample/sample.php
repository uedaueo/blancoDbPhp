<?php
/**
 * Created by IntelliJ IDEA.
 * User: tueda
 * Date: 15/09/22
 * Time: 15:27
 */


include_once(__DIR__ . "/env.php");
require_once(SRC_PATH . "/etc/DbConfig.php");
require_once(SRC_PATH . "/common/ClassLoader.php");

ClassLoader::addPath(
    array(
        LIBRARY_PATH . '/' . DbConfig::$DB_ROW,
        LIBRARY_PATH . '/' . DbConfig::$DB_QUERY,
        LIBRARY_PATH . '/' . DbConfig::$DB_EXCEPTION,
        SRC_PATH . '/' . DbConfig::$DB_ETC,
        SRC_PATH . '/' . DbConfig::$DB_COMMON,
        SRC_PATH . '/' . DbConfig::$DB_SAMPLE
    )
);

spl_autoload_register(array('ClassLoader', '_autoLoad'));

try {

    $dbh = new DbConnection();

    $mysqlnd = function_exists('mysqli_fetch_all');

    if ($mysqlnd) {
        print "mysqlnd enabled!" . "\n";
    }

    $clver = $dbh->getConnection()->getAttribute(PDO::ATTR_CLIENT_VERSION);
    if (strpos($clver, 'mysqlnd') !== false) {
        print "PDO MySQLnd enabled! : " . $clver . "\n";
    }

    $ite = new SimpleBlancoSelectAllIterator($dbh->getConnection());
    $ite->prepareStatement();

    $stmt = $ite->getStatement();

    while ($ite->next()) {
        $row = $ite->getRow();
        print $row . "\n";
    }

} catch (Exception $e) {

    print $e . "\n";

}
