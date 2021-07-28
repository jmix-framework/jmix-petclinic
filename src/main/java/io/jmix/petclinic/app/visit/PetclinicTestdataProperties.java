package io.jmix.petclinic.app.visit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;


@ConstructorBinding
@ConfigurationProperties(prefix = "petclinic.testdata.visit")
public class PetclinicTestdataProperties {


    private final Integer visitStartAmountPastDays;
    private final Integer visitStartAmountFutureDays;
    private final Integer amountPerDay;
    private final List<String> descriptionOptions;


    public PetclinicTestdataProperties(
            Integer visitStartAmountPastDays,
            Integer visitStartAmountFutureDays,
            Integer amountPerDay,
            List<String> descriptionOptions
    ) {
        this.visitStartAmountPastDays = visitStartAmountPastDays;
        this.visitStartAmountFutureDays = visitStartAmountFutureDays;
        this.amountPerDay = amountPerDay;
        this.descriptionOptions = descriptionOptions;
    }

    public Integer getVisitStartAmountPastDays() {
        return visitStartAmountPastDays;
    }

    public Integer getVisitStartAmountFutureDays() {
        return visitStartAmountFutureDays;
    }

    public Integer getAmountPerDay() {
        return amountPerDay;
    }

    public List<String> getDescriptionOptions() {
        return descriptionOptions;
    }
}