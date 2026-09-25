package com.example.dicoapp.ui.detail_event

import android.annotation.SuppressLint
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
import com.example.dicoapp.utils.formatEventDateRange

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding

    private val viewModel: DetailEventViewModel by viewModels()

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

    @SuppressLint("SetTextI18n")
    private fun observeViewModel() {
        viewModel.dataEvent.observe(this) { events ->
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            Glide.with(this)
                .load(events.mediaCover)
                .into(binding.ivEventImage)
            binding.tvEventInfo.text = getString(R.string.tv_event_info)
            binding.tvEventSummary.text = getString(R.string.tv_event_summary)
            binding.tvEventName.text = events.name
            binding.tvEventOwner.text = "by " + events.ownerName
            binding.tvEventQuota.text =
                events.quota.toString() + " (" + (events.quota - events.registrants) + "available)"
            binding.tvEventCategory.text = events.category
            binding.tvEventLocation.text = events.cityName
            binding.tvSummary.text = events.summary
            binding.tvEventTime.text = formatEventDateRange(events.beginTime, events.endTime)
            binding.tvDescription.text = HtmlCompat.fromHtml(
                events.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            binding.btnRegister.setOnClickListener {
                if (events.link.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = events.link.toUri()
                    }
                    startActivity(intent)
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