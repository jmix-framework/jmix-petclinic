package io.jmix.petclinic.screen.owner;

import io.jmix.core.EntityStates;
import io.jmix.ui.component.Label;
import io.jmix.ui.screen.*;
import io.jmix.petclinic.entity.owner.Owner;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;

@UiController("petclinic_Owner.edit")
@UiDescriptor("owner-edit.xml")
@EditedEntityContainer("ownerDc")
public class OwnerEdit extends StandardEditor<Owner> {

    @Inject
    protected Label<String> titleLabel;
    @Inject
    protected MessageBundle messageBundle;
    @Autowired
    private EntityStates entityStates;

    @Subscribe
    protected void onAfterShow(AfterShowEvent event) {
        titleLabel.setValue(editorTitleLabel());
    }

    @NotNull
    private String editorTitleLabel() {
        if (entityStates.isNew(getEditedEntity())) {
            return messageBundle.getMessage("newOwnerTitle");
        }
        else {
            return messageBundle.formatMessage("ownerTitle", getEditedEntity().getName());
        }
    }
}