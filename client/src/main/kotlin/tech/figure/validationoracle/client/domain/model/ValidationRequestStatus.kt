package tech.figure.validationoracle.client.domain.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The status of a [ValidationRequestOrder].
 */
enum class ValidationRequestStatus {
    /** Denotes a validation request which has been submitted but not claimed or completed by any validator. */
    @JsonProperty("requested")
    REQUESTED,

    /**
     * Denotes a validation request which has been submitted and claimed by a validator for fulfillment,
     * but has yet to have results submitted.
     */
    @JsonProperty("pending")
    PENDING,

    /** Denotes a validation request which has had its results submitted. */
    @JsonProperty("fulfilled")
    FULFILLED,
}
