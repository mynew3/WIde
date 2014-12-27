
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.configuration.internal.config;

import javafx.beans.property.StringProperty;

import com.github.naios.wide.configuration.ClientStorageConfig;

public class ClientStorageConfigImpl implements ClientStorageConfig
{
    private StringProperty path, schema;

    @Override
    public StringProperty path()
    {
        return path;
    }

    @Override
    public StringProperty schema()
    {
        return schema;
    }
}