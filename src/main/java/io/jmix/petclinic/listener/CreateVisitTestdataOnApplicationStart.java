package io.jmix.petclinic.listener;

import io.jmix.core.security.Authenticated;
import io.jmix.petclinic.app.visit.VisitTestDataCreation;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("petclinic_CreateVisitTestdataOnApplicationStart")
public class CreateVisitTestdataOnApplicationStart {


    @Inject
    protected VisitTestDataCreation visitTestDataCreation;


    @Authenticated
    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        visitTestDataCreation.createData();
    }
}