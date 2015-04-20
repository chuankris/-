package com.example.wandaojiaindex;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings.RenderPriority;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wandaojiaindex.MyScrollView.OnScrollChangedListener;

/**
 * 
 * @author chuankris
 * 
 */
public class MainActivity extends Activity implements OnScrollChangedListener, OnTouchListener, OnClickListener
{

	private ViewPager mMainVewPager;

	private MyScrollView mSvMain;
	private View mVHeaderContainer;
	private View mVSearchBar;

	private View mVMainNav;
	private View mVMainNavLeftContainer;
	private RadioGroup mVMNavMidTGroup;
	private View mVMNavRightContainer;
	private RadioButton mVMainNavIndex;
	private RadioButton mVMainNavDiscover;
	private RadioButton mVMainNavBlog;
	private RadioButton mVMainNavMine;
	private RadioButton mVMainNavMore;

	private View mVIndexHeaderNav;
	private View mVIndexNavDiscover;
	private View mVIndexNavBlog;
	private View mVIndexNavMine;
	private View mVIndexNavMore;
	private ImageView mIvIndexNavDiscover;
	private ImageView mIvIndexNavBlog;
	private ImageView mIvIndexNavMine;
	private ImageView mIvIndexNavMore;
	private TextView mTvIndexNavDiscover;
	private TextView mTvIndexNavBlog;
	private TextView mTvIndexNavMine;
	private TextView mTvIndexNavMore;

