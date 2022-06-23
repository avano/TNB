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

import java.math.BigDecimal;
import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * DatapointLastTimestamp
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-07-12T10:19:43.430893315+02:00[Europe/Rome]")
public class DatapointLastTimestamp {
    public static final String SERIALIZED_NAME_VARIABLE = "variable";
    public static final String SERIALIZED_NAME_TIMESTAMP = "timestamp";
    @SerializedName(SERIALIZED_NAME_VARIABLE)
    private Integer variable;
    @SerializedName(SERIALIZED_NAME_TIMESTAMP)
    private BigDecimal timestamp;

    public DatapointLastTimestamp() {
    }

    public DatapointLastTimestamp variable(Integer variable) {

        this.variable = variable;
        return this;
    }

    /**
     * Get variable
     *
     * @return variable
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")

    public Integer getVariable() {
        return variable;
    }

    public void setVariable(Integer variable) {
        this.variable = variable;
    }

    public DatapointLastTimestamp timestamp(BigDecimal timestamp) {

        this.timestamp = timestamp;
        return this;
    }

    /**
     * Get timestamp
     *
     * @return timestamp
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")

    public BigDecimal getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigDecimal timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DatapointLastTimestamp datapointLastTimestamp = (DatapointLastTimestamp) o;
        return Objects.equals(this.variable, datapointLastTimestamp.variable) &&
            Objects.equals(this.timestamp, datapointLastTimestamp.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, timestamp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DatapointLastTimestamp {\n");
        sb.append("    variable: ").append(toIndentedString(variable)).append("\n");
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

