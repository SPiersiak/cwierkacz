package com.example.cwierkaczapp.listeners

import androidx.recyclerview.widget.RecyclerView
import com.example.cwierkaczapp.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TwitterListenerImpl(val tweetList: RecyclerView,var user:User?, val callback: HomeCallback?):TweetListener {

    protected val firebaseDB=FirebaseFirestore.getInstance()
    private val userId=FirebaseAuth.getInstance().currentUser?.uid

    override fun onLayoutClick(tweet: Tweet?) {

    }

    override fun onLike(tweet: Tweet?) {
        tweet?.let {
            tweetList.isClickable = false
            val likes = tweet.likes
            if (tweet.likes?.contains(userId) == true) {
                likes?.remove(userId)
            } else {
                likes?.add(userId!!)
            }
            firebaseDB.collection(DATA_TWEETS).document(tweet.tweetId!!).update(DATA_TWEETS_LIKES, likes)
                .addOnSuccessListener {
                    tweetList.isClickable = true
                    callback?.onRefresh()
                }
                .addOnFailureListener {
                    tweetList.isClickable = true
                }
        }
    }

    override fun onRetweet(tweet: Tweet?) {

        tweet?.let {
            tweetList.isClickable = false
            val retweets = tweet.userIds
            if (retweets?.contains(userId) == true) {
                retweets?.remove(userId)
            } else {
                retweets?.add(userId!!)
            }
            firebaseDB.collection(DATA_TWEETS).document(tweet.tweetId!!).update(DATA_TWEET_USER_IDS, retweets)
                .addOnSuccessListener {
                    tweetList.isClickable = true
                    callback?.onRefresh()
                }
                .addOnFailureListener {
                    tweetList.isClickable = true
                }
        }
    }
}