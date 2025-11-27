package com.example.bloodlink.data.model.enums

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserRoleDeserializer : JsonDeserializer<UserRole> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): UserRole {
        Log.d("UserRoleDeserializer", "=== DESERIALIZING ROLE ===")
        Log.d("UserRoleDeserializer", "JSON element: $json")
        Log.d("UserRoleDeserializer", "JSON as string: ${json?.asString}")
        
        val roleString = json?.asString ?: run {
            Log.e("UserRoleDeserializer", "Role is null!")
            throw IllegalArgumentException("Role cannot be null")
        }
        
        Log.d("UserRoleDeserializer", "Attempting to convert: '$roleString'")
        val role = UserRole.fromString(roleString)
        
        if (role == null) {
            Log.e("UserRoleDeserializer", "Unknown role: $roleString")
            Log.e("UserRoleDeserializer", "Available roles: ${UserRole.entries.map { it.value }}")
            throw IllegalArgumentException("Unknown role: $roleString")
        }
        
        Log.d("UserRoleDeserializer", "Successfully converted to: $role")
        return role
    }
}
