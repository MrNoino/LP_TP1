package com.lp.tp1.backend.requests

import com.google.gson.annotations.SerializedName

data class SwitchboardResponse(
    @SerializedName("blueprint_filename") var blueprintFilename: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("switches") var switches: ArrayList<Switches> = arrayListOf()
) {
    data class Switches(
        @SerializedName("blueprint_room_position") var blueprintRoomPosition: BlueprintRoomPosition? = BlueprintRoomPosition(),
        @SerializedName("position") var position: Int? = null,
        @SerializedName("room") var room: String? = null
    ) {
        data class BlueprintRoomPosition(
            @SerializedName("bottom_left") var bottomLeft: BottomLeft? = BottomLeft(),
            @SerializedName("bottom_right") var bottomRight: BottomRight? = BottomRight(),
            @SerializedName("top_left") var topLeft: TopLeft? = TopLeft(),
            @SerializedName("top_right") var topRight: TopRight? = TopRight()

        ) {
            data class TopRight(
                @SerializedName("x") var x: Int? = null,
                @SerializedName("y") var y: Int? = null
            )

            data class TopLeft(
                @SerializedName("x") var x: Int? = null,
                @SerializedName("y") var y: Int? = null
            )

            data class BottomRight(
                @SerializedName("x") var x: Int? = null,
                @SerializedName("y") var y: Int? = null
            )

            data class BottomLeft(
                @SerializedName("x") var x: Int? = null,
                @SerializedName("y") var y: Int? = null
            )
        }
    }
}