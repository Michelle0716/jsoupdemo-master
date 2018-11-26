package me.mikasa.science.model;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import me.mikasa.science.bean.SongShu;
import me.mikasa.science.contract.GetSongShuContract;
import me.mikasa.science.utils.JsoupUtil;
import me.mikasa.science.utils.OkHttpResultCallback;
import me.mikasa.science.utils.OkHttpUtil;
import okhttp3.Call;
import okhttp3.Connection;

import static me.mikasa.science.constants.Constant.SongShuHui_Head;
import static me.mikasa.science.constants.Constant.SongShuHui_Tail;

/**
 * Created by mikasacos on 2018/9/27.
 */

public class GetSongShuModelImpl implements GetSongShuContract.Model {
    private String TAG="GetSongShuModelImpl";
    private GetSongShuContract.Presenter mPresenter;
    public GetSongShuModelImpl(GetSongShuContract.Presenter presenter){
        this.mPresenter=presenter;
    }

    @Override
    public void getSongShu(final int page) {
        //  http://songshuhui.net/archives/category/major/astro/page/2?tag=%E5%8E%9F%E5%88%9B&pagetag=yuanchuang
        String url=SongShuHui_Head+String.valueOf(page)+SongShuHui_Tail;
        Log.e(TAG,url);
        OkHttpUtil.getInstance().getAsync(url,
                new OkHttpResultCallback() {
            @Override
            public void onError(Call call, Exception e) {
            }

            @Override
            public void onResponse(byte[] bytes) {
                try {
                    String s=new String(bytes,"utf-8");
                    Log.e(TAG,"松鼠数据： "+s);//html页面源码
                    List<SongShu> list= JsoupUtil.getInstance().getSongShu(s);
                    if (list!=null){
                        if (page==1){
                            //  它用于将一个 list 集合中的元素顺序进行打乱 ,类似于洗牌的过程,
                            Collections.shuffle(list);
                        }
                        mPresenter.getSongshuSuccess(list);
                    }else {
                        mPresenter.getError("获取网络数据出错...");
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
