package io.jmix.petclinic.screen.visit;

import io.jmix.core.EntityStates;
import io.jmix.petclinic.app.EmployeeRepository;
import io.jmix.petclinic.entity.User;
import io.jmix.petclinic.entity.visit.Visit;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.navigation.Route;
import io.jmix.ui.screen.EditedEntityContainer;
import io.jmix.ui.screen.MessageBundle;
import io.jmix.ui.screen.StandardEditor;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UiController("petclinic_Visit.edit")
@UiDescriptor("visit-edit.xml")
@EditedEntityContainer("visitDc")
@Route(value = "visits/edit", parentPrefix = "visits")
public class VisitEdit extends StandardEditor<Visit> {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EntityComboBox<User> assignedNurseField;

    @Autowired
    protected MessageBundle messageBundle;
    @Autowired
    private EntityStates entityStates;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        List<User> allNurses = employeeRepository.findAllNurses();
        assignedNurseField.setOptionsList(allNurses);

        getWindow().setCaption(editorTitleLabel());
    }


    private String editorTitleLabel() {
        if (entityStates.isNew(getEditedEntity())) {
            return messageBundle.getMessage("newCaption");
        }
        else {
            return messageBundle.getMessage("editCaption");
        }
    }
}