package io.jmix.petclinic.screen.visit;

import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.petclinic.entity.visit.VisitTreatmentStatus;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.action.list.EditAction;
import io.jmix.ui.component.Table;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.screen.MasterDetailScreen;
import io.jmix.ui.screen.MessageBundle;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Named;

@UiController("petclinic_MyVisits")
@UiDescriptor("my-visits.xml")
@LookupComponent("table")
@Route(value = "my-visits")
public class MyVisits extends MasterDetailScreen<Visit> {


    @Autowired
    protected CollectionLoader<Visit> visitsDl;

    @Autowired
    protected Table<Visit> table;
    @Autowired
    protected Notifications notifications;

    @Named("table.edit")
    protected EditAction<Visit> tableEdit;
    @Autowired
    protected CurrentAuthentication currentAuthentication;
    @Autowired
    protected InstanceContainer<Visit> visitDc;
    @Autowired
    protected DataManager dataManager;
    @Autowired
    protected MessageBundle messageBundle;

    @Subscribe
    protected void onInit(InitEvent event) {
        tableEdit.withHandler(actionPerformedEvent -> {
            Visit item = table.getSingleSelected();
            if (item != null) {
                refreshOptionsForLookupFields();
                disableEditControls();
                getActionsPane().setVisible(true);
            }
        });
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        visitsDl.setParameter("currentUser", currentAuthentication.getUser());
        visitsDl.load();
    }


    @Subscribe("table.start")
    protected void onStart(Action.ActionPerformedEvent event) {
        final Visit visit = table.getSingleSelected();

        if (visit.hasStarted()) {
            petTreatmentWarningMessage("treatmentAlreadyStarted", visit.getPetName());
        }
        else {
            updateTreatmentTo(visit, VisitTreatmentStatus.IN_PROGRESS);
            petTreatmentSuccessMessage("treatmentStarted", visit.getPetName());
        }
    }

    @Subscribe("table.finish")
    protected void onTableFinish(Action.ActionPerformedEvent event) {
        final Visit visit = table.getSingleSelected();

        if (visit.hasFinished()) {
            petTreatmentWarningMessage("treatmentAlreadyFinished", visit.getPetName());
        }
        else {
            updateTreatmentTo(visit, VisitTreatmentStatus.DONE);
            petTreatmentSuccessMessage("treatmentFinished", visit.getPetName());
        }
    }

    private void petTreatmentWarningMessage(String messageKey, String petName) {
        message(messageKey, petName, Notifications.NotificationType.WARNING);
    }

    private void petTreatmentSuccessMessage(String messageKey, String petName) {
        message(messageKey, petName, Notifications.NotificationType.TRAY);
    }

    private void message(String messageKey, String petName, Notifications.NotificationType warning) {
        notifications.create(warning)
                .withCaption(messageBundle.formatMessage(messageKey, petName))
                .show();
    }
    private void updateTreatmentTo(Visit visitToStart, VisitTreatmentStatus targetStatus) {
        visitToStart.setTreatmentStatus(targetStatus);
        dataManager.save(visitToStart);
        visitsDl.load();
    }

}