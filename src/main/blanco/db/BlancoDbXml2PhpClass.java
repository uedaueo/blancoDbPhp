/*
 * blanco Framework
 * Copyright (C) 2004-2006 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.BlancoCgTransformer;
import blanco.cg.transformer.BlancoCgTransformerFactory;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoNameAdjuster;
import blanco.commons.util.BlancoStringUtil;
import blanco.db.common.BlancoDbXml2SqlInfo;
import blanco.db.common.IBlancoDbProgress;
import blanco.db.common.stringgroup.BlancoDbSqlInfoTypeStringGroup;
import blanco.db.common.util.BlancoDbUtil;
import blanco.db.common.valueobject.BlancoDbSetting;
import blanco.db.common.valueobject.BlancoDbSqlInfoStructure;
import blanco.db.expander.exception.DeadlockExceptionClassPhp;
import blanco.db.expander.exception.IntegrityConstraintExceptionClassPhp;
import blanco.db.expander.exception.NoRowFoundExceptionClassPhp;
import blanco.db.expander.exception.NoRowModifiedExceptionClassPhp;
import blanco.db.expander.exception.NotSingleRowExceptionClassPhp;
import blanco.db.expander.exception.TimeoutExceptionClassPhp;
import blanco.db.expander.exception.TooManyRowsFoundExceptionClassPhp;
import blanco.db.expander.exception.TooManyRowsModifiedExceptionClassPhp;
import blanco.db.expander.query.invoker.QueryInvokerClassPhp;
import blanco.db.expander.query.iterator.QueryIteratorClassPhp;
import blanco.db.util.BlancoDbMappingUtilPhp;
import blanco.dbmetadata.valueobject.BlancoDbMetaDataColumnStructure;
import blanco.valueobjectphp.BlancoValueObjectPhpXml2SourceFile;
import blanco.valueobjectphp.valueobject.BlancoValueObjectPhpFieldStructure;
import blanco.valueobjectphp.valueobject.BlancoValueObjectPhpStructure;

/**
 * 中間XMLファイルからソースコードを生成します。
 */
public abstract class BlancoDbXml2PhpClass implements IBlancoDbProgress {
    private BlancoDbSetting fDbSetting = null;

    /**
     * XMLファイルからソースコードを生成します。
     * 
     * @param connDef
     *            データベース接続情報。
     * @param blancoSqlDirectory
     *            SQL XMLファイルが格納されているディレクトリ。
     * @param rootPackage
     *            ルートとなる基準パッケージ。
     * @param runtimePackage
     *            blancoに設定するランタイムパッケージ。nullならデフォルトに出力。
     * @param statementTimeout
     *            ステートメントタイムアウト値。
     * @param blancoTargetSourceDirectory
     *            出力先ディレクトリ。
     * @throws SQLException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws ClassNotFoundException
     * @throws TransformerException
     */
    public void process(final BlancoDbSetting argDbSetting,
            final File blancoSqlDirectory) throws SQLException, SAXException,
            IOException, ParserConfigurationException, ClassNotFoundException,
            TransformerException {
        System.out.println(BlancoDbConstantsPhp.PRODUCT_NAME + " ("
                + BlancoDbConstantsPhp.VERSION + ") ソースコード生成: 開始.");

        fDbSetting = argDbSetting;

        if (BlancoStringUtil.null2Blank(fDbSetting.getRuntimePackage()).trim()
                .length() == 0) {
            fDbSetting.setRuntimePackage(null);
        }

        Connection conn = null;
        try {
            conn = BlancoDbUtil.connect(fDbSetting);
            BlancoDbUtil.getDatabaseVersionInfo(conn, fDbSetting);

            if (blancoSqlDirectory != null) {
                // 指定がある場合にのみ SQL定義書ファイル格納ディレクトリを処理します。

                // ValueObject情報を格納するディレクトリを作成します。
                new File(blancoSqlDirectory.getAbsolutePath() + "/valueobject")
                        .mkdirs();

                final File[] fileSettingXml = blancoSqlDirectory.listFiles();
                for (int index = 0; index < fileSettingXml.length; index++) {
                    if (fileSettingXml[index].getName().endsWith(".xml") == false) {
                        // ファイルの拡張子が xml であるもののみ処理します。
                        continue;
                    }
                    if (progress(index + 1, fileSettingXml.length,
                            fileSettingXml[index].getName()) == false) {
                        break;
                    }

                    // 生成はファイル毎に行います。
                    processEveryFile(conn, fileSettingXml[index], new File(
                            blancoSqlDirectory.getAbsolutePath()
                                    + "/valueobject"));
                }
            }

        } finally {
            BlancoDbUtil.close(conn);
            conn = null;
            System.out.println("ソースコード生成: 終了.");
        }
    }

