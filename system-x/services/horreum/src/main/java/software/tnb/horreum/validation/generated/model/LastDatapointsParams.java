/*
 * Horreum API
 * Horreum data repository API
 *
 * The version of the OpenAPI document: 0.1-SNAPSHOT
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package software.tnb.horreum.validation.generated.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * LastDatapointsParams
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-07-12T10:19:43.430893315+02:00[Europe/Rome]")
public class LastDatapointsParams {
    public static final String SERIALIZED_NAME_VARIABLES = "variables";
    public static final String SERIALIZED_NAME_FINGERPRINT = "fingerprint";
    @SerializedName(SERIALIZED_NAME_VARIABLES)
    private List<Integer> variables = new ArrayList<Integer>();
    @SerializedName(SERIALIZED_NAME_FINGERPRINT)
    private String fingerprint;

    public LastDatapointsParams() {
    }

    public LastDatapointsParams variables(List<Integer> variables) {

        this.variables = variables;
        return this;
    }

    public LastDatapointsParams addVariablesItem(Integer variablesItem) {
        this.variables.add(variablesItem);
        return this;
    }

    /**
     * Get variables
     *
     * @return variables
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")

    public List<Integer> getVariables() {
        return variables;
    }

    public void setVariables(List<Integer> variables) {
        this.variables = variables;
    }

    public LastDatapointsParams fingerprint(String fingerprint) {

        this.fingerprint = fingerprint;
        return this;
    }

    /**
     * Get fingerprint
     *
     * @return fingerprint
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LastDatapointsParams lastDatapointsParams = (LastDatapointsParams) o;
        return Objects.equals(this.variables, lastDatapointsParams.variables) &&
            Objects.equals(this.fingerprint, lastDatapointsParams.fingerprint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variables, fingerprint);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LastDatapointsParams {\n");
        sb.append("    variables: ").append(toIndentedString(variables)).append("\n");
        sb.append("    fingerprint: ").append(toIndentedString(fingerprint)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

