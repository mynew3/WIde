package wide.core.session.hooks;

import java.util.Collection;

import wide.core.WIde;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ActionHook
{
    private final Multimap<Hook, HookListener> listeners = HashMultimap.create();

    public void addListener(HookListener listener)
    {
        listeners.put(listener.getType(), listener);
    }

    public void removeListener(HookListener listener)
    {
        listeners.remove(listener.getType(), listener);
    }

    public void removeListenersOf(Object owner)
    {
        final Collection<HookListener> all_listeners = listeners.values();
        for (final HookListener listener : all_listeners)
            if (listener.getOwner() == owner)
                listeners.remove(listener.getType(), listener);
    }

    public void fire(Hook hook)
    {
        if (WIde.getEnviroment().isTraceEnabled())
            System.out.println("Firing hook: " + hook.name());

        final Collection<HookListener> hook_to_inform = listeners.get(hook);
        for (final HookListener listener : hook_to_inform)
            listener.informed();
    }
}
