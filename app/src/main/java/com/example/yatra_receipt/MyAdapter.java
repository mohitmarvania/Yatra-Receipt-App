package com.example.yatra_receipt;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    RealmResults<Data> dataList;

    public MyAdapter(Context context, RealmResults<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.nameOutput.setText(data.getName().toUpperCase(Locale.ROOT));
        holder.yatraOutput.setText("Yatra: " + data.getTithiYatra());

        String formatedTime = DateFormat.getDateTimeInstance().format(data.createdTime);
        holder.creationOutput.setText(formatedTime);

        // UPDATING DATA FOR USER.
        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, AddUserDetails.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.putExtra("name", data.name);
            intent.putExtra("gam", data.gam);
            intent.putExtra("mobileNumber", data.mobileNo);
            intent.putExtra("tirthYatra", data.tithiYatra);
            intent.putExtra("totalPeople", data.people);
            intent.putExtra("totalChildren", data.children);
            intent.putExtra("amount", data.amount);
            intent.putExtra("deposit", data.deposit);
            intent.putExtra("baki", data.baki);
            intent.putExtra("svikarnar", data.svikarnar);

            String dataId = Integer.toString(holder.itemView.getId());
            intent.putExtra("dataId", dataId);

            context.startActivity(intent);
        });

        // DELETING A USER.
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu menu = new PopupMenu(context, v);
                menu.getMenu().add("Delete");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Delete")) {
                            // Delete the node.
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            data.deleteFromRealm();
                            realm.commitTransaction();
                            Toast.makeText(context, "Data Deleted !!", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                menu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameOutput, yatraOutput, creationOutput;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameOutput = itemView.findViewById(R.id.nameOutput);
            yatraOutput = itemView.findViewById(R.id.yatraOutput);
            creationOutput = itemView.findViewById(R.id.creationOutput);
        }
    }

}
