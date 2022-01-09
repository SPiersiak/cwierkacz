package com.example.cwierkaczapp.listeners

import com.example.cwierkaczapp.util.Tweet

interface TweetListener {
    fun onLayoutClick(tweet: Tweet?)
    fun onLike(tweet: Tweet?)
    fun onRetweet(tweet: Tweet?)
}