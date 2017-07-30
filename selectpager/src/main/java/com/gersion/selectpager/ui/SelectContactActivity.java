package com.gersion.selectpager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gersion.selectpager.R;
import com.gersion.selectpager.adapter.SelectIconRvAdapter;
import com.gersion.selectpager.adapter.SelectRvAdapter;
import com.gersion.selectpager.utils.MatchUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SelectContactActivity extends AppCompatActivity {
    public static final String RESULT_DATA = "RESULT_DATA"; // 返回结果
    private static final String FILTER_DATA = "filter_data";
    private static final String OPTIONS = "options";
    private static final String MAIN_DATA = "main_data";

    private static final int REQUEST_CODE_FORWARD_PERSON = 0x01; //保证与MessageListPanel值一样
    private static final int REQUEST_CODE_FORWARD_TEAM = 0x02;

    @Bind(R.id.icon_recyclerView)
    RecyclerView mIconRecyclerView;
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.container)
    LinearLayout mContainer;
    @Bind(R.id.content_recyclerView)
    RecyclerView mContentRecyclerView;
    @Bind(R.id.pageLoader)
    PageLoader mPageLoader;
    @Bind(R.id.iv_select_all)
    ImageView mIvSelectAll;
    @Bind(R.id.ll_select_all)
    LinearLayout mLlSelectAll;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.activity_select_contact)
    LinearLayout mActivitySelectContact;
    @Bind(R.id.titleView)
    TitleView mTitleView;
    @Bind(R.id.tv_indexview_tip)
    TextView mTvIndexviewTip;
    @Bind(R.id.indexView)
    IndexView mIndexView;
    @Bind(R.id.fl_container)
    FrameLayout mFlContainer;

    private SelectIconRvAdapter mIconListRvAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private SelectRvAdapter mSelectContanctRvAdapter;
    private List<FriendBean.FriendDataBean> mDatas = new ArrayList<>();
    private List<FriendBean.FriendDataBean> mSelectList;
    private List<FriendBean.FriendDataBean> mMatchList = new ArrayList<>();
    private List<FriendBean.FriendDataBean> mMainList;
    private ArrayList<String> mResultList = new ArrayList<>();
    private int mMaxWidth;
    private Map<String, Object> mPramasMap;
    private List<String> mFilterDatas;
    private BGARecyclerViewScrollHelper mRecyclerViewScrollHelper;
    private String mCurrentTitle;
    private String mUserid;
    private boolean mHasCurrentUser;
    private int mSelectType;
    private String mTeamId;
    private String mTitle;
    private String mMaxSelectedTip;
    private int mMaxSelectNum;

    private static boolean isOnlyOne = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_select_view);
        setupView();
    }

    @Override
    protected void setupView() {
        setContentView(R.layout.view_select_view);
        ButterKnife.bind(this);
        mPageLoader.setContentView(mFlContainer);
        mTitleView.setTopText("选择成员");
        initRecycleView();
        int screenWidth = ScreenUtils.getScreenWidth(this);
        mMaxWidth = screenWidth * 2 / 3;
        mPageLoader.stopProgress();
        mUserid = PreferenceUtils.getString(this, "userid");
    }

    private void initRecycleView() {
        mIconListRvAdapter = new SelectIconRvAdapter(null);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setAutoMeasureEnabled(true);

        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mIconRecyclerView.setLayoutManager(mLinearLayoutManager);
        mIconRecyclerView.setAdapter(mIconListRvAdapter);

        mSelectContanctRvAdapter = new SelectRvAdapter(null);
        LinearLayoutManager supportLinearLayoutManager = new LinearLayoutManager(this);
        supportLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mContentRecyclerView.setLayoutManager(supportLinearLayoutManager);
        mContentRecyclerView.setAdapter(mSelectContanctRvAdapter);

        mSelectList = mIconListRvAdapter.getList();
        TitleItemDecoration decoration = new TitleItemDecoration();
        mContentRecyclerView.addItemDecoration(decoration);
        mRecyclerViewScrollHelper = BGARecyclerViewScrollHelper.newInstance(mContentRecyclerView);

        if (isOnlyOne) {
            mLlSelectAll.setVisibility(View.GONE);
        } else {
            mLlSelectAll.setVisibility(View.VISIBLE);
        }
    }

    public static void startActivityForResult(FragmentActivity activity, List<String> data, String paramas, int requestCode) {
        Intent intent = new Intent(activity, SelectContactActivity.class);
        intent.putExtra(FILTER_DATA, (Serializable) data);
        intent.putExtra(EXTRA_PARAMS, paramas);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(FragmentActivity activity, List<String> data, int requestCode) {
        Intent intent = new Intent(activity, SelectContactActivity.class);
        intent.putExtra(FILTER_DATA, (Serializable) data);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(FragmentActivity activity, List<FriendBean.FriendDataBean> data, List<String> filterData, int requestCode) {
        Intent intent = new Intent(activity, SelectContactActivity.class);
        intent.putExtra(FILTER_DATA, (Serializable) filterData);
        intent.putExtra(MAIN_DATA, (Serializable) data);
        if (requestCode == REQUEST_CODE_FORWARD_PERSON || requestCode == REQUEST_CODE_FORWARD_TEAM) {
            isOnlyOne = true;
        } else {
            isOnlyOne = false;
        }
        activity.startActivityForResult(intent, requestCode);
    }

//    public static void startActivityForResult(FragmentActivity activity,Options options, int requestCode) {
//        Intent intent = new Intent(activity, SelectContactActivity.class);
//        intent.putExtra(OPTIONS, options);
//        activity.startActivityForResult(intent, requestCode);
//    }

//    private void parseIntent(){
//        Intent intent = getIntent();
//        Options options = (Options) intent.getSerializableExtra(OPTIONS);
//        if (options!=null){
//            mSelectType = options.type;
//            mTeamId = options.teamId;
//            mFilterDatas = options.filterData;
//            mTitle = options.title;
//            mMaxSelectedTip = options.maxSelectedTip;
//            mMaxSelectNum = options.maxSelectNum;
//        }
//        if (mSelectType==Options.SELECT_ADDRESS){//选择联系人
//            getAddressData();
//        }else if (mSelectType==Options.SELECT_CIRCLE){//选择圈子成员
//            if (mTeamId==null){
//                throw new NullPointerException("teamId不能为空");
//            }
//            getCicleMemberData(mTeamId);
//        }
//        if (mTitle!=null){
////            throw new NullPointerException("title不能为空");
//            mTitleView.setTopText(mTitle);
//        }
//    }

    private void parseIntentData() {
        Intent intent = getIntent();
        mFilterDatas = (List<String>) intent.getSerializableExtra(FILTER_DATA);
        mMainList = (List<FriendBean.FriendDataBean>) intent.getSerializableExtra(MAIN_DATA);
        if (mMainList == null) {
//            getInviteData(intent);//分享的数据
            getAddressData();
            getInviteData(intent);
        } else {
            refreshAdapter(mMainList);
        }
    }

    @Override
    protected void initData() {
        parseIntentData();
//        parseIntent();
    }

    @Override
    protected void setClickListener() {
        mTitleView.setBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResultList.size() > 0) {
                    mResultList.clear();
                }
                for (FriendBean.FriendDataBean friendDataBean : mSelectList) {
                    mResultList.add(friendDataBean.friendId);
                }
                LoggerUtils.d(mResultList.size());
                onSelected(mResultList);
            }
        });

        mIndexView.setTipTv(mTvIndexviewTip);
        mIndexView.setDelegate(new IndexView.Delegate() {
            @Override
            public void onIndexViewSelectedChanged(IndexView indexView, String text) {
                int position = mSelectContanctRvAdapter.getPositionForCategory(text);
                if (position != -1) {
                    mRecyclerViewScrollHelper.smoothScrollToPosition(position);
                }
            }
        });

        mPageLoader.setOnRetryClickListener(new PageLoader.OnRetryClickListener() {
            @Override
            public void onRetryClick() {
                getAddressData();
            }
        });

        mLlSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIvSelectAll.isSelected()) {
                    mIconListRvAdapter.addAllData(mDatas);

                } else {
                    mIconListRvAdapter.clear();
                }
                LoggerUtils.d(mSelectList.size());
                mSelectContanctRvAdapter.changeAllDataStatus(!mIvSelectAll.isSelected());
                refreshLayout(!mIvSelectAll.isSelected());
            }
        });

        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = s.toString();
                mMatchList.clear();
                if (TextUtils.isEmpty(content)) {
                    mSelectContanctRvAdapter.addAllData(mDatas);
                } else {
                    for (FriendBean.FriendDataBean friendDataBean : mDatas) {
                        if (MatchUtils.isMatch(friendDataBean.friendAlias, content)) {
                            mMatchList.add(friendDataBean);
                        }
                    }
                    mSelectContanctRvAdapter.addAllData(mMatchList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (isOnlyOne) {
            mSelectContanctRvAdapter.setHasMaxSize(true);
            mSelectContanctRvAdapter.setMaxSize(1);
            mSelectContanctRvAdapter.setOnSelectItemChangeListener(new SelectRvAdapter.OnSelectItemChangeListener() {
                @Override
                public void onAdd(FriendBean.FriendDataBean bean) {
                    mIconListRvAdapter.clear();
                    mIconListRvAdapter.add(bean);
                    refreshLayout(mIvSelectAll.isSelected());
                }

                @Override
                public void onRemove(FriendBean.FriendDataBean bean) {
                    mIconListRvAdapter.clear();
                    refreshLayout(false);
                }
            });

            mIconListRvAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FriendBean.FriendDataBean friendDataBean = mSelectList.get(position);
                    mIconListRvAdapter.removeItem(friendDataBean);
                    mSelectContanctRvAdapter.updateItem(friendDataBean);
                    refreshLayout(false);
                }
            });
        } else {
            mSelectContanctRvAdapter.setHasMaxSize(false);
            mSelectContanctRvAdapter.setOnSelectItemChangeListener(new SelectRvAdapter.OnSelectItemChangeListener() {
                @Override
                public void onAdd(FriendBean.FriendDataBean bean) {
                    mIconListRvAdapter.add(bean);
                    refreshLayout(mIvSelectAll.isSelected());
                }

                @Override
                public void onRemove(FriendBean.FriendDataBean bean) {
                    mIconListRvAdapter.remove(bean);
                    refreshLayout(false);
                }
            });

            mIconListRvAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FriendBean.FriendDataBean friendDataBean = mSelectList.get(position);
                    mIconListRvAdapter.removeItem(friendDataBean);
                    mSelectContanctRvAdapter.updateItem(friendDataBean);
                    refreshLayout(false);
                }
            });
        }
    }

    //获取联系人数据
    private void getAddressData() {
        mPageLoader.startProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", AccountUtil.getAccount().userid);
        FriendController.searchFriend(Constants.QUERYFRIENDLIST, GsonQuick.toJsonFromMap(map), this, new ResultListener() {
            @Override
            public void onError(String response) {
                showToast(response);
                mPageLoader.stopProgressAndFailed();
            }

            @Override
            public void onSuccess(Object o) {
                FriendBean bean = (FriendBean) o;
                if (bean != null) {
                    ArrayList<FriendBean.FriendDataBean> data = bean.data;
                    if (data == null || data.size() == 0) {
                        mPageLoader.stopProgressAndNoData();
                    } else {
                        mPageLoader.stopProgress();
                        filterDatas(mFilterDatas, data);
                        //setAdapter(list);
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).firstletter = converterToFirstSpell(data.get(i).friendAlias);
                            char ch = data.get(i).firstletter.charAt(0);
                            data.get(i).ascII = (int) ch;
                        }
                        Collections.sort(data);
                        addTitleItem(data);
                        mSelectContanctRvAdapter.addAllData(data);
                    }
                } else {
                    mPageLoader.stopProgressAndNoData();
                }
            }
        });
    }

    //获取圈子数据
    public void getCicleMemberData(String teamId) {
        mPageLoader.startProgress();
        HashMap<String, String> maps = new HashMap<>();
        maps.put("yxId", teamId);
        QueryMommentMemberControler.queryMomentMember(GsonQuick.toJsonFromMap(maps), this, new ResultListener() {
            @Override
            public void onSuccess(Object o) {
                mPageLoader.stopProgress();
                List<MomentMemberBean.MomentMemberInnerBean> data = (List<MomentMemberBean.MomentMemberInnerBean>) o;
                TeaminfoCache.MomentMembers = (ArrayList<MomentMemberBean.MomentMemberInnerBean>) data;
                TeaminfoCache.NimUserInfo = toNimList(data);
                ArrayList<FriendBean.FriendDataBean> list = convert2FriendBean(data);
                refreshAdapter(list);
            }

            @Override
            public void onError(String response) {
                showToast(response);
                mPageLoader.stopProgressAndFailed();
            }
        });
    }

    public ArrayList<NimUserInfo> toNimList(List<MomentMemberBean.MomentMemberInnerBean> data) {
        ArrayList<NimUserInfo> infos = new ArrayList<>();
        for (final MomentMemberBean.MomentMemberInnerBean bean : data) {
            NimUserInfo info = new NimUserInfo() {
                @Override
                public String getSignature() {
                    return null;
                }

                @Override
                public GenderEnum getGenderEnum() {
                    return null;
                }

                @Override
                public String getEmail() {
                    return null;
                }

                @Override
                public String getBirthday() {
                    return null;
                }

                @Override
                public String getMobile() {
                    return null;
                }

                @Override
                public String getExtension() {
                    return null;
                }

                @Override
                public Map<String, Object> getExtensionMap() {
                    return null;
                }

                @Override
                public String getAccount() {
                    return bean.userId;
                }

                @Override
                public String getName() {
                    return bean.alias;
                }

                @Override
                public String getAvatar() {
                    return bean.icon;
                }
            };
            infos.add(info);
        }
        return infos;
    }

    private ArrayList<FriendBean.FriendDataBean> convert2FriendBean(List<MomentMemberBean.MomentMemberInnerBean> data) {
        ArrayList<FriendBean.FriendDataBean> list = new ArrayList<>();
        if (data == null || data.size() == 0) {
            mPageLoader.stopProgressAndNoData();
        } else {
            for (MomentMemberBean.MomentMemberInnerBean dataBean : data) {
                FriendBean.FriendDataBean bean = new FriendBean.FriendDataBean();
                bean.friendId = dataBean.groupId;
                bean.friendAlias = dataBean.alias;
                bean.icon = dataBean.icon;
                list.add(bean);
            }
        }
        return list;
    }

    //刷新界面展示（过滤数据，排序，添加分组）,获取到所有的数据后调用
    private void refreshAdapter(List<FriendBean.FriendDataBean> data) {
        if (data == null || data.size() == 0) {
            mPageLoader.stopProgressAndNoData();
        } else {
            getFirstLetter(data);
            filterDatas(mFilterDatas, data);
            Collections.sort(data);
            addTitleItem(data);
            mSelectContanctRvAdapter.addAllData(data);
        }
    }

    //生成首字母字段，在数据转换的过程中使用
    private void getFirstLetter(List<FriendBean.FriendDataBean> data) {
        for (FriendBean.FriendDataBean bean : data) {
            bean.firstletter = converterToFirstSpell(bean.friendAlias);
            char ch = bean.firstletter.charAt(0);
            bean.ascII = (int) ch;
        }
    }

    private void addTitleItem(List<FriendBean.FriendDataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            String firstletter = data.get(i).firstletter;
            if (!TextUtils.equals(mCurrentTitle, firstletter)) {
                mCurrentTitle = firstletter;
                FriendBean.FriendDataBean bean = new FriendBean.FriendDataBean();
                bean.firstletter = getAlpha(firstletter);
                data.add(i, bean);
            }
        }
    }

    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(str).matches()) {
            return (str).toUpperCase();
        } else {
            return "#";
        }
    }

    public void filterDatas(List<String> data, List<FriendBean.FriendDataBean> datas) {//过滤掉已经是圈子成员的好友
        if (mDatas.size() > 0) {
            mDatas.clear();
        }
        mDatas.addAll(datas);
        if (data != null && data.size() > 0) {
            for (FriendBean.FriendDataBean friendDataBean : datas) {
                if (friendDataBean.friendId.equals(mUserid)) {
                    friendDataBean.isFiltered = true;
                    mDatas.remove(friendDataBean);
                    mHasCurrentUser = true;
                } else {
                    for (String friendId : data) {
                        if (friendDataBean.friendId.equals(friendId)) {
                            friendDataBean.isFiltered = true;
                            mDatas.remove(friendDataBean);
                        }
                    }
                }
            }
        }
    }

    //添加圈子成员时，分享给好友的数据
    private void getInviteData(Intent intent) {
        String stringExtra = intent.getStringExtra(EXTRA_PARAMS);
        if (stringExtra == null) {
            return;
        }
        String imageUrl = GsonQuick.getString(stringExtra, "icon");
        String url = GsonQuick.getString(stringExtra, "pageUrl");
        String name = GsonQuick.getString(stringExtra, "name");
        mPramasMap = new HashMap<>();
        mPramasMap.put("image", imageUrl);
        mPramasMap.put("title", "邀请您加入\"" + name + "\"");
        mPramasMap.put("detailText", "您的好友邀请您加入\"" + name + "\",TA在那里等你哟~");
        mPramasMap.put("url", url);
        mPramasMap.put("code", Constants.Reciver_SystemMsg_Code.INITVATECICLE);
    }


    public void onSelected(ArrayList<String> selects) {
        if (mPramasMap != null) {//添加圈子成员
            if (selects != null) {
                for (String select : selects) {
                    CustomMsgUtils.SendCustom(this, select, SessionTypeEnum.P2P, mPramasMap, "邀请成功");
                }
            }
        } else {
            setResults(selects);//分享到好友
        }
        finish(this);
    }

    private void setResults(ArrayList<String> selects) {
        if (selects == null || selects.size() == 0) {
            ToastUtil.show(this, "请选择联系人");
            return;
        }
        Intent intent = new Intent();
        intent.putStringArrayListExtra(RESULT_DATA, selects);
        setResult(RESULT_OK, intent);
    }

    private void refreshLayout(boolean isSelected) {
        mIvSelectAll.setSelected(isSelected);
        int size = mSelectList.size();
        mTvConfirm.setText("确定(" + size + ")");
        if (size == 0) {
            mTvConfirm.setEnabled(false);
        } else {
            mTvConfirm.setEnabled(true);
        }
//        int width = SizeUtils.dp2px(this, 45) * size;
//        if (width > mMaxWidth) {//只能是屏幕宽度的3/4
//            width = mMaxWidth;
//        }
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
//        mIconRecyclerView.setLayoutParams(params);
//        mContainer.requestLayout();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (SharePopup.getInstance() != null) {
            SharePopup.getInstance().finish();
        } else if (ShareCirclePopup.getInstance() != null) {
            ShareCirclePopup.getInstance().quit();
        }
    }

    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    public String converterToFirstSpell(String chines) {
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < 1; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
                } catch (Exception e) {
                    pinyinName = "#"; //解析不了即为表情或者特殊字符
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    public static class Options implements Serializable {
        public static final int SELECT_ADDRESS = 1001;//选择联系人
        public static final int SELECT_CIRCLE = 1002;//选择圈子
        public String title;//标题，用来设置titleview
        public String teamId;
        public String maxSelectedTip;
        public int maxSelectNum;
        public List<String> filterData = new ArrayList<>();//过滤掉的数据
        public int type = SELECT_ADDRESS;//要选择的数据类型，默认为选择联系人
    }

}
