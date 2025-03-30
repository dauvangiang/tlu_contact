package com.mobile.group.tlucontact.models

import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
class Department @JsonCreator constructor(
    @JsonProperty("code") val code: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("phone") val phone: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("logoURL") val logoUrl: String? = null,
    @JsonProperty("address") val address: String? = null,
    @JsonProperty("fax") val fax: String? = null,
    @JsonProperty("type") val type: String,

    @JsonDeserialize(using = ParentDepartmentDeserializer::class)
    @JsonProperty("parentDepartment") val parentDepartment: ParentDepartment? = null,

    @JsonProperty("dependentDepartments") val dependentDepartments: List<DependentDepartment>? = emptyList()
) : Parcelable {
    val firstLetter: String
        get() = name.first().uppercase()
}

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
class ParentDepartment @JsonCreator constructor(
    @JsonProperty("code") val code: String,
    @JsonProperty("name") val name: String
) : Parcelable

@Parcelize
@JsonIgnoreProperties(ignoreUnknown = true)
class DependentDepartment @JsonCreator constructor(
    @JsonProperty("code") val code: String,
    @JsonProperty("name") val name: String
) : Parcelable

class ParentDepartmentDeserializer : JsonDeserializer<ParentDepartment?>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ParentDepartment? {
        val node: JsonNode = p.codec.readTree(p)

        // Nếu là object rỗng `{}`, trả về null
        if (node.isObject && node.size() == 0) {
            return null
        }

        // Ngược lại, deserialize bình thường
        val code = node.get("code")?.asText() ?: return null
        val name = node.get("name")?.asText() ?: return null
        return ParentDepartment(code, name)
    }
}