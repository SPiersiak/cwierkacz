package com.example.cwierkaczapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cwierkaczapp.R
import com.example.cwierkaczapp.adapters.TweetListAdapter
import com.example.cwierkaczapp.listeners.TweetListener
import com.example.cwierkaczapp.util.DATA_TWEETS
import com.example.cwierkaczapp.util.DATA_TWEET_HASHTAGS
import com.example.cwierkaczapp.util.Tweet
import com.example.cwierkaczapp.util.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : TwitterFragment() {

    private var currentHashtag = ""
    private var tweetsAdapter: TweetListAdapter? = null
    private var currentUser: User? = null
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val listener:TweetListener?=null

    private var hashtagFollowed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tweetsAdapter = TweetListAdapter(userId!!, arrayListOf())
        tweetsAdapter?.setListener(listener)
        tweetList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tweetsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            updateList()
        }

    }

    fun newHashtag(term: String) {
        currentHashtag = term
        followHashtag.visibility = View.VISIBLE
        updateList()
    }

    fun updateList() {
        tweetList?.visibility = View.GONE
        firebaseDB.collection(DATA_TWEETS).whereArrayContains(DATA_TWEET_HASHTAGS, currentHashtag)
            .get()
            .addOnSuccessListener { list ->
                tweetList?.visibility = View.VISIBLE
                val tweets = arrayListOf<Tweet>()
                for (document in list.documents) {
                    val tweet = document.toObject(Tweet::class.java)
                    tweet?.let { tweets.add(it) }
                }
                val sortedTweets = tweets.sortedWith(compareByDescending { it.timestamp })
                tweetsAdapter?.updateTweets(sortedTweets)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }

    }
}
