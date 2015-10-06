package cn.edu.dlut.tiyuguan.fragment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import cn.edu.dlut.tiyuguan.activity.TiyuguanGuideActivity;
import cn.edu.dlut.tiyuguan.activity.FitAdviceActivity;
import cn.edu.dlut.tiyuguan.core.sportanalysis.SportAnalysisActivity;
import cn.edu.dlut.tiyuguan.core.weather.WeatherActivity;
import cn.edu.dlut.tiyuguan.adapterview.MyGridView;
import cn.edu.dlut.tiyuguan.global.Img;
import cn.edu.dlut.tiyuguan.core.location.MapActivity;

import cn.edu.dlut.tiyuguan.ChildAnimationExample;
import cn.edu.dlut.tiyuguan.R;
import cn.edu.dlut.tiyuguan.util.AppUtil;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.*;

public class MainTab01Fragment extends Fragment implements BaseSliderView.OnSliderClickListener
{
	 private SliderLayout mDemoSlider;
	 private View view;


     private int resImage[]={R.drawable.ic_action_cloudy,R.drawable.ic_action_calculator,R.drawable.ic_action_location,
			 R.drawable.ic_action_news,R.drawable.ic_action_alarm,R.drawable.ic_action_alarm,R.drawable.ic_action_tv,R.drawable.ic_action_book,R.drawable.ic_action_alarm};
	 private String resString[]={"大连天气","我的运动分析","附近的人","体育馆指南","运动提醒","运动指南"};

	 protected LinkedHashMap<String, String> url_maps;

     private LayoutInflater inflater;
	 private ViewGroup container;

     Bundle savedInstanceState;
	 public Handler parentHandler=new Handler(){
		 public void handleMessage(Message msg) {
			 if(msg.what==0x1237){
				 String str=msg.obj.toString();
					if(str!=null)
						Toast.makeText(getActivity(),str, Toast.LENGTH_LONG).show();
			 }
		 };
	 };
	 

	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		this.container = container;
		this.savedInstanceState=savedInstanceState;
        initView();
		if(Img.img != null){
			url_maps = Img.img;
		}
		else{
			System.out.println("为空");
			 //新建一个线程去实时访问img可用不
			if(AppUtil.isConnected(getActivity())){
			}
			else{
				System.out.println("网络连接不可用");
			}
			 
		}

		return this.view;
	}
/*初始化视图*/
    private  void initView(){
        this.view = (getLayoutInflater(savedInstanceState)).inflate(R.layout.main_tab_01, null, false);

        initSliderView();

        MyGridView gridView = (MyGridView)view.findViewById(R.id.homegridview);
        if(gridView!=null){
            gridView.setAdapter(new MyGridViewAdapter());
            gridView.setSelector(R.color.blue);
        }
    }

    private void initSliderView(){
        //从本地加载
        HashMap<String,Integer> file_maps = new HashMap<>();
        file_maps.put("Hannibal",R.drawable.hannibal);
        file_maps.put("Big Bang Theory",R.drawable.bigbang);
        file_maps.put("House of Cards",R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);
        //从网络加载
        url_maps = new LinkedHashMap<>();
        url_maps.put("学校第十届教职工代表大会第四次会议开幕", "http://www.dlut.edu.cn/_mediafile/testdlut2/2015/03/05/_thumb/2rntyetvov.jpg");
        url_maps.put("高水平艺术团开考 郭东明校长视察现场", "http://upload.dlut.edu.cn/2015/0310/1425956025882.jpg");
        url_maps.put("2014.11.28辽宁省各市知识产权局局长参观", "http://chuangxin.dlut.edu.cn/_mediafile/chuangxin/2014/11/28/_thumb/2p5u5s1dh8.jpg");
        url_maps.put("3月4日大连市委书记唐军视察我院学生开发的3D打印机", "http://chuangxin.dlut.edu.cn/_mediafile/chuangxin/2015/03/09/4ppw8qfo2b.jpg");
        //网络操作，应该放在线程里面的额

        mDemoSlider=(SliderLayout)view.findViewById(R.id.slider);

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());//根据url的数量去新建sliderView
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.bundle(new Bundle());
            Bundle bundle = textSliderView.getBundle();
            if(bundle != null)
                bundle.putString("extra",name);
            else
                System.out.println("bundle is null");
            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
    }
    @Override
    public void onResume() {
	// TODO Auto-generated method stub
	if(view!=null)
	     ((ScrollView) view).smoothScrollTo(0, 0);
	
	super.onResume();
}
    /*下面是点击sliderView后的回调方法*/
	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
	}
	//gridview的适配器
	 class MyGridViewAdapter extends BaseAdapter{
		

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 6;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view=getActivity().getLayoutInflater().inflate(R.layout.grid_item_view, null);
			((ImageView)view.findViewById(R.id.grid_item_view_imageview)).setImageResource(resImage[position]);
			((TextView)view.findViewById(R.id.grid_item_view_textview)).setText(resString[position]);
			view.setTag(resString[position]);//给view加便签
			view.setOnClickListener(new GridViewOnClickListener());//添加监听事件
			return view;
		}
		
	}
   //监听gridview的点击事件
	class GridViewOnClickListener implements View.OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String tag=v.getTag().toString();
		switch(tag){
		case "大连天气":
			startActivity(new Intent(getActivity(), WeatherActivity.class));			
			break;
		case "体育馆指南":
			startActivity(new Intent(getActivity(), TiyuguanGuideActivity.class));
			break;
		case "运动指南":
			startActivity(new Intent(getActivity(), FitAdviceActivity.class));

			break;
		case "我的运动分析":
			startActivity(new Intent(getActivity(), SportAnalysisActivity.class));
			break;
		case "附近的人":
			startActivity(new Intent(getActivity(), MapActivity.class));
			break;
		
		}
	}
		
	}
	

}




