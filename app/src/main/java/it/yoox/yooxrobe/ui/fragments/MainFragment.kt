package it.yoox.yooxrobe.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.common.GeoCoordinate
import it.yoox.yooxrobe.R
import it.yoox.yooxrobe.ui.viewmodels.MainViewModel
import org.greenrobot.eventbus.EventBus
import it.yoox.yooxrobe.components.events.MessageEvent
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import com.here.android.mpa.common.OnEngineInitListener
import com.here.android.mpa.common.PositioningManager
import com.here.android.mpa.mapping.SupportMapFragment
import com.here.android.mpa.mapping.Map
import android.location.LocationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.android.synthetic.main.main_fragment.*
import it.yoox.yooxrobe.ui.activities.PhotoSearchActivity


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        action_photo_search.setOnClickListener {
            startActivity(Intent(requireActivity(), PhotoSearchActivity::class.java))
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        /* Do something */
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

}
