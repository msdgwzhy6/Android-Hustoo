package com.scb.administrator.a.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

/**
 * Created by Administrator on 2015/6/5 0005.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String>   titleList;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList){
        super(fm);

        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    /**
     * 得到每个页面
     */
    @Override
    public Fragment getItem(int arg0) {
        return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
    }

    /**
     * 每个页面的title
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return (titleList.size() > position) ? titleList.get(position) : "";
    }

    /**
     * 页面的总个数
     */
    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}