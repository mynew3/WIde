package com.github.naios.wide.core.framework.storage.server;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ObservableValue;

import com.github.naios.wide.core.framework.storage.StorageStructure;
import com.github.naios.wide.core.framework.util.ClassUtil;
import com.github.naios.wide.core.framework.util.Pair;

public abstract class ServerStorageStructure extends StorageStructure implements Iterable<Pair<ObservableValue<?>, Field>>
{
    final private ServerStorage<?> owner;

    private ServerStorageStructureState state = ServerStorageStructureState.STATE_IN_SYNC;

    public ServerStorageStructure(final ServerStorage<?> owner)
    {
        this.owner = owner;
    }

    public ObservableValue<?> getObservableValue(final Field field)
    {
        if (!field.isAccessible())
            field.setAccessible(true);

        try
        {
            return (ObservableValue<?>) field.get(this);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Class<? extends Annotation> getSpecificAnnotation()
    {
        return ServerStorageEntry.class;
    }

    public List<Field> getPrimaryFields()
    {
        return getPrimaryFields(getClass());
    }

    public static List<Field> getPrimaryFields(final Class<? extends ServerStorageStructure> type)
    {
        final List<Field> list = new LinkedList<>();

        final Field[] fields = ClassUtil.getAnnotatedDeclaredFields(type,
                ServerStorageEntry.class, true);

        for (final Field field : fields)
            if (field.getAnnotation(ServerStorageEntry.class).key())
                list.add(field);

        return list;
    }

    public List<Object> getPrimaryKeys()
    {
        final List<Object> list = new LinkedList<>();

        for (final Field field : getPrimaryFields())
            {
                if (!field.isAccessible())
                    field.setAccessible(true);

                try
                {
                    list.add(field.get(this));
                }
                catch (final Exception e)
                {
                }
            }

        return list;
    }

    public static String getNameOfField(final Field field)
    {
        final ServerStorageEntry annotation = field.getAnnotation(ServerStorageEntry.class);
        if (!annotation.name().equals(""))
            return annotation.name();
        else
            return field.getName();
    }

    public <T extends ServerStorageStructure> ServerStorageKey<T> getKey()
    {
        // TODO this seems to be very expensive
        return new ServerStorageKey<T>(getPrimaryKeys().toArray());
    }

    protected ServerStorage<?> getOwner()
    {
        return owner;
    }

    protected void valueChanged(final Field field, final ObservableValue<?> me, final Object oldValue)
    {
        setState(ServerStorageStructureState.STATE_UPDATED);
        owner.valueChanged(this, field, me, oldValue);
    }

    public boolean hasState(final ServerStorageStructureState state)
    {
        return state.equals(this.state);
    }

    protected void setState(final ServerStorageStructureState state)
    {
        this.state = state;
    }

    public void delete()
    {
        setState(ServerStorageStructureState.STATE_DELETED);
        owner.structureDeleted(this);
    }

    public boolean isInSync()
    {
        return state.equals(ServerStorageStructureState.STATE_IN_SYNC);
    }

    public boolean isUpdated()
    {
        return state.equals(ServerStorageStructureState.STATE_UPDATED);
    }

    public boolean isNew()
    {
        return state.equals(ServerStorageStructureState.STATE_NEW);
    }

    public boolean isDeleted()
    {
        return state.equals(ServerStorageStructureState.STATE_DELETED);
    }

    private class ServerStorageIterator implements Iterator<Pair<ObservableValue<?>, Field>>
    {
        private final ServerStorageStructure storage;

        private final Field[] fields;

        private int i = 0;

        protected ServerStorageIterator(final ServerStorageStructure storage)
        {
            this.storage = storage;
            fields = storage.getAllFields();
        }

        @Override
        public boolean hasNext()
        {
            return i < fields.length;
        }

        @Override
        public Pair<ObservableValue<?>, Field> next()
        {
            try
            {
                final Pair<ObservableValue<?>, Field> pair = new Pair<>(storage.getObservableValue(fields[i]), fields[i]);
                i += 1;
                return pair;
            }
            catch (final Exception e)
            {
                return null;
            }
        }
    }

    @Override
    public Iterator<Pair<ObservableValue<?>, Field>> iterator()
    {
        return new ServerStorageIterator(this);
    }

    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ServerStorageStructure other = (ServerStorageStructure) obj;
        if (owner == null)
        {
            if (other.owner != null)
                return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        return getKey().hashCode();
    }
}
