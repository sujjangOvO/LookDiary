package com.example.lookdiary;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Dress> dresses = null;

    CustomAdapter(ArrayList<Dress> dressList){ // 생성자
        dresses = dressList;
    }

    // 아이템 뷰를 저장하는 뷰홀더
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView type, name, cost, brand, size;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgView);
            type = (TextView) itemView.findViewById(R.id.type);
            name = (TextView) itemView.findViewById(R.id.name);
            size = (TextView) itemView.findViewById(R.id.size);
            brand = (TextView) itemView.findViewById(R.id.brand);
            cost = (TextView) itemView.findViewById(R.id.cost);

            // 아이템뷰 롱클릭
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    //Toast.makeText(itemView.getContext(),pos+"번째 롱클릭",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dlg = new AlertDialog.Builder(itemView.getContext());
                    dlg.setTitle("삭제 확인 메시지");
                    dlg.setMessage("해당 아이템을 삭제하시겠습니까?");
                    dlg.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    dlg.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dress removeDress = dresses.get(pos);
                            String type = removeDress.getType();
                            String name = removeDress.getName();
                            String fname = "/data/data/com.example.lookdiary/cache"+ "/" + type + "_" + name + "_img"; // 이미지 파일 경로
                            File imgFile = new File(fname);
                            imgFile.delete(); // 이미지 파일 삭제

                            fname = itemView.getContext().getFilesDir().getAbsolutePath() +"/dress_"+name; // 옷 정보 파일 경로
                            File file = new File(fname);
                            file.delete(); // 옷 정보 파일 삭제

                            dresses.remove(pos); // 뷰에서 지우기
                            notifyDataSetChanged();
                            Toast.makeText(itemView.getContext(),"해당 아이템을 성공적으로 삭제했습니다.",Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlg.show();

                    return true;
                }
            });
        }
    }

    // position에 해당하는 데이터를 뷰홀더 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dress dress = dresses.get(position);
        String type = dress.getType();
        String name = dress.getName();
        holder.type.setText(type);
        holder.name.setText("상품명 : "+name);
        holder.cost.setText("가격 : "+dress.getCost());
        holder.brand.setText("브랜드 : "+dress.getBrande());
        holder.size.setText("사이즈 : "+dress.getSize());

        String ImgFilePath = "/data/data/com.example.lookdiary/cache"+ "/" + type + "_" + name + "_img"; // 이미지 파일 경로
        Bitmap bitmap = BitmapFactory.decodeFile(ImgFilePath);
        holder.imageView.setImageBitmap(bitmap);
    }


    @Override
    public int getItemCount() {
        return dresses.size();
    }

    // 아이템 뷰를 위한 뷰홀더 객체 생성하여 return
    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item,parent,false);
        CustomAdapter.ViewHolder viewHolder = new CustomAdapter.ViewHolder(view);

        return viewHolder;
    }

}


