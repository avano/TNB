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
 * AllTableReports
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2022-07-12T10:19:43.430893315+02:00[Europe/Rome]")
public class AllTableReports {
    public static final String SERIALIZED_NAME_REPORTS = "reports";
    public static final String SERIALIZED_NAME_COUNT = "count";
    @SerializedName(SERIALIZED_NAME_REPORTS)
    private List<TableReportSummary> reports = new ArrayList<TableReportSummary>();
    @SerializedName(SERIALIZED_NAME_COUNT)
    private Long count;

    public AllTableReports() {
    }

    public AllTableReports reports(List<TableReportSummary> reports) {

        this.reports = reports;
        return this;
    }

    public AllTableReports addReportsItem(TableReportSummary reportsItem) {
        this.reports.add(reportsItem);
        return this;
    }

    /**
     * Get reports
     *
     * @return reports
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")

    public List<TableReportSummary> getReports() {
        return reports;
    }

    public void setReports(List<TableReportSummary> reports) {
        this.reports = reports;
    }

    public AllTableReports count(Long count) {

        this.count = count;
        return this;
    }

    /**
     * Get count
     *
     * @return count
     **/
    @javax.annotation.Nonnull
    @ApiModelProperty(required = true, value = "")

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AllTableReports allTableReports = (AllTableReports) o;
        return Objects.equals(this.reports, allTableReports.reports) &&
            Objects.equals(this.count, allTableReports.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reports, count);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AllTableReports {\n");
        sb.append("    reports: ").append(toIndentedString(reports)).append("\n");
        sb.append("    count: ").append(toIndentedString(count)).append("\n");
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

