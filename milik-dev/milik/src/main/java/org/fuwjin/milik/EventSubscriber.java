package org.fuwjin.milik;

public interface EventSubscriber {
    void onEvent(EventPublisher ep, Event e);
}
