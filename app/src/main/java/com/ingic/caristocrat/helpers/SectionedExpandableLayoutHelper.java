package com.ingic.caristocrat.helpers;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ingic.caristocrat.adapters.SpecsAdapter;
import com.ingic.caristocrat.interfaces.ItemClickListener;
import com.ingic.caristocrat.interfaces.SectionStateChangeListener;
import com.ingic.caristocrat.models.MyCarAttributes;
import com.ingic.caristocrat.models.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 */
public class SectionedExpandableLayoutHelper implements SectionStateChangeListener {

    //data list
    private LinkedHashMap<Section, ArrayList<MyCarAttributes>> mSectionDataMap = new LinkedHashMap<Section, ArrayList<MyCarAttributes>>();
    private ArrayList<Object> mDataArrayList = new ArrayList<Object>();

    //section map
    //TODO : look for a way to avoid this
    private HashMap<String, Section> mSectionMap = new HashMap<String, Section>();

    //adapter
    private SpecsAdapter specsAdapter;

    //recycler view
    RecyclerView mRecyclerView;

    public SectionedExpandableLayoutHelper(Context context, RecyclerView recyclerView, ItemClickListener itemClickListener,
                                           int gridSpanCount) {

        //setting the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, gridSpanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        specsAdapter = new SpecsAdapter(context, mDataArrayList,
                gridLayoutManager, itemClickListener, this);
        recyclerView.setAdapter(specsAdapter);

        mRecyclerView = recyclerView;
    }

    public void notifyDataSetChanged() {
        //TODO : handle this condition such that these functions won't be called if the recycler view is on scroll
        generateDataList();
        specsAdapter.notifyDataSetChanged();
    }

    public void addSection(String section, ArrayList<MyCarAttributes> items) {
        Section newSection;
        mSectionMap.put(section, (newSection = new Section(section)));
        mSectionDataMap.put(newSection, items);
//        newSection.isExpanded = true;
    }

    public void addItem(String section, MyCarAttributes item) {
        mSectionDataMap.get(mSectionMap.get(section)).clear();
        mSectionDataMap.get(mSectionMap.get(section)).add(item);
    }

    public void addAll(String section, ArrayList<MyCarAttributes> item) {
        mSectionDataMap.get(mSectionMap.get(section)).clear();
        mSectionDataMap.get(mSectionMap.get(section)).addAll(item);
    }

    public void removeItem(String section, MyCarAttributes item) {
        mSectionDataMap.get(mSectionMap.get(section)).remove(item);
    }

    public void removeSection(String section) {
        mSectionDataMap.remove(mSectionMap.get(section));
        mSectionMap.remove(section);
    }

    private void generateDataList() {
        mDataArrayList.clear();
        for (Map.Entry<Section, ArrayList<MyCarAttributes>> entry : mSectionDataMap.entrySet()) {
            Section key;
            mDataArrayList.add((key = entry.getKey()));
            if (key.isExpanded)
                mDataArrayList.addAll(entry.getValue());
        }
    }

    @Override
    public void onSectionStateChanged(Section section, boolean isOpen) {
        if (!mRecyclerView.isComputingLayout()) {
            section.isExpanded = isOpen;
            notifyDataSetChanged();
        }
    }
}
