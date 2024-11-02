package self.adragon.aviarouteanalyse.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import self.adragon.aviarouteanalyse.R
import self.adragon.aviarouteanalyse.ui.viewmodels.FlightViewModel

class FlightAnalysisFragment : Fragment(R.layout.flight_analysis) {

    private val flightViewModel: FlightViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}
