package com.aniket.workmgmt.tasks;
import com.aniket.workmgmt.tasks.Status;

public class StatusUpdateRequest {
    private Long initiatorId;
    private Status status;

    public StatusUpdateRequest(){}

    public Long getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(Long initiatorId) {
        this.initiatorId = initiatorId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}