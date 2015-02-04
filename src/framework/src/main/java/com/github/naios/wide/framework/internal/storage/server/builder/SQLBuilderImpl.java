
/*
 * Copyright (c) 2013 - 2015 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.framework.internal.storage.server.builder;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.naios.wide.api.config.main.QueryTypeConfig;
import com.github.naios.wide.api.framework.storage.server.SQLBuilder;
import com.github.naios.wide.api.framework.storage.server.SQLInfoProvider;
import com.github.naios.wide.api.framework.storage.server.SQLUpdateInfo;
import com.github.naios.wide.api.framework.storage.server.ServerStorageStructure;

/**
 * Implementation of an SQLBuilder based on storage holders
 */
public final class SQLBuilderImpl implements SQLBuilder
{
    private final SQLInfoProvider sqlInfoProvider;

    private final SQLMaker sqlMaker;

    private final Map<ServerStorageStructure, Collection<SQLUpdateInfo>> update;
    private final Collection<ServerStorageStructure> insert, delete;

    private final QueryTypeConfig updateConfig, insertConfig, deleteConfig;

    public SQLBuilderImpl(final SQLInfoProvider sqlInfoProvider,
            final Map<ServerStorageStructure, Collection<SQLUpdateInfo>> update,
            final Collection<ServerStorageStructure> insert,
            final Collection<ServerStorageStructure> delete,
            final QueryTypeConfig updateConfig,
            final QueryTypeConfig insertConfig,
            final QueryTypeConfig deleteConfig)
    {
        this.sqlInfoProvider = sqlInfoProvider;

        this.update = update;
        this.insert = insert;
        this.delete = delete;

        this.updateConfig = updateConfig;
        this.insertConfig = insertConfig;
        this.deleteConfig = deleteConfig;

        this.sqlMaker = new SQLMaker(this, new SQLVariableHolder());
    }

    public SQLInfoProvider getSQLInfoProvider()
    {
        return sqlInfoProvider;
    }

    public SQLMaker getSQLMaker()
    {
        return sqlMaker;
    }

    public QueryTypeConfig getUpdateConfig()
    {
        return updateConfig;
    }

    public QueryTypeConfig getInsertConfig()
    {
        return insertConfig;
    }

    public QueryTypeConfig getDeleteConfig()
    {
        return deleteConfig;
    }

    /**
     * Builds our SQL query
     */
    @Override
    public void write(final OutputStream stream)
    {
        final PrintWriter writer = new PrintWriter(stream);

        // Pre calculate everything
        final SQLVariableHolder vars = new SQLVariableHolder();
        final Map<String /*scope*/, SQLScope> scopes = SQLScope.split(this, update, insert, delete);
        final Map<String /*scope*/, String /*query*/> querys = new HashMap<>();

        for (final Entry<String, SQLScope> entry : scopes.entrySet())
            querys.put(entry.getKey(), entry.getValue().buildQuery(entry.getKey(), vars, sqlInfoProvider));

        vars.writeQuery(writer);

        for (final Entry<String, String> entry : querys.entrySet())
        {
            final String comment = sqlInfoProvider.getCommentOfScope(entry.getKey());

            if (!comment.isEmpty())
                writer.println(SQLMaker.createComment(comment));

            // The actual scope querys
            writer.println(entry.getValue());
        }

        writer.close();
    }

    /**
     * Executes our sql batch on the connection
     */
    @Override
    public boolean commit()
    {
        /*
        if (changeTracker.connection().get() == null)
            return false;

        changeTracker.connection().get().asyncExecute(toString());
        */

        return true;
    }

    @Override
    public String toString()
    {
        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        write(stream);
        return new String(stream.toByteArray()).trim();
    }
}