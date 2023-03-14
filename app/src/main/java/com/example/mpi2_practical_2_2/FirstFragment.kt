package com.example.mpi2_practical_2_2

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mpi2_practical_2_2.databinding.FragmentFirstBinding
import okhttp3.*
import java.io.IOException
import kotlinx.android.synthetic.main.fragment_first.*
import org.json.JSONObject
import org.json.JSONTokener


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {


    private var _binding: FragmentFirstBinding? = null
    private val client = OkHttpClient()
    private val jokes = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response){
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONObject
                val jsonArray = jsonObject.getJSONArray("jokes")
                for (i in 0 until jsonArray.length()) {
                    jokes.add(jsonArray.getJSONObject(i).getString("joke"))
                }

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        val arrayAdapter: ArrayAdapter<*>
        run("https://v2.jokeapi.dev/joke/Any?blacklistFlags=explicit&type=single&amount=10")
        Thread.sleep(400)
        arrayAdapter = ArrayAdapter(requireContext().applicationContext, android.R.layout.simple_list_item_1, jokes)
        api_list_view?.adapter = arrayAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}