package np.sthaniya.dpis.location.api.dto.enums

import np.sthaniya.dpis.common.enum.EntityField

enum class DistrictField : EntityField {
    CODE {
        override fun toJsonFieldName() = "code"
    },
    NAME {
        override fun toJsonFieldName() = "name"
    },
    NAME_NEPALI {
        override fun toJsonFieldName() = "nameNepali"
    },
    AREA {
        override fun toJsonFieldName() = "area"
    },
    POPULATION {
        override fun toJsonFieldName() = "population"
    },
    HEADQUARTER {
        override fun toJsonFieldName() = "headquarter"
    },
    HEADQUARTER_NEPALI {
        override fun toJsonFieldName() = "headquarterNepali"
    },
    PROVINCE {
        override fun toJsonFieldName() = "province"
    },
    MUNICIPALITY_COUNT {
        override fun toJsonFieldName() = "municipalityCount"
    },
    TOTAL_POPULATION {
        override fun toJsonFieldName() = "totalPopulation"
    },
    TOTAL_AREA {
        override fun toJsonFieldName() = "totalArea"
    },
    GEOMETRY {
        override fun toJsonFieldName() = "geometry"
    },
    MUNICIPALITIES {
        override fun toJsonFieldName() = "municipalities"
    },
    CREATED_AT {
        override fun toJsonFieldName() = "createdAt"
    },
    CREATED_BY {
        override fun toJsonFieldName() = "createdBy"
    },
    UPDATED_AT {
        override fun toJsonFieldName() = "updatedAt"
    },
    UPDATED_BY {
        override fun toJsonFieldName() = "updatedBy"
    };

    override fun fieldName(): String = name

    companion object {
        val DEFAULT_FIELDS = setOf(CODE, NAME, NAME_NEPALI, AREA, POPULATION)
        val SUMMARY_FIELDS = setOf(CODE, NAME, NAME_NEPALI)
        val DETAIL_FIELDS = values().toSet()
        val AUDIT_FIELDS = setOf(CREATED_AT, CREATED_BY, UPDATED_AT, UPDATED_BY)

        fun fromString(value: String): DistrictField = valueOf(value.uppercase())
    }
}