	private View myView = null;
	private WebView mWebView;
	private Context mContext;
	private String url = "http://www.baidu.com";
	private WebViewClient mWebViewClient = new WebViewClient()
	{
		String cnaCookie = null;

		// 处理页面导航
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			if (url.startsWith("http://h5.m.taobao.com/awp/core/detail.htm"))
			{
				mWebView.loadUrl(url);
			}
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			Log.e("qxc", "onPageFinished");
			super.onPageFinished(view, url);

		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			Log.e("qxc", "onpagestarted");
			super.onPageStarted(view, url, favicon);

		}
	};

	final class InJavaScriptLocalObj
	{
		public void showSource(String html)
		{
			Log.e("qxc", "html = " + html);

			Toast.makeText(mContext, html, Toast.LENGTH_LONG).show();
		}
	}

	// 浏览网页历史记录
	// goBack()和goForward()
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			mChromeClient.onHideCustomView();
		}
		return super.onKeyDown(keyCode, event);
	}

	private WebChromeClient mChromeClient = new WebChromeClient()
	{

		private CustomViewCallback myCallback = null;

		// 配置权限 （在WebChromeClinet中实现）
		@Override
		public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback)
		{
			callback.invoke(origin, true, false);
			super.onGeolocationPermissionsShowPrompt(origin, callback);
		}

		// 扩充数据库的容量（在WebChromeClinet中实现）
		@Override
		public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota,
		        WebStorage.QuotaUpdater quotaUpdater)
		{

			quotaUpdater.updateQuota(estimatedSize * 2);
		}

		// 扩充缓存的容量
		@Override
		public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater)
		{

			quotaUpdater.updateQuota(spaceNeeded * 2);
		}

		// Android 使WebView支持HTML5 Video（全屏）播放的方法
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback)
		{
			if (myCallback != null)
			{
				myCallback.onCustomViewHidden();
				myCallback = null;
				return;
			}

			ViewGroup parent = (ViewGroup) mWebView.getParent();
			parent.removeView(mWebView);
			parent.addView(view);
			myView = view;
			myCallback = callback;
			mChromeClient = this;
		}

		@Override
		public void onHideCustomView()
		{
			if (myView != null)
			{
				if (myCallback != null)
				{
					myCallback.onCustomViewHidden();
					myCallback = null;
				}

				ViewGroup parent = (ViewGroup) myView.getParent();
				parent.removeView(myView);
				parent.addView(mWebView);
				myView = null;
			}
		}
	};

	private void initwebview()
	{
		mWebView.setWebChromeClient(mChromeClient);
		mWebView.setWebViewClient(mWebViewClient);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setAppCacheEnabled(true);
		//	String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		//Log.i("TAG", "appCacheDir==" + appCacheDir);
		//mWebView.getSettings().setAppCachePath(appCacheDir);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 1);// 设置缓冲大小，我设的是10M
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);

		mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
		mWebView.getSettings().setBuiltInZoomControls(true);
		//            mWebView.getSettings().setDisplayZoomControls(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
		String cnaCookie = null;
		try
		{
			mWebView.loadUrl(url);
			//mWebView.loadUrl(url);
		}
		catch (Exception e)
		{

			finish();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		initView();
		initwebview();
	}

	private void initView()
	{
		mSvMain = (MyScrollView) findViewById(R.id.main_sv);
		mMainVewPager = (ViewPager) findViewById(R.id.main_viewPager);

		mVHeaderContainer = findViewById(R.id.header_container);

		mVIndexHeaderNav = ViewFinder.findViewById(mVHeaderContainer, R.id.index_header_nav);
		mVIndexNavDiscover = ViewFinder.findViewById(mVIndexHeaderNav, R.id.discover_item);
		mVIndexNavBlog = ViewFinder.findViewById(mVIndexHeaderNav, R.id.blog_item);
		mVIndexNavMine = ViewFinder.findViewById(mVIndexHeaderNav, R.id.mine_item);
		mVIndexNavMore = ViewFinder.findViewById(mVIndexHeaderNav, R.id.more_item);

		mTvIndexNavDiscover = ViewFinder.findViewById(mVIndexNavDiscover, R.id.nav_text_tv);
		mTvIndexNavBlog = ViewFinder.findViewById(mVIndexNavBlog, R.id.nav_text_tv);
		mTvIndexNavMine = ViewFinder.findViewById(mVIndexNavMine, R.id.nav_text_tv);
		mTvIndexNavMore = ViewFinder.findViewById(mVIndexNavMore, R.id.nav_text_tv);
		mIvIndexNavDiscover = ViewFinder.findViewById(mVIndexNavDiscover, R.id.nav_icon_iv);
		mIvIndexNavBlog = ViewFinder.findViewById(mVIndexNavBlog, R.id.nav_icon_iv);
		mIvIndexNavMine = ViewFinder.findViewById(mVIndexNavMine, R.id.nav_icon_iv);
		mIvIndexNavMore = ViewFinder.findViewById(mVIndexNavMore, R.id.nav_icon_iv);

		mVSearchBar = ViewFinder.findViewById(mVHeaderContainer, R.id.search_bar);
		mWebView = (WebView) findViewById(R.id.test);
		mVMainNav = findViewById(R.id.main_nav);
		mVMNavRightContainer = ViewFinder.findViewById(mVMainNav, R.id.right_container);
		mVMainNavLeftContainer = ViewFinder.findViewById(mVMainNav, R.id.main_nav_layout);
		mVMNavMidTGroup = ViewFinder.findViewById(mVMainNav, R.id.middle_tab_group);
		mVMainNavIndex = ViewFinder.findViewById(mVMainNav, R.id.main_tab_index);
		mVMainNavDiscover = ViewFinder.findViewById(mVMainNav, R.id.main_tab_ranking);
		mVMainNavBlog = ViewFinder.findViewById(mVMainNav, R.id.main_tab_blog);
		mVMainNavMine = ViewFinder.findViewById(mVMainNav, R.id.main_tab_mine);
		mVMainNavMore = ViewFinder.findViewById(mVMainNav, R.id.main_tab_more);

		mIvIndexNavDiscover.setBackgroundResource(R.drawable.cached_icon_taobao);
		mIvIndexNavBlog.setBackgroundResource(R.drawable.cached_icon_tianmao);
		mIvIndexNavMine.setBackgroundResource(R.drawable.community_icon);
		mIvIndexNavMore.setBackgroundResource(R.drawable.cached_icon_qqreader);
		mVMainNavIndex.setOnClickListener(this);
		mVMainNavDiscover.setOnClickListener(this);
		mVMainNavBlog.setOnClickListener(this);
		mVMainNavMine.setOnClickListener(this);
		mVMainNavMore.setOnClickListener(this);

		mSvMain.setOnScrollChangedListener(this);
		mSvMain.setOnTouchListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.main_tab_index:
			mSvMain.setVisibility(View.VISIBLE);
			mMainVewPager.setVisibility(View.GONE);
			RadioButton rbChild;
			for (int childIndex = 0; childIndex < mVMNavMidTGroup.getChildCount(); childIndex++)
			{
				rbChild = (RadioButton) mVMNavMidTGroup.getChildAt(childIndex);
				rbChild.setChecked(false);
			}

			break;
		case R.id.main_tab_ranking:
			mSvMain.setVisibility(View.GONE);
			mVMainNavIndex.setChecked(false);
			mVMainNavDiscover.setChecked(true);
			mMainVewPager.setVisibility(View.VISIBLE);
			break;
		}
	}

	private boolean mIsInitFactor = false;

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		if (!mIsInitFactor)
		{
			int mMainNavHeight = mVMainNav.getBottom();
			int mSearchBarTop = mVSearchBar.getTop();
			int mSearchBarButtom = mVSearchBar.getBottom();
			int mIndexNavTop = mVIndexHeaderNav.getTop();
			int mTextFadeDis = getResources().getDimensionPixelSize(R.dimen.app_padding);
			int mVMainNavIndexWidth = mVMainNavIndex.getWidth();
			int mVIndexHeaderNavBottom = mVIndexHeaderNav.getBottom();
			int mMNRightContainerWidth = mVMNavRightContainer.getWidth();

			mMaxScrollTop = mVIndexHeaderNavBottom;

			mTextFadeEndScrollTop = (int) (mSearchBarTop - mMainNavHeight * 0.5f);
			mTextFadeBeginScrollTop = mTextFadeEndScrollTop - mTextFadeDis;
			mTextFadeFactor = 255.0f / mTextFadeDis;
			mCurrentTextAlpha = 255;

			mMainNavFadeBeginScrollTop = mTextFadeEndScrollTop;
			mMainNavFadeEndScrollTop = mMainNavFadeBeginScrollTop + mMainNavHeight;
			mCurrentMainNavAlpha = 0.0f;
			mMainNavFadeFactor = 1.0f / mMainNavHeight;
			changeMainNavAlpha(0);

			mIndexNavZoomBeginScrollTop = mSearchBarButtom - mMainNavHeight;
			mIndexNavZoomEndScrollTop = mIndexNavTop - mMainNavHeight;
			float zoomDistance = mIndexNavZoomEndScrollTop - mIndexNavZoomBeginScrollTop;
			mFinalIndexNavPaddingLeft = mVMainNavIndex.getWidth();
			mFinalIndexNavPaddingRight = (mVIndexHeaderNav.getRight() - mVMainNavLeftContainer.getWidth());
			mIndexNavMoveLeftFactor = mFinalIndexNavPaddingLeft / zoomDistance;
			mIndexNavMoveRightFactor = mFinalIndexNavPaddingRight / zoomDistance;

			mMainNavTranBeginScrollTop = mIndexNavZoomEndScrollTop;
			mMainNavTranEndScrollTop = mIndexNavZoomEndScrollTop + mMainNavHeight;
			mOrginMNavIndexMarginLeft = -mVMainNavIndexWidth;
			mOrginMNavMidTGroupMarginTop = mMainNavHeight;
			mOrginMNavRightContainerMarginRight = -mMNRightContainerWidth;
			mMNavIndexFactor = mVMainNavIndexWidth / (float) mMainNavHeight;
			mMNavRightContainerFactor = mMNRightContainerWidth / (float) mMainNavHeight;
			mMNavMidTGroupFactor = 1.0f;
			mCurrentMNavIndexMarginLeft = mOrginMNavIndexMarginLeft;
			mCurrentMNavMidTGroupMarginTop = mOrginMNavMidTGroupMarginTop;
			mCurrentMNavRightContainerMarginRight = mOrginMNavRightContainerMarginRight;
			setLeftMargin(mVMainNavIndex, mCurrentMNavIndexMarginLeft);
			setRightMargin(mVMNavRightContainer, mOrginMNavRightContainerMarginRight);
			setTopMargin(mVMNavMidTGroup, mOrginMNavMidTGroupMarginTop);

			mIsInitFactor = true;
		}
	}

	private int mMaxScrollTop;
	private int mScrollerOldNewTop;

	@Override
	public void onScrollChanged(int newLeft, int newTop, int oldLeft, int oldTop)
	{

		if (mScrollerOldNewTop == newTop || oldTop > mMaxScrollTop)
		{
			return;
		}
		mScrollerOldNewTop = newTop;

		fadeTextOnScroll(newLeft, newTop, oldLeft, oldTop);

		fadeMainNavOnScroll(newLeft, newTop, oldLeft, oldTop);

		zoomIndexNavOnScroll(newLeft, newTop, oldLeft, oldTop);

		transferMainNavOnScroll(newLeft, newTop, oldLeft, oldTop);
	}

	private int mTextFadeBeginScrollTop;
	private int mTextFadeEndScrollTop;
	private float mTextFadeFactor;
	private int mCurrentTextAlpha;

	/**
	 * 文字随着活动逐步消失或者显示
	 */
	private void fadeTextOnScroll(int newLeft, int newTop, int oldLeft, int oldTop)
	{
		// 上滑上界
		if (newTop >= mTextFadeEndScrollTop)
		{
			mCurrentTextAlpha = 0;
			changeIndexNavTextAlpha(0);
			return;
		}

		// 下滑下界
		if (newTop <= mTextFadeBeginScrollTop)
		{
			mCurrentTextAlpha = 255;
			changeIndexNavTextAlpha(255);
			return;
		}

		// 上滑下交界
		if (mTextFadeBeginScrollTop < oldTop && newTop > mTextFadeBeginScrollTop)
		{
			mCurrentTextAlpha = (int) (255 - (newTop - mTextFadeBeginScrollTop) * mTextFadeFactor);
			ensureFadeTextAlphaRight(newTop, oldTop);
			changeIndexNavTextAlpha(mCurrentTextAlpha);
			return;
		}

		// 下滑上交界
		if (mTextFadeEndScrollTop < oldTop && newTop < mTextFadeEndScrollTop)
		{
			mCurrentTextAlpha = (int) ((mTextFadeEndScrollTop - oldTop) * mTextFadeFactor);
			ensureFadeTextAlphaRight(newTop, oldTop);
			changeIndexNavTextAlpha(mCurrentTextAlpha);
			return;
		}

		// 中间状态
		if (mTextFadeBeginScrollTop <= oldTop && oldTop <= mTextFadeEndScrollTop)
		{
			mCurrentTextAlpha = (int) (mCurrentTextAlpha - (newTop - oldTop) * mTextFadeFactor);
			ensureFadeTextAlphaRight(newTop, oldTop);
			changeIndexNavTextAlpha(mCurrentTextAlpha);
		}
	}

	private void ensureFadeTextAlphaRight(int newTop, int oldTop)
	{
		if (newTop > oldTop)
		{
			mCurrentTextAlpha = mCurrentTextAlpha < 0 ? 0 : mCurrentTextAlpha;
		}
		else
		{
			mCurrentTextAlpha = mCurrentTextAlpha > 255 ? 255 : mCurrentTextAlpha;
		}
	}

	private int mMainNavFadeBeginScrollTop;
	private int mMainNavFadeEndScrollTop;
	private float mMainNavFadeFactor;
	private float mCurrentMainNavAlpha;

	/**
	 * 主导航条消失或者显示
	 */
	private void fadeMainNavOnScroll(int newLeft, int newTop, int oldLeft, int oldTop)
	{
		// 上滑上界
		if (newTop >= mMainNavFadeEndScrollTop)
		{
			changeMainNavAlpha(1.0f);
			mCurrentMainNavAlpha = 1.0f;
			return;
		}
		// 下滑下界
		if (newTop <= mMainNavFadeBeginScrollTop)
		{
			changeMainNavAlpha(0.0f);
			mCurrentMainNavAlpha = 0.0f;
			return;
		}

		// 上滑下交界
		if (mMainNavFadeBeginScrollTop > oldTop && newTop > mMainNavFadeBeginScrollTop)
		{
			mCurrentMainNavAlpha = (newTop - mMainNavFadeBeginScrollTop) * mMainNavFadeFactor;
			System.out.println("newTop :" + newTop + ",oldTop :" + oldTop + "mCurrentMainNavAlpha :" + mCurrentMainNavAlpha);
			ensureFadeMainNavAlphaRight(newTop, oldTop);
			changeMainNavAlpha(mCurrentMainNavAlpha);
			return;
		}

		// 下滑上交界
		if (mMainNavFadeEndScrollTop < oldTop && newTop < mMainNavFadeEndScrollTop)
		{
			mCurrentMainNavAlpha = 1.0f - (mMainNavFadeEndScrollTop - newTop) * mMainNavFadeFactor;
			ensureFadeMainNavAlphaRight(newTop, oldTop);
			changeMainNavAlpha(mCurrentMainNavAlpha);
			return;
		}

		// 中间状态
		if (mMainNavFadeBeginScrollTop <= oldTop && oldTop <= mMainNavFadeEndScrollTop)
		{
			mCurrentMainNavAlpha = mCurrentMainNavAlpha + (newTop - oldTop) * mMainNavFadeFactor;
			ensureFadeMainNavAlphaRight(newTop, oldTop);
			changeMainNavAlpha(mCurrentMainNavAlpha);
		}
	}

	private void ensureFadeMainNavAlphaRight(int newTop, int oldTop)
	{
		if (newTop > oldTop)
		{
			mCurrentMainNavAlpha = mCurrentMainNavAlpha > 1 ? 1 : mCurrentMainNavAlpha;
		}
		else
		{
			mCurrentMainNavAlpha = mCurrentMainNavAlpha < 0 ? 0 : mCurrentMainNavAlpha;
		}
	}

	private int mIndexNavZoomBeginScrollTop;
	private int mIndexNavZoomEndScrollTop;
	private float mIndexNavMoveLeftFactor;
	private float mIndexNavMoveRightFactor;
	private int mFinalIndexNavPaddingLeft;
	private int mFinalIndexNavPaddingRight;
	private int mCurrentIndexNavPaddingLeft;
	private int mCurrentIndexNavPaddingRight;

	/**
	 * 缩放首页导航条
	 */
	private void zoomIndexNavOnScroll(int newLeft, int newTop, int oldLeft, int oldTop)
	{

		// 上滑上界
		if (newTop >= mIndexNavZoomEndScrollTop)
		{
			changeIndexNavPadding(mFinalIndexNavPaddingLeft, mFinalIndexNavPaddingRight);
			mCurrentIndexNavPaddingLeft = mFinalIndexNavPaddingLeft;
			mCurrentIndexNavPaddingRight = mFinalIndexNavPaddingRight;
			return;
		}

		// 下滑下界
		if (newTop <= mIndexNavZoomBeginScrollTop)
		{
			changeIndexNavPadding(0, 0);
			mCurrentIndexNavPaddingLeft = 0;
			mCurrentIndexNavPaddingRight = 0;
			return;
		}

		// 上滑下交界
		if (mIndexNavZoomBeginScrollTop > oldTop && newTop > mIndexNavZoomBeginScrollTop)
		{
			mCurrentIndexNavPaddingLeft = (int) ((newTop - mIndexNavZoomBeginScrollTop) * mIndexNavMoveLeftFactor);
			mCurrentIndexNavPaddingRight = (int) ((newTop - mIndexNavZoomBeginScrollTop) * mIndexNavMoveRightFactor);
			ensureIndexNavPaddingRight(newTop, oldTop);
			changeIndexNavPadding(mCurrentIndexNavPaddingLeft, mCurrentIndexNavPaddingRight);
			return;
		}

		// 下滑上交界
		if (mIndexNavZoomBeginScrollTop < oldTop && newTop < mIndexNavZoomBeginScrollTop)
		{
			mCurrentIndexNavPaddingLeft = (int) (mFinalIndexNavPaddingLeft - (mIndexNavZoomBeginScrollTop - newTop) * mIndexNavMoveLeftFactor);
			mCurrentIndexNavPaddingRight = (int) (mCurrentIndexNavPaddingRight - (mIndexNavZoomBeginScrollTop - newTop) * mIndexNavMoveRightFactor);
			ensureIndexNavPaddingRight(newTop, oldTop);
			changeIndexNavPadding(mCurrentIndexNavPaddingLeft, mCurrentIndexNavPaddingRight);
			return;
		}

		// 中间状态
		if (mIndexNavZoomBeginScrollTop <= oldTop && oldTop <= mIndexNavZoomEndScrollTop)
		{
			mCurrentIndexNavPaddingLeft = (int) (mCurrentIndexNavPaddingLeft + (newTop - oldTop) * mIndexNavMoveLeftFactor);
			mCurrentIndexNavPaddingRight = (int) (mCurrentIndexNavPaddingRight + (newTop - oldTop) * mIndexNavMoveRightFactor);
			ensureIndexNavPaddingRight(newTop, oldTop);
			changeIndexNavPadding(mCurrentIndexNavPaddingLeft, mCurrentIndexNavPaddingRight);
		}
	}

	private void ensureIndexNavPaddingRight(int newTop, int oldTop)
	{
		if (newTop > oldTop)
		{
			mCurrentIndexNavPaddingLeft = mCurrentIndexNavPaddingLeft > mFinalIndexNavPaddingLeft ? mFinalIndexNavPaddingLeft : mCurrentIndexNavPaddingLeft;
			mCurrentIndexNavPaddingRight = mCurrentIndexNavPaddingRight > mFinalIndexNavPaddingRight ? mFinalIndexNavPaddingRight
			        : mCurrentIndexNavPaddingRight;
		}
		else
		{
			mCurrentIndexNavPaddingLeft = mCurrentIndexNavPaddingLeft < 0 ? 0 : mCurrentIndexNavPaddingLeft;
			mCurrentIndexNavPaddingRight = mCurrentIndexNavPaddingRight < 0 ? 0 : mCurrentIndexNavPaddingRight;
		}
	}

	private int mMainNavTranBeginScrollTop;
	private int mMainNavTranEndScrollTop;
	private float mMNavIndexFactor;
	private float mMNavMidTGroupFactor;
	private float mMNavRightContainerFactor;
	private int mOrginMNavIndexMarginLeft;
	private int mOrginMNavMidTGroupMarginTop;
	private int mOrginMNavRightContainerMarginRight;
	private int mCurrentMNavIndexMarginLeft;
	private int mCurrentMNavMidTGroupMarginTop;
	private int mCurrentMNavRightContainerMarginRight;

	/**
	 * 移动主导航栏的元素
	 */
	private void transferMainNavOnScroll(int newLeft, int newTop, int oldLeft, int oldTop)
	{

		// 上滑上界
		if (newTop >= mMainNavTranEndScrollTop)
		{
			mCurrentMNavIndexMarginLeft = 0;
			mCurrentMNavMidTGroupMarginTop = 0;
			mCurrentMNavRightContainerMarginRight = 0;
			setLeftMargin(mVMainNavIndex, 0);
			setTopMargin(mVMNavMidTGroup, 0);
			setRightMargin(mVMNavRightContainer, mCurrentMNavRightContainerMarginRight);
			return;
		}

		// 下滑下界
		if (newTop <= mMainNavTranBeginScrollTop)
		{
			mCurrentMNavIndexMarginLeft = mOrginMNavIndexMarginLeft;
			mCurrentMNavMidTGroupMarginTop = mOrginMNavMidTGroupMarginTop;
			mCurrentMNavRightContainerMarginRight = mOrginMNavRightContainerMarginRight;
			setLeftMargin(mVMainNavIndex, mOrginMNavIndexMarginLeft);
			setTopMargin(mVMNavMidTGroup, mOrginMNavMidTGroupMarginTop);
			setRightMargin(mVMNavRightContainer, mCurrentMNavRightContainerMarginRight);
			return;
		}

		// 上滑下交界
		if (mMainNavTranBeginScrollTop > oldTop && newTop > mMainNavTranBeginScrollTop)
		{
			mCurrentMNavIndexMarginLeft = (int) (mOrginMNavIndexMarginLeft + (newTop - mMainNavTranBeginScrollTop) * mMNavIndexFactor);
			mCurrentMNavMidTGroupMarginTop = (int) (mOrginMNavMidTGroupMarginTop - (newTop - mMainNavTranBeginScrollTop) * mMNavMidTGroupFactor);
			mCurrentMNavRightContainerMarginRight = (int) (mOrginMNavRightContainerMarginRight - (newTop - mMainNavTranBeginScrollTop)
			        * mMNavRightContainerFactor);
			ensureTransferMainNavRight(newTop, oldTop);
			setLeftMargin(mVMainNavIndex, mCurrentMNavIndexMarginLeft);
			setTopMargin(mVMNavMidTGroup, mOrginMNavMidTGroupMarginTop);
			setRightMargin(mVMNavRightContainer, mCurrentMNavRightContainerMarginRight);
			return;
		}

		// 下滑上交界
		if (mMainNavTranEndScrollTop < oldTop && newTop < mMainNavTranEndScrollTop)
		{
			mCurrentMNavIndexMarginLeft = (int) ((newTop - mMainNavTranEndScrollTop) * mMNavIndexFactor);
			mCurrentMNavMidTGroupMarginTop = (int) ((mMainNavTranEndScrollTop - newTop) * mMNavMidTGroupFactor);
			mCurrentMNavRightContainerMarginRight = (int) ((newTop - mMainNavTranEndScrollTop) * mMNavRightContainerFactor);
			ensureTransferMainNavRight(newTop, oldTop);
			setLeftMargin(mVMainNavIndex, mCurrentMNavIndexMarginLeft);
			setTopMargin(mVMNavMidTGroup, mOrginMNavMidTGroupMarginTop);
			setRightMargin(mVMNavRightContainer, mCurrentMNavRightContainerMarginRight);
			return;
		}

		// 中间
		if (mMainNavTranBeginScrollTop < oldTop && oldTop <= mMainNavTranEndScrollTop)
		{
			mCurrentMNavIndexMarginLeft = (int) (mCurrentMNavIndexMarginLeft + (newTop - oldTop) * mMNavIndexFactor);
			mCurrentMNavMidTGroupMarginTop = (int) (mCurrentMNavMidTGroupMarginTop - (newTop - oldTop) * mMNavMidTGroupFactor);
			mCurrentMNavRightContainerMarginRight = (int) (mCurrentMNavRightContainerMarginRight + (newTop - oldTop) * mMNavRightContainerFactor);
			ensureTransferMainNavRight(newTop, oldTop);
			setLeftMargin(mVMainNavIndex, mCurrentMNavIndexMarginLeft);
			setTopMargin(mVMNavMidTGroup, mCurrentMNavMidTGroupMarginTop);
			setRightMargin(mVMNavRightContainer, mCurrentMNavRightContainerMarginRight);
		}
	}

	private void ensureTransferMainNavRight(int newTop, int oldTop)
	{
		if (newTop > oldTop)
		{
			mCurrentMNavIndexMarginLeft = mCurrentMNavIndexMarginLeft > 0 ? 0 : mCurrentMNavIndexMarginLeft;
			mCurrentMNavRightContainerMarginRight = mCurrentMNavRightContainerMarginRight > 0 ? 0 : mCurrentMNavRightContainerMarginRight;
			mCurrentMNavMidTGroupMarginTop = mCurrentMNavMidTGroupMarginTop < 0 ? 0 : mCurrentMNavMidTGroupMarginTop;
		}
		else
		{
			mCurrentMNavIndexMarginLeft = mCurrentMNavIndexMarginLeft < mOrginMNavIndexMarginLeft ? mOrginMNavIndexMarginLeft : mCurrentMNavIndexMarginLeft;
			mCurrentMNavRightContainerMarginRight = mCurrentMNavRightContainerMarginRight < mOrginMNavRightContainerMarginRight ? mOrginMNavRightContainerMarginRight
			        : mCurrentMNavRightContainerMarginRight;
			mCurrentMNavMidTGroupMarginTop = mCurrentMNavMidTGroupMarginTop > mOrginMNavMidTGroupMarginTop ? mOrginMNavMidTGroupMarginTop
			        : mCurrentMNavMidTGroupMarginTop;
		}
	}

	private void changeIndexNavTextAlpha(int alpha)
	{
		setViewTextAlpha(mTvIndexNavDiscover, alpha);
		setViewTextAlpha(mTvIndexNavBlog, alpha);
		setViewTextAlpha(mTvIndexNavMine, alpha);
		setViewTextAlpha(mTvIndexNavMore, alpha);
	}

	private void changeMainNavAlpha(float alpha)
	{
		setViewAlpha(mVMainNav, alpha);
	}

	private void changeIndexNavPadding(int paddingLeft, int paddingRight)
	{
		mVIndexHeaderNav.setPadding(paddingLeft, mVIndexHeaderNav.getPaddingTop(), paddingRight, mVIndexHeaderNav.getPaddingBottom());
	}

	private void setViewTextAlpha(TextView v, int alpha)
	{
		ColorStateList colorStateList = v.getTextColors();
		v.setTextColor(colorStateList.withAlpha(alpha));
	}

	@SuppressLint("NewApi")
	private void setViewAlpha(View v, float alpha)
	{
		if (android.os.Build.VERSION.SDK_INT >= 11)
		{
			v.setAlpha(alpha);
		}
		else
		{
			AlphaAnimation alphaAnim = new AlphaAnimation(alpha, alpha);
			alphaAnim.setDuration(0);
			alphaAnim.setFillAfter(true);
			v.startAnimation(alphaAnim);
		}
	}

	private void setLeftMargin(View v, int leftMargin)
	{
		RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) v.getLayoutParams();
		lp.leftMargin = leftMargin;
		v.setLayoutParams(lp);
	}

	private void setTopMargin(View v, int topMargin)
	{
		RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) v.getLayoutParams();
		lp.topMargin = topMargin;
		v.setLayoutParams(lp);
	}

	private void setRightMargin(View v, int rightMargin)
	{
		RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) v.getLayoutParams();
		lp.rightMargin = rightMargin;
		v.setLayoutParams(lp);
	}

	private final static int MSG_PULL_RECOVER = 0x999;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case MSG_PULL_RECOVER:
				int upY = mSvMain.getScrollY();
				int divideScrollY = mMaxScrollTop / 2;
				if (upY <= divideScrollY)
				{
					hideMainNav();
				}
				else if (upY < mMaxScrollTop)
				{
					showMainNav();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_UP)
		{
			mHandler.removeMessages(MSG_PULL_RECOVER);
			mHandler.sendEmptyMessageDelayed(MSG_PULL_RECOVER, 200);
		}
		return false;
	}

	private void showMainNav()
	{
		fadeTextOnScroll(0, mMaxScrollTop, 0, mMaxScrollTop);
		fadeMainNavOnScroll(0, mMaxScrollTop, 0, mMaxScrollTop);
		zoomIndexNavOnScroll(0, mMaxScrollTop, 0, mMaxScrollTop);
		transferMainNavOnScroll(0, mMaxScrollTop, 0, mMaxScrollTop);
		mSvMain.scrollTo(0, mMaxScrollTop);
	}

	private void hideMainNav()
	{
		fadeTextOnScroll(0, 0, 0, 0);
		fadeMainNavOnScroll(0, 0, 0, 0);
		zoomIndexNavOnScroll(0, 0, 0, 0);
		transferMainNavOnScroll(0, 0, 0, 0);
		mSvMain.scrollTo(0, 0);
	}

}
