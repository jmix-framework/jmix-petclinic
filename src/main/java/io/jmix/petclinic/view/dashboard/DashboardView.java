package io.jmix.petclinic.view.dashboard;


import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.petclinic.view.main.MainView;

@Route(value = "dashboard", layout = MainView.class)
@ViewController(id = "petclinic_DashboardView")
@ViewDescriptor(path = "dashboard-view.xml")
public class DashboardView extends StandardView {
}