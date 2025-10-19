package com.pwhs.quickmem.domain.model.subject

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.pwhs.quickmem.R

data class SubjectModel(
    val id: Int = 0,
    @param:StringRes val subjectName: Int = -1,
    @param:DrawableRes val iconRes: Int? = null,
    var studySetCount: Int = 0,
    val color: Color? = null,
    @param:StringRes val subjectDescription: Int? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
) {
    companion object {
        val defaultSubjects: List<SubjectModel>
            get() = listOf(
                SubjectModel(
                    id = 1,
                    subjectName = R.string.txt_general,
                    iconRes = R.drawable.ic_all,
                    color = Color(0xFF7f60f9),
                    subjectDescription = R.string.txt_general_subjects_that_do_not_fit_into_specific_categories
                ),
                SubjectModel(
                    id = 2,
                    subjectName = R.string.txt_anthropology,
                    iconRes = R.drawable.ic_anthropology,
                    color = Color(0xFFedb527),
                    subjectDescription = R.string.txt_anthropology_is_the_study_of_human_societies_and_cultures
                ),
                SubjectModel(
                    id = 3,
                    subjectName = R.string.txt_architecture_and_design,
                    iconRes = R.drawable.ic_architecture_and_design,
                    color = Color(0xFF7f60f9),
                    subjectDescription = R.string.txt_architecture_and_design_is_the_study_of_buildings_structures_and_spaces
                ),
                SubjectModel(
                    id = 4,
                    subjectName = R.string.txt_arts,
                    iconRes = R.drawable.ic_arts,
                    color = Color(0xFF7f60f9),
                    subjectDescription = R.string.txt_arts_is_the_study_of_creative_expressions_in_various_forms_like_painting_music_and_sculpture
                ),
                SubjectModel(
                    id = 5,
                    subjectName = R.string.txt_biology,
                    iconRes = R.drawable.ic_biology,
                    color = Color(0xFF15b39b),
                    subjectDescription = R.string.txt_biology_is_the_study_of_living_organisms_and_life_processes
                ),
                SubjectModel(
                    id = 6,
                    subjectName = R.string.txt_business,
                    iconRes = R.drawable.ic_business,
                    color = Color(0xFF7f60f9),
                    subjectDescription = R.string.txt_business_is_the_study_of_commerce_trade_and_organizational_management
                ),
                SubjectModel(
                    id = 7,
                    subjectName = R.string.txt_chemistry,
                    iconRes = R.drawable.ic_chemistry,
                    color = Color(0xFF15b39b),
                    subjectDescription = R.string.txt_chemistry_is_the_study_of_substances_their_properties_and_reactions
                ),
                SubjectModel(
                    id = 8,
                    subjectName = R.string.txt_computer_sciences,
                    iconRes = R.drawable.ic_computer_sciences,
                    color = Color(0xFF3678fd),
                    subjectDescription = R.string.txt_computer_science_is_the_study_of_computational_systems_algorithms_and_programming
                ),
                SubjectModel(
                    id = 9,
                    subjectName = R.string.txt_earth_sciences,
                    iconRes = R.drawable.ic_earth_sciences,
                    color = Color(0xFF15b39b),
                    subjectDescription = R.string.txt_earth_science_is_the_study_of_the_earth_its_composition_and_natural_phenomena
                ),
                SubjectModel(
                    id = 10,
                    subjectName = R.string.txt_economics,
                    iconRes = R.drawable.ic_economics,
                    color = Color(0xFFeaaa08),
                    subjectDescription = R.string.txt_economics_is_the_study_of_the_production_distribution_and_consumption_of_goods_and_services
                ),
                SubjectModel(
                    id = 11,
                    subjectName = R.string.txt_engineering_and_technology,
                    iconRes = R.drawable.ic_engineering_and_technology,
                    color = Color(0xFF2970ff),
                    subjectDescription = R.string.txt_engineering_and_technology_involves_applying_scientific_knowledge_to_develop_solutions_for_practical_problems
                ),
                SubjectModel(
                    id = 12,
                    subjectName = R.string.txt_environmental_studies_and_forestry,
                    iconRes = R.drawable.ic_environmental_studies_and_forestry,
                    color = Color(0xFF7a5af8),
                    subjectDescription = R.string.txt_environmental_studies_and_forestry_focus_on_the_protection_and_management_of_natural_resources_and_ecosystems
                ),
                SubjectModel(
                    id = 13,
                    subjectName = R.string.txt_family_and_consumer_science,
                    iconRes = R.drawable.ic_family_and_consumer_science,
                    color = Color(0xFFeaaa09),
                    subjectDescription = R.string.txt_family_and_consumer_science_is_the_study_of_family_dynamics_nutrition_and_consumer_behavior
                ),
                SubjectModel(
                    id = 14,
                    subjectName = R.string.txt_geography,
                    iconRes = R.drawable.ic_geography,
                    color = Color(0xFFeaaa09),
                    subjectDescription = R.string.txt_geography_is_the_study_of_places_landscapes_and_the_human_environment_relationship
                ),
                SubjectModel(
                    id = 15,
                    subjectName = R.string.txt_history,
                    iconRes = R.drawable.ic_history,
                    color = Color(0xFFf63d68),
                    subjectDescription = R.string.txt_history_is_the_study_of_past_events_civilizations_and_their_impact_on_the_present
                ),
                SubjectModel(
                    id = 16,
                    subjectName = R.string.txt_human_physical_performance_and_recreation,
                    iconRes = R.drawable.ic_human_physical_performance_and_recreation,
                    color = Color(0xFF7a5af8),
                    subjectDescription = R.string.txt_human_physical_performance_and_recreation_focuses_on_physical_activities_sports_and_overall_well_being
                ),
                SubjectModel(
                    id = 17,
                    subjectName = R.string.txt_interdisciplinary_studies,
                    iconRes = R.drawable.ic_interdisciplinary_studies,
                    color = Color(0xFF9076f8),
                    subjectDescription = R.string.txt_interdisciplinary_studies_involve_integrating_knowledge_from_multiple_academic_disciplines
                ),
                SubjectModel(
                    id = 18,
                    subjectName = R.string.txt_journalism_media_studies_and_communication,
                    iconRes = R.drawable.ic_journalism__media_studies_and_communication,
                    color = Color(0xFF7a5af8),
                    subjectDescription = R.string.txt_journalism_and_media_studies_explore_communication_reporting_and_media_s_role_in_society
                ),
                SubjectModel(
                    id = 19,
                    subjectName = R.string.txt_languages_and_cultures,
                    iconRes = R.drawable.ic_languages_and_cultures,
                    color = Color(0xFFd444f1),
                    subjectDescription = R.string.txt_languages_and_cultures_focus_on_linguistics_cultural_practices_and_communication_across_societies
                ),
                SubjectModel(
                    id = 20,
                    subjectName = R.string.txt_law,
                    iconRes = R.drawable.ic_law,
                    color = Color(0xFF7a5af8),
                    subjectDescription = R.string.txt_law_is_the_study_of_legal_systems_regulations_and_justice
                ),
                SubjectModel(
                    id = 21,
                    subjectName = R.string.txt_library_and_museum_studies,
                    iconRes = R.drawable.ic_library_and_museum_studies,
                    color = Color(0xFFf63d68),
                    subjectDescription = R.string.txt_library_and_museum_studies_focus_on_the_preservation_and_organization_of_information_and_artifacts
                ),
                SubjectModel(
                    id = 22,
                    subjectName = R.string.txt_literature,
                    iconRes = R.drawable.ic_literature,
                    color = Color(0xFFf63d68),
                    subjectDescription = R.string.txt_literature_is_the_study_of_written_works_such_as_novels_poetry_and_plays
                ),
                SubjectModel(
                    id = 23,
                    subjectName = R.string.txt_logic,
                    iconRes = R.drawable.ic_logic,
                    color = Color(0xFF4180fe),
                    subjectDescription = R.string.txt_logic_is_the_study_of_reasoning_critical_thinking_and_problem_solving_methods
                ),
                SubjectModel(
                    id = 24,
                    subjectName = R.string.txt_mathematics,
                    iconRes = R.drawable.ic_mathematics,
                    color = Color(0xFF2970ff),
                    subjectDescription = R.string.txt_mathematics_is_the_study_of_numbers_quantities_shapes_and_patterns
                ),
                SubjectModel(
                    id = 25,
                    subjectName = R.string.txt_medicine,
                    iconRes = R.drawable.ic_medicine,
                    color = Color(0xFF7a5af8),
                    subjectDescription = R.string.txt_medicine_is_the_study_of_health_diseases_and_medical_practices
                ),
                SubjectModel(
                    id = 26,
                    subjectName = R.string.txt_military_sciences,
                    iconRes = R.drawable.ic_military_sciences,
                    color = Color(0xFF7a5af8),
                    subjectDescription = R.string.txt_military_sciences_study_military_tactics_defense_strategies_and_security_systems
                ),
                SubjectModel(
                    id = 27,
                    subjectName = R.string.txt_other,
                    iconRes = R.drawable.ic_other,
                    color = Color(0xFF66c61c),
                    subjectDescription = R.string.txt_other_fields_of_study_that_do_not_fit_into_traditional_academic_categories
                ),
                SubjectModel(
                    id = 28,
                    subjectName = R.string.txt_philosophy,
                    iconRes = R.drawable.ic_philosophy,
                    color = Color(0xFFf63d68),
                    subjectDescription = R.string.txt_philosophy_is_the_study_of_existence_knowledge_and_ethics
                ),
                SubjectModel(
                    id = 29,
                    subjectName = R.string.txt_physics,
                    iconRes = R.drawable.ic_physics,
                    color = Color(0xFF17b89f),
                    subjectDescription = R.string.txt_physics_is_the_study_of_matter_energy_and_the_laws_governing_the_universe
                ),
                SubjectModel(
                    id = 30,
                    subjectName = R.string.txt_political_science,
                    iconRes = R.drawable.ic_political_science,
                    color = Color(0xFFeaaa08),
                    subjectDescription = R.string.txt_political_science_is_the_study_of_government_systems_policies_and_political_behavior
                ),
                SubjectModel(
                    id = 31,
                    subjectName = R.string.txt_psychology,
                    iconRes = R.drawable.ic_psychology,
                    color = Color(0xFFeaaa08),
                    subjectDescription = R.string.txt_psychology_is_the_study_of_the_human_mind_behavior_and_mental_processes
                ),
                SubjectModel(
                    id = 32,
                    subjectName = R.string.txt_public_administration,
                    iconRes = R.drawable.ic_public_administration,
                    color = Color(0xFFeaaa08),
                    subjectDescription = R.string.txt_public_administration_involves_the_management_of_government_policies_and_programs
                ),
                SubjectModel(
                    id = 33,
                    subjectName = R.string.txt_religion_and_divinity,
                    iconRes = R.drawable.ic_religion_and_divinity,
                    color = Color(0xFFf63d68),
                    subjectDescription = R.string.txt_religion_and_divinity_focus_on_the_study_of_faith_spirituality_and_religious_beliefs
                ),
                SubjectModel(
                    id = 34,
                    subjectName = R.string.txt_social_work,
                    iconRes = R.drawable.ic_social_work,
                    color = Color(0xFFeaaa09),
                    subjectDescription = R.string.txt_social_work_is_the_study_of_helping_individuals_families_and_communities_improve_their_well_being
                ),
                SubjectModel(
                    id = 35,
                    subjectName = R.string.txt_sociology,
                    iconRes = R.drawable.ic_sociology,
                    color = Color(0xFFeaaa09),
                    subjectDescription = R.string.txt_sociology_is_the_study_of_human_societies_social_behavior_and_social_structures
                ),
                SubjectModel(
                    id = 36,
                    subjectName = R.string.txt_space_sciences,
                    iconRes = R.drawable.ic_space_sciences,
                    color = Color(0xFF1ab8a0),
                    subjectDescription = R.string.txt_space_sciences_explore_the_study_of_the_universe_celestial_bodies_and_cosmic_phenomena
                ),
                SubjectModel(
                    id = 37,
                    subjectName = R.string.txt_systems_science,
                    iconRes = R.drawable.ic_systems_science,
                    color = Color(0xFFf64b73),
                    subjectDescription = R.string.txt_systems_science_is_the_study_of_complex_systems_such_as_ecosystems_organizations_and_technologies
                ),
                SubjectModel(
                    id = 38,
                    subjectName = R.string.txt_transportation,
                    iconRes = R.drawable.ic_transportation,
                    color = Color(0xFF8d72f8),
                    subjectDescription = R.string.txt_transportation_is_the_study_of_the_movement_of_people_and_goods_and_the_infrastructure_that_supports_it
                )
            )

    }
}
