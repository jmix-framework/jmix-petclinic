package io.jmix.petclinic.entity.veterinarian;

import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.petclinic.entity.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@JmixEntity
@Table(name = "PETCLINIC_SPECIALTY")
@Entity(name = "petclinic_Specialty")
public class Specialty extends NamedEntity {
}