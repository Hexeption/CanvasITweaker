package com.canvasclient.event;

import com.canvasclient.event.base.Event;
import com.canvasclient.event.base.Listener;

import java.util.*;


public class EventManager {
	private static final EventManager INSTANCE = new EventManager();
    private final List<Listener> buffer = new ArrayList<Listener>();
    private final Map<Class, List<Listener>> registery = new HashMap<Class, List<Listener>>();
    private final Map<Class<? extends Event>, Listener[]> map = new HashMap<Class<? extends Event>, Listener[]>();

    private EventManager(){}
    
    public void register(Object src, Listener... listeners) {
        final List<Listener> list = this.getEntry(src.getClass());
        if(listeners != null && listeners.length > 0) {
            for(Listener l : listeners) {
                if(!list.contains(l))
                    list.add(l);
            }
        }
        for(Listener l : listeners) {
            this.register(l);
        }
    }

    public void register(Listener l) {
        buffer.clear();
        final Listener[] list = map.get(l.getType());
        if(list != null && list.length > 0)
            buffer.addAll(Arrays.asList(list));
        int x = 0;
        if(!buffer.isEmpty()) {
            for(;x < buffer.size(); x++) {
                final Listener l1 = buffer.get(x);
                if(l.getPriority() < l1.getPriority())
                    break;
            }
        }
        buffer.add(x, l);
        map.put(l.getType(), buffer.toArray(new Listener[buffer.size()]));
    }

    public void unregister(Object src, Listener... listeners) {
        final List<Listener> list = this.getEntry(src.getClass());
        if(listeners != null && listeners.length > 0) {
            for(Listener l : listeners) {
                this.unregister(l);
                list.remove(l);
            }
        } else {
            for(Listener listener : list)
                this.unregister(listener);
            list.clear();
        }
    }

    public void unregister(Listener l) {
        buffer.clear();
        final Listener[] list = map.get(l.getType());
        if(list != null && list.length > 0) {
            for(Listener listener : list) {
                if(listener == l)
                    continue;
                buffer.add(listener);
            }
        }
        map.put(l.getType(), buffer.toArray(new Listener[buffer.size()]));
    }

    public <T extends Event> T call(T event) {
        final Listener[] list = this.map.get(event.getClass());
        if(list != null) {
            for(Listener l : list)
                l.call(event);
        }
        return event;
    }

    private List<Listener> getEntry(Class src) {
        List<Listener> list = this.registery.get(src);
        if(list == null) {
            list = new ArrayList();
            this.registery.put(src.getClass(), list);
        }
        return list;
    }
    
    public static EventManager get() {
    	return INSTANCE;
    }
}