package io.jmix.petclinic.view.visit;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.view.main.MainView;


@Route(value = "visits-lookup", layout = MainView.class)
@ViewController(id = "petclinic_Visit.lookup")
@ViewDescriptor(path = "visit-lookup-view.xml")
@LookupComponent("visitsDataGrid")
@DialogMode(width = "64em")
public class VisitLookupView extends StandardListView<Visit> {

}