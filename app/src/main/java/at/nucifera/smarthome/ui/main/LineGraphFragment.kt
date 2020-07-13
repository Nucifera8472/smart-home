package at.nucifera.smarthome.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import at.nucifera.smarthome.R

class LineGraphFragment : Fragment() {

    companion object {
        fun newInstance() = LineGraphFragment()
    }

    private lateinit var viewModel: LineGraphViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.line_graph_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LineGraphViewModel::class.java)


        viewModel.debugText.observe(viewLifecycleOwner, Observer {
            view?.findViewById<TextView>(R.id.debug_text)?.text = it
        })

        val data = viewModel.prepareRawData()

    }

}
