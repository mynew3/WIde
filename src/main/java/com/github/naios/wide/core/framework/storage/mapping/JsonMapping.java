
/*
 * Copyright (c) 2013 - 2014 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.core.framework.storage.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.naios.wide.core.framework.util.Pair;

public class JsonMapping<FROM, TO extends Mapping<BASE>, BASE> implements Mapping<BASE>
{
    private final MapperBase<FROM, TO, BASE> mapper;

    private final MappingPlan plan;

    private final List<Pair<BASE, MappingMetadata>> values;

    private final List<Pair<BASE, MappingMetadata>> keys;

    public JsonMapping(final MapperBase<FROM, TO, BASE> mapper, final MappingPlan plan,
            final List<Pair<BASE, MappingMetadata>> values)
    {
        this.mapper = mapper;

        this.plan = plan;

        this.values = Collections.unmodifiableList(values);

        final List<Pair<BASE, MappingMetadata>> keys = new ArrayList<>();
        values.forEach((entry) ->
        {
            if (entry.second().isKey())
                keys.add(entry);
        });

        this.keys = Collections.unmodifiableList(keys);
    }

    @Override
    public Iterator<Pair<BASE, MappingMetadata>> iterator()
    {
        return values.iterator();
    }

    @Override
    public List<Pair<BASE, MappingMetadata>> getKeys()
    {
        return keys;
    }

    @Override
    public List<Pair<BASE, MappingMetadata>> getValues()
    {
        return values;
    }

    @Override
    public boolean setDefaultValues()
    {
        boolean success = true;
        for (int i  = 0; i < values.size(); ++i)
        {
            final MappingAdapter<FROM, BASE> adapter = mapper.getAdapterOf(plan.getMappedType().get(i));
            if (!adapter.isPossibleKey())
                if (!adapter.setDefault(values.get(i).first()))
                    success = false;
        }

        return success;
    }

    @Override
    public Pair<BASE, MappingMetadata> getEntryByName(final String name)
    {
        try
        {
            return values.get(plan.getOrdinalOfName(name));
        } catch (final OrdinalNotFoundException e)
        {
            throw new UnknownMappingEntryException(name);
        }
    }
}