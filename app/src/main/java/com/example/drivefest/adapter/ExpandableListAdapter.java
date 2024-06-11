package com.example.drivefest.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.drivefest.R;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> titles;
    private HashMap<String, List<String>> detailsList;
    private Set<String> checkedItems;

    public ExpandableListAdapter(Context context, List<String> titles, HashMap<String, List<String>> detailsList) {
        this.context = context;
        this.titles = titles;
        this.detailsList = detailsList;
        this.checkedItems = new HashSet<>();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.filter_list_elem, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.expandedListItem);

        expandedListTextView.setText(expandedListText);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(checkedItems.contains(expandedListText));
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedItems.add(expandedListText);
            } else {
                checkedItems.remove(expandedListText);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.detailsList.get(this.titles.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.titles.get(listPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.detailsList.get(this.titles.get(groupPosition)).get(childPosition);
    }

    @Override
    public int getGroupCount() {
        return this.titles.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.filter_list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public Set<String> getCheckedItems() {
        return checkedItems;
    }
}
