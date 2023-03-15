

package no.nav.statusportalgcppoll;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import java.util.Objects;
import java.util.UUID;

/**
* RecordDto
*/
public class RecordDto {

    private UUID serviceId = null;

    private OffsetDateTime timestamp = null;

    private StatusDto status = null;

    private String description = null;

    private String logLink = null;

    private Integer responseTime = null;




    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public RecordDto timestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public void setServiceId(UUID serviceId) {
        this.serviceId = serviceId;
    }

    public RecordDto serviceId(UUID serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public StatusDto getStatus() {
        return status;
    }

    public void setStatus(StatusDto status) {
        this.status = status;
    }

    public RecordDto status(StatusDto status) {
        this.status = status;
        return this;
    }

    /**
     * Get description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RecordDto description(String description) {
        this.description = description;
        return this;
    }


    public String getLogLink() {
        return logLink;
    }

    public void setLogLink(String logLink) {
        this.logLink = logLink;
    }

    public RecordDto logLink(String logLink) {
        this.logLink = logLink;
        return this;
    }


    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public RecordDto responseTime(Integer responseTime) {
        this.responseTime = responseTime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecordDto record = (RecordDto) o;
        return  Objects.equals(this.getTimestamp(), record.getTimestamp()) &&
                Objects.equals(this.getServiceId(), record.getServiceId()) &&
                Objects.equals(this.getStatus(), record.getStatus()) &&
                Objects.equals(this.getDescription(), record.getDescription()) &&
                Objects.equals(this.getLogLink(), record.getLogLink()) &&
                Objects.equals(this.getResponseTime(), record.getResponseTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimestamp(), getServiceId(), getStatus(), getDescription(), getLogLink(), getResponseTime());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RecordDto {\n");
        sb.append("    timestamp: ").append(toIndentedString(getTimestamp())).append("\n");
        sb.append("    serviceId: ").append(toIndentedString(getServiceId())).append("\n");
        sb.append("    status: ").append(toIndentedString(getStatus())).append("\n");
        sb.append("    description: ").append(toIndentedString(getDescription())).append("\n");
        sb.append("    logLink: ").append(toIndentedString(getLogLink())).append("\n");
        sb.append("    responseTime: ").append(toIndentedString(getResponseTime())).append("\n");
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

    public static class RecordDtoAdapter extends TypeAdapter<RecordDto> {
        @Override
        public void write(JsonWriter writer, RecordDto recordDto) throws IOException {
            writer.beginObject();
            writer.name("serviceId");
            writer.value(recordDto.getServiceId().toString());
            writer.name("timestamp");
            writer.value(recordDto.getTimestamp().truncatedTo(ChronoUnit.SECONDS).toString());
            writer.name("status");
            writer.value(recordDto.getStatus().getValue());
            writer.name("description");
            writer.value(recordDto.getDescription());
            writer.name("logLink");
            writer.value(recordDto.getLogLink());
            writer.name("responseTime");
            writer.value(recordDto.getResponseTime());
            writer.endObject();
        }

        @Override
        public RecordDto read(JsonReader in) throws IOException {
            return null;
        }

    }
}

