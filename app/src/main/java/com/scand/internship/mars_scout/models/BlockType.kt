package com.scand.internship.mars_scout.models

enum class BlockType {
    GROUND, SAND, PIT, HILL;

    companion object{
        fun setMapBlockTypeAccordingToUIMapBlockDesc(type: String) : BlockType{
            return when {
                type.contains("SAND", true) -> BlockType.SAND
                type.contains("HILL", true) -> BlockType.HILL
                type.contains("PIT", true) -> BlockType.PIT
                else -> BlockType.GROUND
            }
        }
    }
}
