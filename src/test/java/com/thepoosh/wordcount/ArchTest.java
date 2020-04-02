package com.thepoosh.wordcount;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.thepoosh.wordcount");

        noClasses()
            .that()
                .resideInAnyPackage("com.thepoosh.wordcount.service..")
            .or()
                .resideInAnyPackage("com.thepoosh.wordcount.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.thepoosh.wordcount.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
