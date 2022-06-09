package com.example.digital_diary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.MyViewHolder>  {
    ArrayList<Entry> entryArrayList;
    DBHelper dbHelper;
    EntryAdapter adapter;
    public EntryAdapter(ArrayList<Entry> entryArrayList) {
        this.entryArrayList = entryArrayList;
        this.adapter=this;
    }

    @NonNull
    @Override
    public EntryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.memory_cardview,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EntryAdapter.MyViewHolder holder, int position) {
        final ArrayList<Entry> entryList= entryArrayList;
        holder.textEntryLocation.setText(entryList.get(position).getLocation());
        holder.textEntryTitle.setText(entryList.get(position).getTitle());
        holder.textEntryDate.setText(entryList.get(position).getDate());
        holder.imageView.setImageResource(R.drawable.ic_smile_image);
        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(entryList.get(holder.getAdapterPosition()).getPassword()==null){
                    Intent intent =new Intent(view.getContext(),MemoryPageActivity.class);
                    intent.putExtra("Entry",entryArrayList.get(holder.getAdapterPosition()));
                    view.getContext().startActivity(intent);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Enter Password");
                    final EditText input = new EditText(view.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           String m_Text = input.getText().toString();
                           if(entryArrayList.get(holder.getAdapterPosition()).getPassword().equals(m_Text)){
                               Intent intent =new Intent(view.getContext(),MemoryPageActivity.class);
                               intent.putExtra("Entry",entryArrayList.get(holder.getAdapterPosition()));
                               view.getContext().startActivity(intent);
                           }else{
                               Toast.makeText(view.getContext(), "Wrong Password ", Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
        if(entryList.get(position).getPassword()==null){
            holder.passwordStatus.setImageResource(R.drawable.ic_unlocked);
        }else{
            holder.passwordStatus.setImageResource(R.drawable.ic_locked);
        }
    }

    @Override
    public int getItemCount() {
        return entryArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textEntryTitle,textEntryDate,textEntryLocation;
        public ImageView imageView,passwordStatus,optionsMenuButton;
        public CardView itemCard;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textEntryDate=(TextView)itemView.findViewById(R.id.date);
            this.textEntryTitle=(TextView)itemView.findViewById(R.id.memoryTitle);
            this.textEntryLocation=(TextView)itemView.findViewById(R.id.location);
            this.imageView = (ImageView)itemView.findViewById(R.id.imageView);
            this.passwordStatus=(ImageView)itemView.findViewById(R.id.passwordStatus);
            this.optionsMenuButton= (ImageView)itemView.findViewById(R.id.menuButton);
            this.itemCard = (CardView) itemView.findViewById(R.id.entryCard);
            dbHelper =new DBHelper(itemView.getContext());
            optionsMenuButton.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if(view==optionsMenuButton){
                optionsMenuButtonFunction(view,getLayoutPosition());
            }
        }

        private void optionsMenuButtonFunction(View view,int position){
            PopupMenu popupMenu = new PopupMenu(view.getContext(),optionsMenuButton);
            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    // Toast message on menu item clicked
                    if(menuItem.getTitle().toString().equals("Add or Update Password" )){
                        setDialog(menuItem.getTitle().toString());

                    }else if(menuItem.getTitle().toString().equals("Remove Password" )){
                        setDialog(menuItem.getTitle().toString());
                    }else{
                        deleteEntry(menuItem.getTitle().toString());
                    }
                    return true;
                }

                private void deleteEntry(String clickedButton){
                    if(entryArrayList.get(position).getPassword()!=null){
                        setDialog(clickedButton);
                    }else{
                        dbHelper.deleteEntry(entryArrayList.get(position).getId());
                        entryArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, entryArrayList.size());
                    }
                }

                private void setDialog(String clickedButton){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    if(clickedButton.equals("Add or Update Password")){
                        if(entryArrayList.get(position).getPassword()!=null){
                            builder.setTitle("Update Password");
                        }else{
                            builder.setTitle("Add Password");
                        }
                    }else if(clickedButton.equals("Remove Password")){
                        builder.setTitle(clickedButton);
                    }else{
                        builder.setTitle("Enter password");
                    }
                    final EditText input = new EditText(view.getContext());
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String m_Text = input.getText().toString();
                            if(clickedButton.equals("Add or Update Password")){
                                if(m_Text.equals("")){
                                    Toast.makeText(view.getContext(), "You need to entry a password. ", Toast.LENGTH_SHORT).show();
                                }else{
                                    dbHelper.addPassword(entryArrayList.get(position).getId(),m_Text);
                                    entryArrayList.get(position).setPassword(m_Text);
                                    notifyItemChanged(position);
                                }
                            }
                            else if(clickedButton.equals("Remove Password")){
                                if(dbHelper.deletePassword(entryArrayList.get(position).getId(),m_Text)){
                                    Toast.makeText(view.getContext(), "Deleted password successfully ", Toast.LENGTH_SHORT).show();
                                    entryArrayList.get(position).setPassword(null);
                                    notifyItemChanged(position);
                                }
                                else {
                                    Toast.makeText(view.getContext(), "Wrong Password ", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                if(dbHelper.deletePassword(entryArrayList.get(position).getId(),m_Text)){
                                    entryArrayList.get(position).setPassword(null);
                                    notifyItemChanged(position);
                                    dbHelper.deleteEntry(entryArrayList.get(position).getId());
                                    entryArrayList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, entryArrayList.size());
                                }
                                else{
                                    Toast.makeText(view.getContext(), "You need to enter right password or master password ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

                        }
                    });
                    builder.show();
                }
            });
            // Showing the popup menu
            popupMenu.show();
        }
    }

}
