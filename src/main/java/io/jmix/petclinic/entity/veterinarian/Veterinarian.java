package io.jmix.petclinic.entity.veterinarian;

import io.jmix.core.DeletePolicy;
import io.jmix.core.entity.annotation.OnDelete;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.petclinic.entity.Person;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@JmixEntity
@Table(name = "PETCLINIC_VETERINARIAN")
@Entity(name = "petclinic_Veterinarian")
public class Veterinarian extends Person {
    @OnDelete(DeletePolicy.CASCADE)
    @JoinTable(name = "PETCLINIC_VET_SPECIALTY_LINK",
            joinColumns = @JoinColumn(name = "VETERINARIAN_ID"),
            inverseJoinColumns = @JoinColumn(name = "SPECIALTY_ID"))
    @ManyToMany
    private Set<Specialty> specialties;

    public Set<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Set<Specialty> specialties) {
        this.specialties = specialties;
    }
}