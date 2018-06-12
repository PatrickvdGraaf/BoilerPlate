package com.crepetete.basemvpproject.ui.post

import com.crepetete.basemvpproject.R
import com.crepetete.basemvpproject.network.PostApi
import com.crepetete.basemvpproject.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * The Presenter that will present the Post view.
 * @param postView the Post view to be presented by the presenter
 * @property postApi the API interface implementation
 * @property context the context in which the application is running
 * @property subscription the subscription to the API call
 */
class PostPresenter(postView: PostView) : BasePresenter<PostView>(postView) {
    @Inject
    lateinit var postApi: PostApi

    private var subscription: Disposable? = null

    override fun onViewCreated() {
        loadPosts()
    }

    fun loadPosts() {
        view.showLoading()
        subscription = postApi
                .getPosts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnTerminate {
                    view.hideLoading() }
                .subscribe(
                        { postList ->
                            view.updatePosts(postList) },
                        {
                            view.showError(R.string.unknown_error) }
                )
    }

    override fun onViewDestroyed() {
        subscription?.dispose()
    }
}