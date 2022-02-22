package com.scand.internship.mars_scout.models

enum class BlockType {
    GROUND, SAND, PIT, HILL;

    companion object{
        fun setMapBlockTypeAccordingToUIMapBlockDesc(type: String) : BlockType? {
            return when {
                type.contains("GROUND", true) -> GROUND
                type.contains("SAND", true) -> SAND
                type.contains("HILL", true) -> HILL
                type.contains("PIT", true) -> PIT
                else -> null
            }
        }
    }
}
