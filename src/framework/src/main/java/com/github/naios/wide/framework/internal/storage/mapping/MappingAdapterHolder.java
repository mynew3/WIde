
/*
 * Copyright (c) 2013 - 2015 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.framework.internal.storage.mapping;

import java.util.HashMap;
import java.util.Map;

import com.github.naios.wide.api.framework.storage.mapping.Mapping;
import com.google.common.reflect.TypeToken;

@SuppressWarnings("serial")
class MissingMappingAdapterException extends RuntimeException
{
   public MissingMappingAdapterException(final String name)
   {
       super(String.format("Missing MappingAdapter for class %s, did you forgot to register it?", name));
   }
}

public class MappingAdapterHolder<FROM, TO extends Mapping<BASE>, BASE>
{
    private final Map<TypeToken<? extends BASE>,  MappingAdapter<FROM, TO, BASE, ? extends BASE>> adapter =
            new HashMap<>();

    public MappingAdapterHolder<FROM, TO, BASE> registerAdapter(MappingAdapter<FROM, TO, BASE, ? extends BASE> adapter)
    {
        this.adapter.put(adapter.getType(), adapter);
        return this;
    }

    protected MappingAdapter<FROM, TO, BASE, ? extends BASE> getAdapterOf(TypeToken<? extends BASE> type)
    {
        final MappingAdapter<FROM, TO, BASE, ? extends BASE> adapter = this.adapter.get(type);
        if (adapter != null)
            return adapter;
        else
            throw new MissingMappingAdapterException(type.toString());
    }
}
