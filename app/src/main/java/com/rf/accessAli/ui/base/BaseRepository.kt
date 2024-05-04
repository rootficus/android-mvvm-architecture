package com.rf.accessAli.ui.base

import android.content.Context
import android.util.Log
import com.rf.accessAli.R
import com.rf.accessAli.roomDB.model.UniversityData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Objects


/**
 * Akash.Singh
 * RootFicus.
 */
abstract class BaseRepository {
    fun execute(
        call: Call<List<UniversityData>>,
        context: Context,
        success: (List<UniversityData>) -> Unit,
        fail: (String) -> Unit
    ) {
        call.enqueue(object : Callback<List<UniversityData>> {
            override fun onResponse(
                call: Call<List<UniversityData>>,
                response: Response<List<UniversityData>>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        success.invoke(data)
                    } else {
                        fail.invoke(context.getString(R.string.error_something_went_wrong))
                    }
                } else {
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                }
            }

            override fun onFailure(call: Call<List<UniversityData>>, t: Throwable) {
                if (t.toString().contains("failed to connect")) {
                    fail.invoke(context.getString(R.string.no_network))
                } else {
                    fail.invoke(context.getString(R.string.error_something_went_wrong))
                    Log.i("Error Something", "::$t")
                }
            }
        })
    }

}