    /**
     * 個別のXMLファイルを処理します。
     * 
     * @param dbInfoCollector
     * @param rootPackage
     * @param fileSqlForm
     * @param outputDirectory
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     * @throws SQLException
     * @throws ParserConfigurationException
     */
    private void processEveryFile(final Connection conn,
            final File fileSqlForm, final File outputDirectory)
            throws IOException, SAXException, TransformerException,
            SQLException, ParserConfigurationException {

        System.out.println("ファイル[" + fileSqlForm.getAbsolutePath() + "]を処理します");

        final BlancoDbXml2SqlInfo collector = new BlancoDbXml2SqlInfo();
        final List definition = collector
                .process(conn, fDbSetting, fileSqlForm);

        final String packageNameException = BlancoDbUtil
                .getRuntimePackage(fDbSetting)
                + ".exception";

        // 従来と互換性を持たせるため、/mainサブフォルダに出力します。
        final File fileBlancoMain = new File(fDbSetting.getTargetDir()
                + "/main");

        final BlancoCgObjectFactory cgFactory = BlancoCgObjectFactory
                .getInstance();

        final BlancoCgTransformer transformer = BlancoCgTransformerFactory
                .getPhpSourceTransformer();

        // exception系
        transformer.transform(adjust(new DeadlockExceptionClassPhp(cgFactory,
                packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new IntegrityConstraintExceptionClassPhp(
                cgFactory, packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new NoRowFoundExceptionClassPhp(cgFactory,
                packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new NoRowModifiedExceptionClassPhp(
                cgFactory, packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new NotSingleRowExceptionClassPhp(
                cgFactory, packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new TimeoutExceptionClassPhp(cgFactory,
                packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new TooManyRowsFoundExceptionClassPhp(
                cgFactory, packageNameException).expand()), fileBlancoMain);
        transformer.transform(adjust(new TooManyRowsModifiedExceptionClassPhp(
                cgFactory, packageNameException).expand()), fileBlancoMain);

        // util系
        // 現時点では PHP版にはutil系のクラスはありません。

        // iterator, invoker, caller
        for (int index = 0; index < definition.size(); index++) {
            final BlancoDbSqlInfoStructure sqlInfo = (BlancoDbSqlInfoStructure) definition
                    .get(index);
            switch (sqlInfo.getType()) {
            case BlancoDbSqlInfoTypeStringGroup.ITERATOR:
                createRowObjectClass(fDbSetting.getBasePackage(), sqlInfo,
                        outputDirectory);

                transformer.transform(adjust(new QueryIteratorClassPhp(
                        fDbSetting, sqlInfo, cgFactory).expand()),
                        fileBlancoMain);
                break;
            case BlancoDbSqlInfoTypeStringGroup.INVOKER:
                transformer.transform(adjust(new QueryInvokerClassPhp(
                        fDbSetting, sqlInfo, cgFactory).expand()),
                        fileBlancoMain);
                break;
            case BlancoDbSqlInfoTypeStringGroup.CALLER:
                // TODO blancoDbPhpは呼出型の自動生成をサポートしません。
                System.out.println("blancoDbPhpは呼出型の自動生成をサポートしません。");
                break;
            default:
                throw new IllegalArgumentException(
                        "想定外のエラー。不明なクエリオブジェクトが与えられました。" + sqlInfo.toString());
            }
        }
    }

    /**
     * 行オブジェクトを作成します。
     * 
     * @param className
     * @param packageName
     * @param sqlInfo
     * @param outputDirectory
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    private void createRowObjectClass(final String rootPackage,
            final BlancoDbSqlInfoStructure sqlInfo, final File outputDirectory)
            throws SAXException, IOException, TransformerException {
        final String packageName = rootPackage + ".row";
        final String className = BlancoNameAdjuster.toClassName(sqlInfo
                .getName())
                + "Row";

        final BlancoValueObjectPhpStructure voStructure = new BlancoValueObjectPhpStructure();
        // 名前
        voStructure.setName(className);
        voStructure.setPackage(packageName);
        voStructure
                .setDescription("SQL定義(" + sqlInfo.getName() + ")にもとづく行クラス。");
        voStructure.setFileDescription("'" + className + "'行を表現する行クラス。\n");

        for (int index = 0; index < sqlInfo.getResultSetColumnList().size(); index++) {
            final BlancoDbMetaDataColumnStructure columnStructure = (BlancoDbMetaDataColumnStructure) sqlInfo
                    .getResultSetColumnList().get(index);

            final BlancoValueObjectPhpFieldStructure fieldStructure = new BlancoValueObjectPhpFieldStructure();
            voStructure.getListField().add(fieldStructure);

            fieldStructure.setName(columnStructure.getName());

            // PHP版としての型を決定します。
            fieldStructure.setType(BlancoDbMappingUtilPhp
                    .getPhpType(columnStructure));

            fieldStructure.setDescription("列:[" + fieldStructure.getName()
                    + "]: DBにおける型:[" + columnStructure.getTypeName() + "]");
        }

        final BlancoValueObjectPhpXml2SourceFile xml2javaclass = new BlancoValueObjectPhpXml2SourceFile();
        xml2javaclass.setEncoding(fDbSetting.getEncoding());
        if (fDbSetting.getTargetDir() == null) {
            throw new IllegalArgumentException(
                    "BlancoDbGenerator: blanco出力先フォルダが未設定(null)です。");
        }
        xml2javaclass.process(voStructure, new File(fDbSetting.getTargetDir()));
    }

    /**
     * ソース・オブジェクトの内容を調整。
     * 
     * <UL>
     * <LI>ソースコードのエンコーディングを設定。
     * </UL>
     * 
     * @param arg
     * @return
     */
    private BlancoCgSourceFile adjust(final BlancoCgSourceFile arg) {
        if (BlancoStringUtil.null2Blank(fDbSetting.getEncoding()).length() > 0) {
            arg.setEncoding(fDbSetting.getEncoding());
        }
        return arg;
    }
}
