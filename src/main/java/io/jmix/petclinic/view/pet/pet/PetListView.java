package io.jmix.petclinic.view.pet.pet;

import io.jmix.flowui.component.grid.DataGridSortContext;
import io.jmix.flowui.component.grid.sort.DataGridSort;
import io.jmix.flowui.component.grid.sort.DataGridSortBuilder;
import io.jmix.flowui.component.propertyfilter.PropertyFilter;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.petclinic.entity.owner.Owner;
import io.jmix.petclinic.entity.pet.Pet;

import io.jmix.petclinic.entity.pet.PetType;
import io.jmix.petclinic.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

import java.util.List;

@Route(value = "pets", layout = MainView.class)
@ViewController("petclinic_Pet.list")
@ViewDescriptor("pet-list-view.xml")
@LookupComponent("petsDataGrid")
@DialogMode(width = "50em")
public class PetListView extends StandardListView<Pet> {

    @ViewComponent
    private PropertyFilter<String> identificationNumberFilter;
    @ViewComponent
    private PropertyFilter<PetType> typeFilter;
    @ViewComponent
    private PropertyFilter<Owner> ownerFilter;

    @Subscribe("clearFilterAction")
    public void onClearFilterAction(final ActionPerformedEvent event) {
        identificationNumberFilter.clear();
        typeFilter.clear();
        ownerFilter.clear();
    }

    @Install(to = "petsDataGrid", subject = "sortBuilderDelegate")
    private DataGridSort petsDataGridSortBuilderDelegate(final DataGridSortContext<Pet> context) {
        return DataGridSortBuilder.create(context)
                .replaceSort("owner", List.of("{E}.owner.firstName", "{E}.owner.lastName"))
                .build();
    }

}
