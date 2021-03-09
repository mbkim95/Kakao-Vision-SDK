package com.example.kakaovisionsdk.friend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kakaovisionsdk.R
import com.example.kakaovisionsdk.databinding.ActivityFriendsBinding

class FriendsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFriendsBinding
    private lateinit var adapter: FriendsAdapter
    private lateinit var resultReceiver: ResultReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val friends = intent.getParcelableArrayListExtra<FriendItem>("items")
        resultReceiver = intent.getParcelableExtra("resultReceiver")!!

        adapter = FriendsAdapter(friends!!.toList())

        binding.friendsRv.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@FriendsActivity.adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.friend_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_send -> {
                val selectedFriends =
                    adapter.friends.filter { it.isChecked }.map { it.friendInfo.uuid }
                val bundle = Bundle()
                bundle.putStringArrayList("items", ArrayList(selectedFriends))
                resultReceiver.send(Activity.RESULT_OK, bundle)
                finish()
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        resultReceiver.send(Activity.RESULT_CANCELED, Bundle())
    }

    companion object {
        fun startForResult(
            context: Context,
            items: List<FriendItem>,
            callback: (selectedItems: List<String>) -> Unit
        ) {
            val resultReceiver = object : ResultReceiver(Handler(Looper.getMainLooper())) {
                override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                    callback(resultData?.getStringArrayList("items").orEmpty())
                }
            }
            context.startActivity(
                Intent(context, FriendsActivity::class.java)
                    .putParcelableArrayListExtra("items", ArrayList<FriendItem>(items))
                    .putExtra("resultReceiver", resultReceiver)
            )
        }
    }
}