
/*
 * Copyright (c) 2013 - 2015 Naios <naios-dev@live.de>
 *
 * This file is part of WIde which is released under Creative Commons 4.0 (by-nc-sa)
 * See file LICENSE for full license details.
 */

package com.github.naios.wide.api.framework.storage.server;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlySetProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

import com.github.naios.wide.api.config.schema.MappingMetaData;
import com.github.naios.wide.api.util.Pair;

public interface StructureChangeTracker
{
    public void track(ServerStorageStructure structure);

    public void untrack(ServerStorageStructure structure);

    public ReadOnlySetProperty<ServerStorageStructure> structuresRecentlyCreated();

    public ReadOnlySetProperty<ServerStorageStructure> structuresRecentlyDeleted();

    public ReadOnlySetProperty<Pair<ObservableValue<?>, MappingMetaData>> observablesRecentlyChanged();

    public ReadOnlyMapProperty<ServerStorage<?>, ReadOnlyMapProperty<ServerStorageStructure, ReadOnlyListProperty<StructureChangeEvent>>> changeMap();

    public Object getRemoteValue(ServerStorageStructure structure, Pair<ObservableValue<?>, MappingMetaData> value);

    /**
     * @return Our scope property
     */
    public StringProperty scope();

    /**
     * Sets our current scope
     * @param scope unique Scope identifier
     */
    public void setScope(String scope);

    /**
     * Sets our current scope
     * @param scope unique Scope identifier
     * @param comment our comment we want to set
     */
    public void setScope(String scope, String comment);

    /**
     * Releases the scope<br>
     * Equal to setScope(DEFAULT_SCOPE)
     */
    public void releaseScope();

    /**
     * Sets an observable value as custom variable<br>
     * Value is wrapped into the variable then
     */
    public void setCustomVariable(ObservableValue<?> value, String name);

    /**
     * Releases a custom variable of an observable value
     */
    public void releaseCustomVariable(ObservableValue<?> value);

    /**
     * Gets the custom variable of the observable value
     * @param value The observable value we want to get the variable name of
     * @return null if not existing, variable name otherwise
     */
    public String getCustomVariable(ObservableValue<?> value);

    /**
     * Sets the comment of the current scope
     * @param comment the comment you want to set
     */
    public void setScopeComment(String comment);

    /**
     * Drops all changes so the change tracker is in sync with the remote database
     */
    public void reset();

    /**
     * Commits the current content to the database
     */
    public void commit();

    /**
     * @return Returns the sql query of all changes
     */
    public String getQuery();
}