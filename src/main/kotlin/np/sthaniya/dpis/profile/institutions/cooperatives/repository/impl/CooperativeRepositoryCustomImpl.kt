package np.sthaniya.dpis.profile.institutions.cooperatives.repository.impl

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import np.sthaniya.dpis.profile.institutions.cooperatives.dto.response.CooperativeProjection
import np.sthaniya.dpis.profile.institutions.cooperatives.model.Cooperative
import np.sthaniya.dpis.profile.institutions.cooperatives.repository.CooperativeRepositoryCustom
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository

/** Implementation of the custom repository methods for [Cooperative] entities. */
@Repository
class CooperativeRepositoryCustomImpl : CooperativeRepositoryCustom {

    @PersistenceContext private lateinit var entityManager: EntityManager

    /** Finds cooperatives within a specified distance of a geographic point using PostGIS. */
    override fun findWithinDistance(
            longitude: Double,
            latitude: Double,
            distanceInMeters: Double,
            pageable: Pageable
    ): Page<Cooperative> {
        // Create native query to use PostGIS functions
        val sqlQuery =
                """
            SELECT c.* FROM cooperatives c
            WHERE ST_DWithin(
                c.point,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                :distanceInMeters
            )
            ORDER BY ST_Distance(
                c.point,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography
            )
        """.trimIndent()

        // Create the queries (one for data, one for count)
        val query =
                entityManager
                        .createNativeQuery(sqlQuery, Cooperative::class.java)
                        .setParameter("longitude", longitude)
                        .setParameter("latitude", latitude)
                        .setParameter("distanceInMeters", distanceInMeters)
                        .setFirstResult(pageable.offset.toInt())
                        .setMaxResults(pageable.pageSize)

        val countQuery =
                entityManager
                        .createNativeQuery(
                                """
            SELECT COUNT(*) FROM cooperatives c
            WHERE ST_DWithin(
                c.point,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                :distanceInMeters
            )
        """.trimIndent()
                        )
                        .setParameter("longitude", longitude)
                        .setParameter("latitude", latitude)
                        .setParameter("distanceInMeters", distanceInMeters)

        // Execute queries
        val results = query.resultList as List<Cooperative>
        val total = ((countQuery.singleResult as Number).toLong())

        return PageImpl(results, pageable, total)
    }

    /** Retrieves a paginated list of cooperatives as projections with selected fields. */
    override fun findAllWithProjection(
            spec: Specification<Cooperative>,
            pageable: Pageable,
            columns: Set<String>,
            includeTranslations: Boolean,
            includePrimaryMedia: Boolean
    ): Page<CooperativeProjection> {
        val cb = entityManager.criteriaBuilder
        val query = cb.createQuery(Cooperative::class.java)
        val root = query.from(Cooperative::class.java)

        // Apply specification if provided
        spec.toPredicate(root, query, cb)?.let { query.where(it) }

        // Apply sorting
        if (pageable.sort.isSorted) {
            // Create individual order expressions and apply them one by one
            pageable.sort.forEach { order ->
                val orderExpr =
                        if (order.isAscending) cb.asc(root.get<Any>(order.property))
                        else cb.desc(root.get<Any>(order.property))

                // For the first order expression, initialize orderBy
                if (query.orderList.isEmpty()) {
                    query.orderBy(orderExpr)
                } else {
                    // For subsequent expressions, add to the existing order list
                    query.orderBy(query.orderList + orderExpr)
                }
            }
        }

        // Execute query with pagination
        val typedQuery =
                entityManager
                        .createQuery(query)
                        .setFirstResult(pageable.offset.toInt())
                        .setMaxResults(pageable.pageSize)

        val results = typedQuery.resultList

        // Get total count
        val countQuery = cb.createQuery(Long::class.java)
        val countRoot = countQuery.from(Cooperative::class.java)
        countQuery.select(cb.count(countRoot))

        // Apply the same specification for counting
        spec.toPredicate(countRoot, countQuery, cb)?.let { countQuery.where(it) }

        val total = entityManager.createQuery(countQuery).singleResult

        // Map to projections
        val projections =
                results.map { cooperative ->
                    transformToProjection(
                            cooperative,
                            columns,
                            includeTranslations,
                            includePrimaryMedia
                    )
                }

        return PageImpl(projections, pageable, total)
    }

    /** Transforms a Cooperative entity to a projection with selected fields. */
    private fun transformToProjection(
            cooperative: Cooperative,
            columns: Set<String>,
            includeTranslations: Boolean,
            includePrimaryMedia: Boolean
    ): CooperativeProjection {
        val projection = CooperativeProjection()

        // Add selected fields directly to the projection instead of nesting them in a "data" map
        columns.forEach { column ->
            when (column) {
                "id" -> projection.properties["id"] = cooperative.id
                "code" -> projection.properties["code"] = cooperative.code
                "defaultLocale" ->
                        projection.properties["defaultLocale"] = cooperative.defaultLocale
                "establishedDate" ->
                        projection.properties["establishedDate"] = cooperative.establishedDate
                "ward" -> projection.properties["ward"] = cooperative.ward
                "type" -> projection.properties["type"] = cooperative.type
                "status" -> projection.properties["status"] = cooperative.status
                "registrationNumber" ->
                        projection.properties["registrationNumber"] = cooperative.registrationNumber
                "point" ->
                        projection.properties["point"] =
                                cooperative.point?.let {
                                    mapOf("longitude" to it.x, "latitude" to it.y)
                                }
                "contactEmail" -> projection.properties["contactEmail"] = cooperative.contactEmail
                "contactPhone" -> projection.properties["contactPhone"] = cooperative.contactPhone
                "websiteUrl" -> projection.properties["websiteUrl"] = cooperative.websiteUrl
                "createdAt" -> projection.properties["createdAt"] = cooperative.createdAt
                "updatedAt" -> projection.properties["updatedAt"] = cooperative.updatedAt
            }
        }

        // Add translations if requested
        if (includeTranslations && "translations" in columns) {
            projection.properties["translations"] =
                    cooperative.translations.map { translation ->
                        mapOf(
                                "id" to translation.id,
                                "locale" to translation.locale,
                                "name" to translation.name,
                                "description" to translation.description,
                                "status" to translation.status
                        )
                    }
        }

        // Add primary media if requested
        if (includePrimaryMedia && "primaryMedia" in columns) {
            val primaryMediaMap = mutableMapOf<String, Map<String, Any?>>()

            // Group by media type and find primary ones
            cooperative.media.filter { it.isPrimary }.forEach { media ->
                val mediaType = media.type.name
                primaryMediaMap[mediaType] =
                        mapOf(
                                "id" to media.id,
                                "title" to media.title,
                                "fileUrl" to media.fileUrl,
                                "thumbnailUrl" to media.thumbnailUrl,
                                "altText" to media.altText
                        )
            }

            projection.properties["primaryMedia"] = primaryMediaMap
        }

        return projection
    }
}
