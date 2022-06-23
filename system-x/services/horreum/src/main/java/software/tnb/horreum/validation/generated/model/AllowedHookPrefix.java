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

import java.util.Objects;

import io.swagger.annotations.ApiModelProperty;

/**
 * AllowedHookPrefix
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-07-12T10:19:43.430893315+02:00[Europe/Rome]")
public class AllowedHookPrefix {
    public static final String SERIALIZED_NAME_ID = "id";
    public static final String SERIALIZED_NAME_PREFIX = "prefix";
    @SerializedName(SERIALIZED_NAME_ID)
    private Long id;
    @SerializedName(SERIALIZED_NAME_PREFIX)
    private String prefix;

    public AllowedHookPrefix() {
    }

    public AllowedHookPrefix id(Long id) {

        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @javax.annotation.Nullable
    @ApiModelProperty(value = "")

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AllowedHookPrefix prefix(String prefix) {

        this.prefix = prefix;
        return this;
    }

    /**
     * Get prefix
     *
     * @return prefix
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AllowedHookPrefix allowedHookPrefix = (AllowedHookPrefix) o;
        return Objects.equals(this.id, allowedHookPrefix.id) &&
            Objects.equals(this.prefix, allowedHookPrefix.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prefix);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AllowedHookPrefix {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    prefix: ").append(toIndentedString(prefix)).append("\n");
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

