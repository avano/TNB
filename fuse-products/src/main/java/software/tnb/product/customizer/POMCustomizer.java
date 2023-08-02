package software.tnb.product.customizer;

import software.tnb.common.config.TestConfiguration;
import software.tnb.product.util.maven.Maven;

import org.apache.maven.model.Model;

import java.io.File;

public abstract class POMCustomizer extends ProductsCustomizer {
    @Override
    public void customizeQuarkus() {
        File pomFile = TestConfiguration.appLocation().resolve(getIntegrationBuilder().getIntegrationName()).toFile();
        Model pom = Maven.loadPom(pomFile);
        customizeQuarkus(pom);
        Maven.writePom(pomFile, pom);
    }

    @Override
    public void customizeSpringboot() {
        File pomFile = TestConfiguration.appLocation().resolve(getIntegrationBuilder().getIntegrationName()).resolve("pom.xml").toFile();
        Model pom = Maven.loadPom(pomFile);
        customizeSpringboot(pom);
        Maven.writePom(pomFile, pom);
    }

    public abstract void customizeQuarkus(Model pom);

    public abstract void customizeSpringboot(Model pom);

    @Override
    public void customizeCamelK() {
    }
}
