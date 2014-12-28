
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.configuration.internal.config;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.github.naios.wide.configuration.DatabaseConfig;
import com.github.naios.wide.configuration.internal.util.GsonHelper;
import com.github.naios.wide.configuration.internal.util.LateAllocate;

public class DatabaseConfigImpl implements DatabaseConfig
{
    private StringProperty id = new SimpleStringProperty(GsonHelper.EMPTY_STRING),
                name = new SimpleStringProperty(GsonHelper.EMPTY_STRING),
                    host = new SimpleStringProperty(GsonHelper.EMPTY_STRING),
                        user = new SimpleStringProperty(GsonHelper.EMPTY_STRING),
                            password = new SimpleStringProperty(GsonHelper.EMPTY_STRING),
                                schema = new SimpleStringProperty(GsonHelper.EMPTY_STRING);

    // We need to late bind the connection property to user and host
    // because user & host might be null sometimes
    private final LateAllocate<StringProperty> connection = new LateAllocate<StringProperty>()
    {
        @Override
        public StringProperty allocate()
        {
            final StringProperty property = new SimpleStringProperty();
            property.bind(Bindings.concat(user, "@", host));
            return property;
        }
    };

    @Override
    public StringProperty id()
    {
        return id;
    }

    @Override
    public StringProperty name()
    {
        return name;
    }

    @Override
    public StringProperty host()
    {
        return host;
    }

    @Override
    public StringProperty user()
    {
        return user;
    }

    @Override
    public StringProperty password()
    {
        return password;
    }

    @Override
    public StringProperty schema()
    {
        return schema;
    }

    @Override
    public StringProperty endpoint()
    {
        return connection.get();
    }
}
