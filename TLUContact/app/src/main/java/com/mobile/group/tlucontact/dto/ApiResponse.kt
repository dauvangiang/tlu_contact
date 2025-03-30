package com.mobile.group.tlucontact.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ApiResponse<T> @JsonCreator constructor(
    @JsonProperty("code") val code: Int,
    @JsonProperty("data") val data: T? = null,
    @JsonProperty("message") val message: String,
    @JsonProperty("total_record") val totalRecord: Int? = null,
    @JsonProperty("current_page") val currentPage: Int? = null,
    @JsonProperty("errors") val errors: Map<String, Any>? = null
)
