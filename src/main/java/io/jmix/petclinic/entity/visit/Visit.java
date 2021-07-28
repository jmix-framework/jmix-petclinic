package io.jmix.petclinic.entity.visit;

import io.jmix.core.annotation.DeletedBy;
import io.jmix.core.annotation.DeletedDate;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.DependsOnProperties;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.core.metamodel.annotation.JmixProperty;
import io.jmix.petclinic.entity.NamedEntity;
import io.jmix.petclinic.entity.User;
import io.jmix.petclinic.entity.pet.Pet;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static io.jmix.petclinic.entity.visit.VisitTreatmentStatus.DONE;
import static io.jmix.petclinic.entity.visit.VisitTreatmentStatus.IN_PROGRESS;

@JmixEntity
@Table(name = "PETCLINIC_VISIT")
@Entity(name = "petclinic_Visit")
public class Visit {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @Column(name = "VERSION", nullable = false)
    @Version
    private Integer version;

    @CreatedBy
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @DeletedBy
    @Column(name = "DELETED_BY")
    private String deletedBy;

    @DeletedDate
    @Column(name = "DELETED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedDate;

    @Column(name = "TYPE_", nullable = false)
    @NotNull
    private String type;

    @JoinColumn(name = "ASSIGNED_NURSE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private User assignedNurse;

    @JoinColumn(name = "PET_ID", nullable = false)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Pet pet;

    @Column(name = "VISIT_START")
    private LocalDateTime visitStart;

    @Column(name = "VISIT_END")
    private LocalDateTime visitEnd;

    @Column(name = "DESCRIPTION", length = 4000)
    private String description;

    @Column(name = "TREATMENT_STATUS")
    private String treatmentStatus;

    @Transient
    @JmixProperty
    @DependsOnProperties({"type"})
    public String getTypeStyle() {
        return Optional.ofNullable(getType())
                .map(VisitType::getStyleName)
                .orElse("");
    }

    @Transient
    @JmixProperty
    @DependsOnProperties({"pet"})
    public String getPetName() {
        return Optional.ofNullable(getPet())
                .map(NamedEntity::getName)
                .orElse("");
    }

    public VisitTreatmentStatus getTreatmentStatus() {
        return treatmentStatus == null ? null : VisitTreatmentStatus.fromId(treatmentStatus);
    }

    public void setTreatmentStatus(VisitTreatmentStatus treatmentStatus) {
        this.treatmentStatus = treatmentStatus == null ? null : treatmentStatus.getId();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getVisitEnd() {
        return visitEnd;
    }

    public void setVisitEnd(LocalDateTime visitEnd) {
        this.visitEnd = visitEnd;
    }

    public LocalDateTime getVisitStart() {
        return visitStart;
    }

    public void setVisitStart(LocalDateTime visitStart) {
        this.visitStart = visitStart;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public User getAssignedNurse() {
        return assignedNurse;
    }

    public void setAssignedNurse(User assignedNurse) {
        this.assignedNurse = assignedNurse;
    }

    public VisitType getType() {
        return type == null ? null : VisitType.fromId(type);
    }

    public void setType(VisitType type) {
        this.type = type == null ? null : type.getId();
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public boolean hasStarted() {
        return inTreatmentStatus(IN_PROGRESS) || inTreatmentStatus(DONE);
    }

    public boolean hasFinished() {
        return inTreatmentStatus(DONE);
    }

    private boolean inTreatmentStatus(VisitTreatmentStatus visitTreatmentStatus) {
        return getTreatmentStatus().equals(visitTreatmentStatus);
    }
}