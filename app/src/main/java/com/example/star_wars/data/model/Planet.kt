package com.example.star_wars.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Planet(
    @PrimaryKey (autoGenerate = true)
    val id: Int=0,
    val name: String="",
    val rotation_period: Int=0,
    val orbital_period: Int=0,
    val climate: String="",
    val terrain: String="",
    val discovery_date: String="",
    val is_colonized: Boolean=false,
    val user_id: Int=0
): Parcelable{
    companion object {
        fun empty(): Planet {
            return Planet(
                id = 0,
                name = "",
                rotation_period = 0,
                orbital_period = 0,
                climate = "",
                terrain = "",
                discovery_date = "",
                is_colonized = false,
                user_id = 0
            )
        }
    }
}