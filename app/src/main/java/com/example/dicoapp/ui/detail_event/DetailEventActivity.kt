package com.example.dicoapp.ui.detail_event

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.dicoapp.R
import com.example.dicoapp.databinding.ActivityDetailEventBinding
import com.example.dicoapp.ui.ViewModelFactory
import com.example.dicoapp.utils.formatEventDateRange

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    private val viewModel: DetailEventViewModel by viewModels() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Event"

        val eventId = intent.getStringExtra(EXTRA_EVENT_ID)

        if (eventId != "") {
            viewModel.getDetailEvent(eventId.toString())
        }
        observeViewModel()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun observeViewModel() {
        viewModel.dataEvent.observe(this) { events ->
            with(binding) {
                toolbar.setNavigationOnClickListener {
                    onBackPressedDispatcher.onBackPressed()
                }
                Glide.with(this@DetailEventActivity)
                    .load(events.mediaCover)
                    .into(ivEventImage)
                tvEventInfo.text = getString(R.string.tv_event_info)
                tvEventName.text = events.name
                tvEventOwner.text = getString(R.string.tv_event_owner, events.ownerName)
                tvEventCategory.text = events.category
                rowDate.tvRowTitle.text = getString(R.string.row_title_date_time)
                rowDate.tvRowValue.text = formatEventDateRange(events.beginTime, events.endTime)
                rowLocation.imgRow.setImageResource(R.drawable.ic_location)
                rowLocation.tvRowTitle.text = getString(R.string.row_title_location)
                rowLocation.tvRowValue.text = events.cityName
                rowQuota.imgRow.setImageResource(R.drawable.ic_people)
                rowQuota.tvRowTitle.text = getString(R.string.row_title_remaining_quota)
                rowQuota.tvRowValue.text =
                    getString(
                        R.string.tv_event_quota,
                        (events.quota - events.registrants).toString()
                    )

                tvDescription.text = HtmlCompat.fromHtml(
                    events.description,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )

                btnRegister.setOnClickListener {
                    if (events.link.isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = events.link.toUri()
                        }
                        startActivity(intent)
                    }
                }
            }
        }

        viewModel.snackBarText.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading)
                binding.progressBar.visibility = View.VISIBLE
            else
                binding.progressBar.visibility = View.INVISIBLE
        }

        viewModel.snackBarText.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object {
        //        private const val TAG = "DetailEventActivity"
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
}