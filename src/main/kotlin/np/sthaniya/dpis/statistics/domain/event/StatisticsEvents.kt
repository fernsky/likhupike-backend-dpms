package np.sthaniya.dpis.statistics.domain.event

import np.sthaniya.dpis.statistics.domain.event.agriculture.AgriculturalStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.core.StatisticsCalculatedEvent
import np.sthaniya.dpis.statistics.domain.event.core.StatisticsInvalidatedEvent
import np.sthaniya.dpis.statistics.domain.event.core.StatisticalAdjustmentEvent
import np.sthaniya.dpis.statistics.domain.event.core.StatisticsAccessedEvent
import np.sthaniya.dpis.statistics.domain.event.demographics.DemographicStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.demographics.CasteStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.demographics.LanguageStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.demographics.ReligionStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.demographics.AgeGenderStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.demographics.MaritalStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.economics.EconomicStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.education.EducationStatsUpdatedEvent
import np.sthaniya.dpis.statistics.domain.event.institutions.InstitutionalStatsUpdatedEvent

/**
 * This file serves as a central reference point for all statistics events.
 * The actual event implementations have been moved to domain-specific packages
 * for better organization and maintainability.
 * 
 * See:
 * - StatisticsEvent.kt - Base interface
 * - core/* - Core statistics events
 * - demographics/* - Demographic statistics events
 * - economics/* - Economic statistics events
 * - education/* - Education statistics events
 * - agriculture/* - Agricultural statistics events
 * - institutions/* - Institutional statistics events
 */
