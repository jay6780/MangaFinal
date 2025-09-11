package com.m.manga.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.m.manga.Fragment.ChapterFragment;
import com.m.manga.Fragment.InformationFragment;


public class InformationAdapter extends FragmentPagerAdapter {

    private String Id;
    private String title;
    private String desc;
    private String thumbUrl;
    public InformationAdapter(@NonNull FragmentManager fm, String Id,String title,String desc,String thumbUrl) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.Id = Id;
        this.title = title;
        this.desc = desc;
        this.thumbUrl = thumbUrl;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return InformationFragment.newInstance(Id,desc,title,thumbUrl);
            case 1:
                return ChapterFragment.newInstance(Id);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
